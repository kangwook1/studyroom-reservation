package com.kangwook.studyroom.room;

import com.kangwook.studyroom.reservation.dto.req.ReservationReq;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationReqValidTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("시작 시간 < 종료 시간")
    void startBeforeEnd_valid() {
        ReservationReq req = new ReservationReq(
                1L,
                Instant.parse("2025-09-21T10:00:00Z"),
                Instant.parse("2025-09-21T11:00:00Z")
        );

        Set<ConstraintViolation<ReservationReq>> violations = validator.validate(req);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("시작 시간 == 종료 시간")
    void startEqualsEnd_invalid() {
        Instant instant = Instant.parse("2025-09-21T10:00:00Z");
        ReservationReq req = new ReservationReq(
                1L,
                instant,
                instant
        );

        Set<ConstraintViolation<ReservationReq>> violations = validator.validate(req);
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("시작 시간은 종료 시간보다 빨라야 합니다.");
    }

    @Test
    @DisplayName("시작 시간 > 종료 시간")
    void startAfterEnd_invalid() {
        ReservationReq req = new ReservationReq(
                1L,
                Instant.parse("2025-09-21T11:00:00Z"),
                Instant.parse("2025-09-21T10:00:00Z")
        );

        Set<ConstraintViolation<ReservationReq>> violations = validator.validate(req);
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("시작 시간은 종료 시간보다 빨라야 합니다.");
    }
}
