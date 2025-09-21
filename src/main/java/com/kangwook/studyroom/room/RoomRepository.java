package com.kangwook.studyroom.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("""
    SELECT r, res
    FROM Room r
    LEFT JOIN Reservation res
        ON res.roomId = r.id
       AND res.startAt < :dayEnd
       AND res.endAt > :dayStart
    """)
    List<Object[]> findRoomsWithReservations(
            @Param("dayStart") Instant dayStart,
            @Param("dayEnd") Instant dayEnd
    );
}
