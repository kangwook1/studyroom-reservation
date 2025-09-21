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
import org.springframework.web.bind.annotation.*;

import static com.kangwook.studyroom.global.common.StatusCode.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    @AuthRequired(value = Role.USER)
    public ResponseEntity<CommonResponse<ReservationRes>> createReservation(HttpServletRequest request, @RequestBody @Valid ReservationReq reservationReq) {
        Long userId=(Long)request.getAttribute("userId");

        return ResponseEntity.status(RESERVATION_CREATED.getStatus())
                .body(CommonResponse.from(RESERVATION_CREATED.getMessage(),reservationService.createReservation(userId, reservationReq)));
    }

    @DeleteMapping("/{id}")
    @AuthRequired(value = {Role.ADMIN, Role.USER} )
    public ResponseEntity<CommonResponse<Object>> deleteReservation(HttpServletRequest request, @PathVariable("id") Long reservationId) {
        Role role=(Role)request.getAttribute("role");
        Long userId=(Long)request.getAttribute("userId");

        reservationService.deleteReservation(role, userId, reservationId);

        return ResponseEntity.status(RESERVATION_DELETED.getStatus())
                .body(CommonResponse.from(RESERVATION_DELETED.getMessage()));
    }
}
