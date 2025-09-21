package com.kangwook.studyroom.room;


import com.kangwook.studyroom.global.annotation.AuthRequired;
import com.kangwook.studyroom.global.common.CommonResponse;
import com.kangwook.studyroom.global.common.Role;
import com.kangwook.studyroom.room.dto.req.RoomReq;
import com.kangwook.studyroom.room.dto.res.RoomAvailabilityRes;
import com.kangwook.studyroom.room.dto.res.RoomRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.kangwook.studyroom.global.common.StatusCode.ROOM_CREATED;
import static com.kangwook.studyroom.global.common.StatusCode.ROOM_FOUND;


@Tag(name = "[회의실]", description = "회의실 생성 및 회의실 예약 현황 조회")
@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;


    @Operation(summary = "ADMIN 전용 - 회의실 생성", description = "회의실을 생성합니다. <br> "
            + "중복된 이름은 사용할 수 없습니다.")
    @PostMapping
    @AuthRequired(value = Role.ADMIN)
    public ResponseEntity<CommonResponse<RoomRes>> createRoom(@RequestBody @Valid RoomReq roomReq) {

        return ResponseEntity.status(ROOM_CREATED.getStatus())
                .body(CommonResponse.from(ROOM_CREATED.getMessage(), roomService.createRoom(roomReq)));
    }


    @Operation(summary = "회의실 예약 현황 조회", description = "날짜에 해당하는 회의실의 예약 내역과 빈 시간대를 조회합니다. <br> "
            + "응답 데이터의 시간은 UTC 기준입니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<List<RoomAvailabilityRes>>> createRoom(@RequestParam LocalDate date) {

        return ResponseEntity.status(ROOM_FOUND.getStatus())
                .body(CommonResponse.from(ROOM_FOUND.getMessage(), roomService.getRoomAvailability(date)));

    }
}
