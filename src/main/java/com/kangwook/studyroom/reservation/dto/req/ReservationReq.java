package com.kangwook.studyroom.reservation.dto.req;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kangwook.studyroom.reservation.Reservation;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class ReservationReq {

    @NotNull(message = "roomId는 필수 입력값입니다.")
    private Long roomId;

    @NotNull(message = "시작 시간은 필수 입력값입니다.")
    private Instant startAt;

    @NotNull(message = "종료 시간은 필수 입력값입니다.")
    private Instant endAt;

    @AssertTrue(message = "시작 시간은 종료 시간보다 빨라야 합니다.")
    @JsonIgnore
    public boolean isValidTimeRange() {
        if (startAt == null || endAt == null) return true;
        return startAt.isBefore(endAt);
    }

    // 테스트용
    public ReservationReq(Long roomId, Instant startAt, Instant endAt) {
        this.roomId = roomId;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Reservation toEntity(Long memberId) {
        return Reservation.builder()
                .startAt(startAt)
                .endAt(endAt)
                .roomId(roomId)
                .memberId(memberId)
                .build();
    }
}
