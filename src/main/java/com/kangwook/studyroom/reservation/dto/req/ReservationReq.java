package com.kangwook.studyroom.reservation.dto.req;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kangwook.studyroom.reservation.Reservation;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor
public class ReservationReq {

    @NotNull(message = "roomId는 필수 입력값입니다.")
    private Long roomId;

    @NotNull(message = "시작 시간은 필수 입력값입니다.")
    private Instant startAt;

    @NotNull(message = "종료 시간은 필수 입력값입니다.")
    private Instant endAt;

    @AssertTrue(message = "시작 시간은 현재 시각 이후여야 합니다.")
    @JsonIgnore
    public boolean isStartAtAfterOrEqualsNow() {
        if (startAt == null) return true;
        return !startAt.isBefore(Instant.now()); 
    }

    @AssertTrue(message = "시작 시간은 종료 시간보다 빨라야 합니다.")
    @JsonIgnore
    public boolean isValidTimeRange() {
        if (startAt == null || endAt == null) return true;
        return startAt.isBefore(endAt);
    }

    @AssertTrue(message = "종료 시간은 당일까지만 가능합니다.")
    @JsonIgnore
    public boolean isEndAtBeforeToday() {
        if (endAt == null) return true;

        Instant tomorrowUtc = Instant.now().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS);
        return endAt.isBefore(tomorrowUtc);
    }

    // 테스트용
    public ReservationReq(Long roomId, Instant startAt, Instant endAt) {
        this.roomId = roomId;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Reservation toEntity(Long userId) {
        return Reservation.builder()
                .startAt(startAt)
                .endAt(endAt)
                .roomId(roomId)
                .userId(userId)
                .build();
    }
}
