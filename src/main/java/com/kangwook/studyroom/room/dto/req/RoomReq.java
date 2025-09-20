package com.kangwook.studyroom.room.dto.req;

import com.kangwook.studyroom.room.Room;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomReq {

    @NotBlank(message = "이름은 필수입력값입니다.")
    private String name;

    @NotBlank(message = "위치는 필수입력값입니다.")
    private String location;

    @NotNull
    private Integer capacity;

    public Room toEntity(){
        return Room.builder()
                .name(name)
                .location(location)
                .capacity(capacity)
                .build();
    }
}
