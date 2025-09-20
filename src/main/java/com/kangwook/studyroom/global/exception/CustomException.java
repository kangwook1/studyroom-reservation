package com.kangwook.studyroom.global.exception;

import com.kangwook.studyroom.global.common.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final StatusCode statusCode;
}
