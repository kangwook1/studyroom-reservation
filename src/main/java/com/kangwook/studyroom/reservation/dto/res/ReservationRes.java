package com.kangwook.studyroom.reservation.dto.res;

import com.kangwook.studyroom.reservation.Reservation;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ReservationRes {

    private final Long id;
    private final Instant startAt;
    private final Instant endAt;
    private final Long roomId;

    @Builder
    public ReservationRes(Long id, Instant startAt, Instant endAt, Long roomId) {
        this.id = id;
        this.startAt = startAt;
        this.endAt = endAt;
        this.roomId = roomId;

    }

    public static ReservationRes from(Reservation reservation) {
        return ReservationRes.builder()
                .id(reservation.getId())
                .startAt(reservation.getStartAt())
                .endAt(reservation.getEndAt())
                .roomId(reservation.getRoomId())
                .build();
    }
}
