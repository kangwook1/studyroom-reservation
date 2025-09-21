package com.kangwook.studyroom.room.dto.res;

import com.kangwook.studyroom.reservation.dto.res.EmptyReservationRes;
import com.kangwook.studyroom.reservation.dto.res.ReservationRes;
import lombok.Getter;

import java.util.List;

@Getter
public class RoomAvailabilityRes {
    private final RoomRes roomRes;
    private final List<ReservationRes> reservationRes;
    private final List<EmptyReservationRes> emptyReservations;

    public RoomAvailabilityRes(RoomRes roomRes, List<ReservationRes> reservationRes, List<EmptyReservationRes> emptyReservations) {
        this.roomRes = roomRes;
        this.reservationRes = reservationRes;
        this.emptyReservations = emptyReservations;
    }
}
