package com.kangwook.studyroom.room;

import com.kangwook.studyroom.room.dto.req.RoomReq;
import com.kangwook.studyroom.room.dto.res.RoomRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;


    @Transactional
    public RoomRes createRoom(RoomReq roomReq){
        Room room= roomRepository.save(roomReq.toEntity());
        return RoomRes.from(room);
    }
}
