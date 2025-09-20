package com.kangwook.studyroom.room.dto.req;

import com.kangwook.studyroom.room.Room;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomReq {

    @NotBlank(message = "이름은 필수입력값입니다.")
    private String name;

    @NotBlank(message = "위치는 필수입력값입니다.")
    private String location;

    @NotBlank(message = "수용 인원은 필수입력값입니다.")
    private Integer capacity;

    public Room toEntity(){
        return Room.builder()
                .name(name)
                .location(location)
                .capacity(capacity)
                .build();
    }
}
