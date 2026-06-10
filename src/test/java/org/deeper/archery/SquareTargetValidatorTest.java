package org.deeper.archery;

import org.deeper.archery.domain.Point;
import org.deeper.archery.domain.SquareTarget;
import org.deeper.archery.exception.InvalidTargetException;
import org.deeper.archery.validator.SquareTargetValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SquareTargetValidatorTest {
    private SquareTargetValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SquareTargetValidator();
    }

    @Test
    void validate_shouldNotThrow_whenTargetIsValidSquare() {
        SquareTarget target = new SquareTarget(
                new Point(0.0, 1.0),
                new Point(1.0, 1.0),
                new Point(1.0, 0.0),
                new Point(0.0, 0.0)
        );

        assertThatCode(() -> validator.validate(target))
                .doesNotThrowAnyException();
    }

    @Test
    void validate_shouldThrow_whenPointsAreDuplicated() {
        assertThrows(InvalidTargetException.class, () -> new SquareTarget(
                new Point(0.0, 1.0),
                new Point(1.0, 1.0),
                new Point(1.0, 0.0),
                new Point(1.0, 0.0)));
    }

    @Test
    void validate_shouldThrow_whenSideLengthIsZero() {
        assertThrows(InvalidTargetException.class, () -> new SquareTarget(
                new Point(0.0, 1.0),
                new Point(0.0, 1.0),
                new Point(1.0, 0.0),
                new Point(0.0, 0.0)));
    }

    @Test
    void validate_shouldThrow_whenSidesDoNotHaveEqualLength() {
        assertThrows(InvalidTargetException.class, () -> new SquareTarget(
                new Point(0.0, 1.0),
                new Point(2.0, 1.0),
                new Point(2.0, 0.0),
                new Point(0.0, 0.0)));
    }

    @Test
    void validate_shouldThrow_whenDiagonalsDoNotHaveEqualLength() {
        assertThrows(InvalidTargetException.class, () -> new SquareTarget(
                new Point(0.0, 1.0),
                new Point(1.0, 1.0),
                new Point(1.2, 0.0),
                new Point(0.0, 0.0)));
    }

    @Test
    void validate_shouldThrow_whenAdjacentSidesAreNotPerpendicular() {
        assertThrows(InvalidTargetException.class, () -> new SquareTarget(
                new Point(0.0, 0.0),
                new Point(1.0, 0.0),
                new Point(1.5, 1.0),
                new Point(0.5, 1.0)));
    }

    @Test
    void validate_shouldNotThrow_whenTargetIsRotatedSquare() {
        SquareTarget target = new SquareTarget(
                new Point(0.0, 1.0),
                new Point(1.0, 2.0),
                new Point(2.0, 1.0),
                new Point(1.0, 0.0)
        );

        assertThatCode(() -> validator.validate(target))
                .doesNotThrowAnyException();
    }
}
