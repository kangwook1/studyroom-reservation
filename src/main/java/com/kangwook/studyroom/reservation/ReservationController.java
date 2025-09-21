package com.kangwook.studyroom.reservation;


import com.kangwook.studyroom.global.annotation.AuthRequired;
import com.kangwook.studyroom.global.common.CommonResponse;
import com.kangwook.studyroom.global.common.Role;
import com.kangwook.studyroom.reservation.dto.req.ReservationReq;
import com.kangwook.studyroom.reservation.dto.res.ReservationRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kangwook.studyroom.global.common.StatusCode.RESERVATION_CREATED;
import static com.kangwook.studyroom.global.common.StatusCode.RESERVATION_DELETED;


@Tag(name = "[예약]", description = "예약 생성 및 삭제")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;


    @Operation(summary = "USER 전용 - 예약 생성", description = "원하는 시간대의 예약을 생성합니다. <br>"
            + "겹치는 시간대는 예약할 수 없습니다.")
    @PostMapping
    @AuthRequired(value = Role.USER)
    public ResponseEntity<CommonResponse<ReservationRes>> createReservation(HttpServletRequest request, @RequestBody @Valid ReservationReq reservationReq) {
        Long userId = (Long) request.getAttribute("userId");

        return ResponseEntity.status(RESERVATION_CREATED.getStatus())
                .body(CommonResponse.from(RESERVATION_CREATED.getMessage(), reservationService.createReservation(userId, reservationReq)));
    }

    @Operation(summary = "ADMIN, USER 전용 - 예약 취소", description = "Admin - 모든 예약 내역을 취소 가능합니다. <br>"
            + "USER - 자신이 예약한 내역에 한해 취소할 수 있습니다.")
    @DeleteMapping("/{id}")
    @AuthRequired(value = {Role.ADMIN, Role.USER})
    public ResponseEntity<CommonResponse<Object>> deleteReservation(HttpServletRequest request, @PathVariable("id") Long reservationId) {
        Role role = (Role) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");

        reservationService.deleteReservation(role, userId, reservationId);

        return ResponseEntity.status(RESERVATION_DELETED.getStatus())
                .body(CommonResponse.from(RESERVATION_DELETED.getMessage()));
    }
}
