package com.kangwook.studyroom.room;


import com.kangwook.studyroom.global.annotation.AuthRequired;
import com.kangwook.studyroom.global.common.CommonResponse;
import com.kangwook.studyroom.global.common.Role;
import com.kangwook.studyroom.room.dto.req.RoomReq;
import com.kangwook.studyroom.room.dto.res.RoomAvailabilityRes;
import com.kangwook.studyroom.room.dto.res.RoomRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.kangwook.studyroom.global.common.StatusCode.ROOM_CREATED;
import static com.kangwook.studyroom.global.common.StatusCode.ROOM_FOUND;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @AuthRequired(value = Role.ADMIN)
    public ResponseEntity<CommonResponse<RoomRes>> createRoom(@RequestBody @Valid RoomReq roomReq) {

        return ResponseEntity.status(ROOM_CREATED.getStatus())
                .body(CommonResponse.from(ROOM_CREATED.getMessage(),roomService.createRoom(roomReq)));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<RoomAvailabilityRes>>> createRoom(@RequestParam LocalDate date ) {

        return ResponseEntity.status(ROOM_FOUND.getStatus())
                .body(CommonResponse.from(ROOM_FOUND.getMessage(),roomService.getRoomAvailability(date)));

    }
}
