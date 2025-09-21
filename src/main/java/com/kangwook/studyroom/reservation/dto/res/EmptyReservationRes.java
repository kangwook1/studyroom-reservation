package com.kangwook.studyroom.reservation.dto.res;

import lombok.Getter;

import java.time.Instant;

@Getter
public class EmptyReservationRes {
    private final Instant startAt;
    private final Instant endAt;

    public EmptyReservationRes(Instant startAt, Instant endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
