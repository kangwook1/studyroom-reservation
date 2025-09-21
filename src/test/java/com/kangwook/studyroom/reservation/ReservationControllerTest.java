package com.kangwook.studyroom.reservation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangwook.studyroom.reservation.dto.req.ReservationReq;
import com.kangwook.studyroom.reservation.dto.res.ReservationRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    @DisplayName("USER 토큰으로 예약 생성 API 인증,인가 성공")
    void createReservation_Success() throws Exception {
        // 생성자로만 초기화
        ReservationReq request = new ReservationReq(
                1L,
                Instant.parse("2025-09-21T10:00:00Z"),
                Instant.parse("2025-09-21T11:00:00Z")
        );

        ReservationRes response = ReservationRes.builder()
                .id(1L)
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .build();

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "user-token-1")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("ADMIN 토큰으로 예약 생성 API 인증 성공, 인가 실패")
    void createReservation_Forbidden() throws Exception {
        ReservationReq request = new ReservationReq(
                1L,
                Instant.parse("2025-09-21T10:00:00Z"),
                Instant.parse("2025-09-21T11:00:00Z")
        );

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "admin-token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("토큰 없이 예약 생성 API 인증 실패")
    void createReservation_UnAuthorized() throws Exception {
        ReservationReq request = new ReservationReq(
                1L,
                Instant.parse("2025-09-21T10:00:00Z"),
                Instant.parse("2025-09-21T11:00:00Z")
        );

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
