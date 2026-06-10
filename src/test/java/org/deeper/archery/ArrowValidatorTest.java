package org.deeper.archery;

import org.deeper.archery.domain.Arrow;
import org.deeper.archery.domain.Point;
import org.deeper.archery.exception.InvalidArrowException;
import org.deeper.archery.validator.ArrowValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArrowValidatorTest {
    private ArrowValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ArrowValidator();
    }

    @Test
    void validate_shouldNotThrow_whenStartAndEndPointsAreDifferent() {
        Arrow arrow = new Arrow(
                new Point(0.0, 0.0),
                new Point(1.0, 1.0)
        );

        assertThatCode(() -> validator.validate(arrow))
                .doesNotThrowAnyException();
    }

    @Test
    void validate_shouldThrowInvalidArrowException_whenStartAndEndPointsAreEqual() {
        assertThrows(InvalidArrowException.class, () -> new Arrow(
                new Point(1.0, 1.0),
                new Point(1.0, 1.0)
        ));
    }

    @Test
    void validate_shouldThrowInvalidArrowException_whenPointsDifferLessThanEpsilon() {
        assertThrows(InvalidArrowException.class, () -> new Arrow(
                new Point(1.0, 1.0),
                new Point(
                        1.0 + 1e-10,
                        1.0 + 1e-10
                )
        ));
    }

    @Test
    void validate_shouldNotThrow_whenPointsDifferMoreThanEpsilon() {
        Arrow arrow = new Arrow(
                new Point(1.0, 1.0),
                new Point(
                        1.0 + 1e-8,
                        1.0 + 1e-8
                )
        );

        assertThatCode(() -> validator.validate(arrow))
                .doesNotThrowAnyException();
    }
}