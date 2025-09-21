package com.kangwook.studyroom.room;

import com.kangwook.studyroom.reservation.ReservationRepository;
import com.kangwook.studyroom.reservation.ReservationService;
import com.kangwook.studyroom.reservation.dto.req.ReservationReq;
import com.kangwook.studyroom.reservation.dto.res.ReservationRes;
import com.kangwook.studyroom.room.dto.req.RoomReq;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReservationConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Long roomId;

    @BeforeEach
    void setUp() {
        Room room=roomRepository.save(new RoomReq("회의실1", "A-7", 6).toEntity());
        roomId=room.getId();
    }

    @AfterEach
    void cleanup() {
        reservationRepository.deleteAll();
        roomRepository.deleteAll();

    }

    @Test
    @DisplayName("회의실 예약 동시성 테스트")
    void concurrentReservationTest() throws InterruptedException {

        int numberOfThreads = 10;
        Instant startAt = Instant.parse("2025-09-21T12:00:00Z");
        Instant endAt = Instant.parse("2025-09-21T13:00:00Z");

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Exception> exceptions = new ArrayList<>();
        List<Long> responseTimes = new ArrayList<>();

        long testStartTime = System.nanoTime();

        for (int i = 0; i < numberOfThreads; i++) {
            final long userId = i + 1;
            executorService.submit(() -> {
                long startTime = System.nanoTime();
                try {
                    ReservationReq req = new ReservationReq(roomId, startAt, endAt);
                    ReservationRes res = reservationService.createReservation(userId, req);
                    successCount.incrementAndGet();
                } catch (DataIntegrityViolationException e) {
                    synchronized (exceptions) {
                        exceptions.add(e);
                    }
                } catch (Exception e) {
                    synchronized (exceptions) {
                        exceptions.add(e);
                    }
                } finally {
                    long endTime = System.nanoTime();
                    synchronized (responseTimes) {
                        responseTimes.add(endTime - startTime);
                    }
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long testEndTime = System.nanoTime();
        long totalTestTimeMillis = (testEndTime - testStartTime) / 1_000_000;

        System.out.println("성공한 예약 수: " + successCount.get());
        System.out.println("발생한 예외 수: " + exceptions.size());

        long fastestResponse = responseTimes.stream().min(Long::compare).orElse(0L) / 1_000_000;
        long slowestResponse = responseTimes.stream().max(Long::compare).orElse(0L) / 1_000_000;
        double averageResponse = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0) / 1_000_000.0;

        exceptions.forEach(ex -> System.out.println("예외 종류: " + ex.getClass().getSimpleName()));

        System.out.println("최단 응답 시간: " + fastestResponse + "ms");
        System.out.println("최장 응답 시간: " + slowestResponse + "ms");
        System.out.println("평균 응답 시간: " + averageResponse + "ms");
        System.out.println("전체 테스트 시간: " + totalTestTimeMillis + "ms");

        // 검증
        assertEquals(1, successCount.get(), "동시에 하나만 성공해야 합니다");
        assertEquals(numberOfThreads - 1, exceptions.size(), "나머지는 예외가 발생해야 합니다");

    }
}
