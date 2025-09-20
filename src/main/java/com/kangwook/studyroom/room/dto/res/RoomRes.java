package com.kangwook.studyroom.room.dto.res;

import com.kangwook.studyroom.room.Room;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomRes {
    private final Long id;
    private final String name;
    private final String location;
    private final Integer capacity;

    @Builder
    public RoomRes(Long id, String name, String location, Integer capacity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }

    public static RoomRes from(Room room) {
        return new RoomRes(room.getId(), room.getName(), room.getLocation(), room.getCapacity());
    }
}
