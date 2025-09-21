package com.kangwook.studyroom.reservation;


import com.kangwook.studyroom.global.annotation.AuthRequired;
import com.kangwook.studyroom.global.common.CommonResponse;
import com.kangwook.studyroom.global.common.Role;
import com.kangwook.studyroom.reservation.dto.req.ReservationReq;
import com.kangwook.studyroom.reservation.dto.res.ReservationRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kangwook.studyroom.global.common.StatusCode.ROOM_CREATED;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    @AuthRequired(value = Role.USER)
    public ResponseEntity<CommonResponse<ReservationRes>> createReservation(HttpServletRequest request, @RequestBody @Valid ReservationReq reservationReq) {
        Long userId=Long.parseLong((String)request.getAttribute("userId"));

        return ResponseEntity.status(ROOM_CREATED.getStatus())
                .body(CommonResponse.from(ROOM_CREATED.getMessage(),reservationService.createReservation(userId, reservationReq)));
    }
}
