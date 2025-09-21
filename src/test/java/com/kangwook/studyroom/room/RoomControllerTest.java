package com.kangwook.studyroom.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangwook.studyroom.room.dto.req.RoomReq;
import com.kangwook.studyroom.room.dto.res.RoomRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoomService roomService;

    @Test
    @DisplayName("ADMIN 토큰으로 회의실 생성 API 인증, 인가 성공")
    void createRoom_Success() throws Exception {
        RoomReq request = new RoomReq("회의실1", "A-7", 6);
        RoomRes response = new RoomRes(1L, "회의실1", "A-7", 6);

        when(roomService.createRoom(any())).thenReturn(response);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "admin-token")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("USER 토큰으로 회의실 생성 API 인증 성공, 인가 실패")
    void createRoom_Forbidden() throws Exception {
        RoomReq request = new RoomReq("회의실1", "A-7", 6);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "user-token-1")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("토큰 없이 회의실 생성 API 인증 실패 ")
    void createRoom_UnAuthorized() throws Exception {
        RoomReq request = new RoomReq("회의실1", "A-7", 6);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
