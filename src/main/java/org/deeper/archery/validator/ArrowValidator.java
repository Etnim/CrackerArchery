package org.deeper.archery.validator;

import org.deeper.archery.domain.Arrow;
import org.deeper.archery.domain.Point;
import org.deeper.archery.exception.InvalidArrowException;
import org.springframework.stereotype.Component;

@Component
public class ArrowValidator implements Validator<Arrow> {
    private static final double EPSILON = 1e-9;

    @Override
    public void validate(Arrow arrow) {
        if (isSamePoint(arrow.getStart(), arrow.getEnd())) {
            throw new InvalidArrowException(
                    "Arrow start and end points must be different"
            );
        }
    }

    private boolean isSamePoint(Point first, Point second) {
        return areEqual(first.x(), second.x())
                && areEqual(first.y(), second.y());
    }

    private boolean areEqual(double first, double second) {
        return Math.abs(first - second) < EPSILON;
    }
}
