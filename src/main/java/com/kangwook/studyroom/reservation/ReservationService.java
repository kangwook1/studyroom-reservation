package com.kangwook.studyroom.reservation;


import com.kangwook.studyroom.global.common.Role;
import com.kangwook.studyroom.global.common.StatusCode;
import com.kangwook.studyroom.global.exception.CustomException;
import com.kangwook.studyroom.reservation.dto.req.ReservationReq;
import com.kangwook.studyroom.reservation.dto.res.ReservationRes;
import com.kangwook.studyroom.room.Room;
import com.kangwook.studyroom.room.RoomRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final EntityManager em;

    @Transactional
    public ReservationRes createReservation(Long userId,ReservationReq reservationReq) {

        Room room = roomRepository.findById(reservationReq.getRoomId())
                .orElseThrow(() -> new CustomException(StatusCode.ROOM_NOT_FOUND));

        setLockTimeout(3000);
        Reservation reservation= reservationRepository.save(reservationReq.toEntity(userId));

        return ReservationRes.from(reservation);

    }

    @Transactional
    public void deleteReservation(Role role, Long userId, Long reservationId) {
        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(StatusCode.RESERVATION_NOT_FOUND));

        if(role==Role.ADMIN){
            reservationRepository.delete(reservation);
        }
        else{
            if(reservation.getUserId().equals(userId)){
                reservationRepository.delete(reservation);
            }
            else throw new CustomException(StatusCode.FORBIDDEN_USER);
        }
    }

    private void setLockTimeout(int millis) {
        em.createNativeQuery("SET LOCAL lock_timeout = '" + millis + "ms'").executeUpdate();
    }
}
