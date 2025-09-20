package com.kangwook.studyroom.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    // Room
    ROOM_CREATED(HttpStatus.CREATED, "회의실이 생성되었습니다."),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INPUT_VALUE_INVALID(BAD_REQUEST,"유효하지 않은 입력입니다."),
    DATA_INTEGRITY_VIOLATION(BAD_REQUEST,"이미 존재하는 값이거나 필수 필드에 null이 있습니다."),


    /* 401 UNAUTHORIZED : 비인증 사용자 */
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    /* 403 FORBIDDEN : 권한 없음 */
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다."),

    /* 404 NOT_FOUND : 존재하지 않는 리소스 */
    MEMBER_NOT_EXIST(NOT_FOUND, "존재하지 않는 회원입니다."),


    /* 409 CONFLICT : 리소스 충돌 */
    COUPON_ALREADY_ISSUED(CONFLICT, "이미 발급된 쿠폰입니다."),

    /* 503 UNAVAILABLE : 서비스 이용 불가  */
    FCM_UNAVAILABLE(SERVICE_UNAVAILABLE, "알림 기능을 이용할 수 없습니다.");


    private final HttpStatus status;
    private final String message;
}
