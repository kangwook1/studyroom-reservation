package com.kangwook.studyroom.room;

import com.kangwook.studyroom.reservation.Reservation;
import com.kangwook.studyroom.reservation.dto.res.EmptyReservationRes;
import com.kangwook.studyroom.reservation.dto.res.ReservationRes;
import com.kangwook.studyroom.room.dto.req.RoomReq;
import com.kangwook.studyroom.room.dto.res.RoomAvailabilityRes;
import com.kangwook.studyroom.room.dto.res.RoomRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;


    @Transactional
    public RoomRes createRoom(RoomReq roomReq) {
        Room room = roomRepository.save(roomReq.toEntity());
        return RoomRes.from(room);
    }

    public List<RoomAvailabilityRes> getRoomAvailability(LocalDate date) {
        // UTC 기준 시간 계산
        Instant dayStart = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant dayEnd = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Room 별 예약 그룹화
        Map<Long, List<ReservationRes>> roomSchedule = new HashMap<>();
        Map<Long, Room> roomMap = new HashMap<>();

        List<Object[]> results = roomRepository.findRoomsWithReservations(dayStart, dayEnd);
        for (Object[] row : results) {
            Room room = (Room) row[0];
            Reservation reservation = (Reservation) row[1];

            roomMap.putIfAbsent(room.getId(), room);

            if (!roomSchedule.containsKey(room.getId())) {
                roomSchedule.put(room.getId(), new ArrayList<ReservationRes>());
            }

            if (reservation != null) {
                roomSchedule.get(room.getId()).add(ReservationRes.from(reservation));
            }
        }

        List<RoomAvailabilityRes> roomAvailabilityList = new ArrayList<>();
        for (Long roomId : roomSchedule.keySet()) {
            Room room = roomMap.get(roomId);
            List<ReservationRes> reservations = roomSchedule.get(roomId);

            // 예약 정렬
            reservations.sort(Comparator.comparing(ReservationRes::getStartAt));

            // 빈 시간대 계산
            List<EmptyReservationRes> emptyTimes = calculateEmptyTimes(dayStart, dayEnd, reservations);

            roomAvailabilityList.add(new RoomAvailabilityRes(RoomRes.from(room), reservations, emptyTimes));
        }

        return roomAvailabilityList;
    }

    private List<EmptyReservationRes> calculateEmptyTimes(Instant dayStart, Instant dayEnd, List<ReservationRes> reservations) {
        List<EmptyReservationRes> emptyTimes = new ArrayList<>();

        // 이전 예약 종료 시간, 초기값은 하루 시작
        Instant prevEnd = dayStart;

        for (ReservationRes curTime : reservations) {
            // 이전 예약 종료 ~ 이번 예약 시작 사이에 빈 시간대가 있으면 추가
            if (prevEnd.isBefore(curTime.getStartAt())) {
                emptyTimes.add(new EmptyReservationRes(prevEnd, curTime.getStartAt()));
            }
            prevEnd = curTime.getEndAt();

        }

        // 마지막 예약 이후 남은 빈 시간대 추가
        if (prevEnd.isBefore(dayEnd)) {
            emptyTimes.add(new EmptyReservationRes(prevEnd, dayEnd));
        }

        return emptyTimes;
    }
}
