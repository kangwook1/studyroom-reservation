package com.kangwook.studyroom.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    // Room
    ROOM_CREATED(CREATED, "회의실이 생성되었습니다."),

    // Reservation
    RESERVATION_CREATED(CREATED, "예약이 완료되었습니다."),
    RESERVATION_DELETED(OK, "예약이 취소되었습니다."),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INPUT_VALUE_INVALID(BAD_REQUEST,"유효하지 않은 입력입니다."),
    DATA_INTEGRITY_VIOLATION(BAD_REQUEST,"데이터 무결성 제약조건을 위반했습니다."),

    /* 401 UNAUTHORIZED : 비인증 사용자 */
    INVALID_TOKEN(UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    /* 403 FORBIDDEN : 권한 없음 */
    FORBIDDEN_USER(FORBIDDEN, "권한이 없는 사용자입니다."),

    /* 404 NOT_FOUND : 존재하지 않는 리소스 */
    ROOM_NOT_FOUND(NOT_FOUND,"존재하지 않는 회의실입니다."),
    RESERVATION_NOT_FOUND(NOT_FOUND,"존재하지 않는 예약입니다."),


    /* 409 CONFLICT : 리소스 충돌 */
    CONCURRENT_RESERVATION(CONFLICT, "현재 리소스에 대한 동시 접근이 많아 처리할 수 없습니다. 잠시 후 다시 시도해주세요.");


    private final HttpStatus status;
    private final String message;
}
