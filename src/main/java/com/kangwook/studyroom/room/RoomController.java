package com.kangwook.studyroom.room;


import com.kangwook.studyroom.global.annotation.AuthRequired;
import com.kangwook.studyroom.global.common.CommonResponse;
import com.kangwook.studyroom.room.dto.req.RoomReq;
import com.kangwook.studyroom.room.dto.res.RoomRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kangwook.studyroom.global.common.StatusCode.ROOM_CREATED;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    @AuthRequired(adminOnly = true)
    public ResponseEntity<CommonResponse<RoomRes>> createRoom(@RequestBody @Valid RoomReq roomReq) {

        return ResponseEntity.status(ROOM_CREATED.getStatus())
                .body(CommonResponse.from(ROOM_CREATED.getMessage(),roomService.createRoom(roomReq)));
    }
}
