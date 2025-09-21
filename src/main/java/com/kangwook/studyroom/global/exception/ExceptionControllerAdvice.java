package com.kangwook.studyroom.global.exception;


import com.kangwook.studyroom.global.common.ErrorResponse;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.kangwook.studyroom.global.common.StatusCode.*;


@RestControllerAdvice
public class ExceptionControllerAdvice {


    // 유효성 검사 실패로 인한 예외로 메시지와 오류 객체도 담는다.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        ErrorResponse errorResponse= ErrorResponse.builder()
                .message(INPUT_VALUE_INVALID.getMessage())
                .validationErrors(ErrorResponse.ValidationError.from(e.getBindingResult()))
                .build();

        return ResponseEntity.status(INPUT_VALUE_INVALID.getStatus())
                .body(errorResponse);
    }

    // DB 무결성 제약에 위반할 경우 발생하는 예외
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        ErrorResponse errorResponse= ErrorResponse.builder()
                .message(DATA_INTEGRITY_VIOLATION.getMessage())
                .build();


        return ResponseEntity.status(DATA_INTEGRITY_VIOLATION.getStatus())
                .body(errorResponse);
    }

    // 예약 시 락 획득 실패 처리
    @ExceptionHandler({CannotAcquireLockException.class})
    public ResponseEntity<ErrorResponse> handleCannotAcquireLockException(CannotAcquireLockException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(CONCURRENT_RESERVATION.getMessage())
                .build();

        return ResponseEntity.status(CONCURRENT_RESERVATION.getStatus())
                .body(errorResponse);
    }


    // 무슨 예외가 터졌는지 메시지만 담는다.
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        ErrorResponse errorResponse=ErrorResponse.builder()
                .message(e.getStatusCode().getMessage())
                .build();

        return ResponseEntity.status(e.getStatusCode().getStatus())
                .body(errorResponse);
    }
}
