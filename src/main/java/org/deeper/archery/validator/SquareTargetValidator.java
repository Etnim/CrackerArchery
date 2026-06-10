package org.deeper.archery.validator;

import org.deeper.archery.domain.Point;
import org.deeper.archery.domain.SquareTarget;
import org.deeper.archery.exception.InvalidTargetException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SquareTargetValidator implements Validator<SquareTarget> {

    private static final double EPSILON = 1e-9;

    @Override
    public void validate(SquareTarget target) {

        Point a = target.getA();
        Point b = target.getB();
        Point c = target.getC();
        Point d = target.getD();

        validatePointsAreUnique(List.of(a,b,c,d));
        validateSideLengthIsNotZero(a, b);
        validateAllSidesHaveEqualLength(a, b, c, d);
        validateDiagonalsHaveEqualLength(a, b, c, d);
        validateAdjacentSidesArePerpendicular(a, b, d);
    }

    private void validatePointsAreUnique(List<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                if (areSamePoint(points.get(i), points.get(j))) {
                    throw new InvalidTargetException("Square target points must not be duplicated");
                }
            }
        }
    }

    private void validateSideLengthIsNotZero(Point p1, Point p2) {
        if (isZero(distanceSquared(p1, p2))) {
            throw new InvalidTargetException("Square target side length must be greater than zero");
        }
    }

    private void validateAllSidesHaveEqualLength(Point p1, Point p2, Point p3, Point p4) {
        double side1 = distanceSquared(p1, p2);
        double side2 = distanceSquared(p2, p3);
        double side3 = distanceSquared(p3, p4);
        double side4 = distanceSquared(p4, p1);

        if (!areEqual(side1, side2)
                || !areEqual(side2, side3)
                || !areEqual(side3, side4)) {
            throw new InvalidTargetException("Square target sides must have equal length");
        }
    }

    private void validateDiagonalsHaveEqualLength(Point p1, Point p2, Point p3, Point p4) {
        double diagonal1 = distanceSquared(p1, p3);
        double diagonal2 = distanceSquared(p2, p4);

        if (!areEqual(diagonal1, diagonal2)) {
            throw new InvalidTargetException("Square target diagonals must have equal length");
        }
    }

    private void validateAdjacentSidesArePerpendicular(Point p1, Point p2, Point p4) {
        double dotProduct = dotProduct(p2, p1, p4);

        if (!isZero(dotProduct)) {
            throw new InvalidTargetException("Square target adjacent sides must be perpendicular");
        }
    }

    private double distanceSquared(Point first, Point second) {
        double dx = first.x() - second.x();
        double dy = first.y() - second.y();

        return dx * dx + dy * dy;
    }

    private double dotProduct(Point first, Point origin, Point second) {
        double firstVectorX = first.x() - origin.x();
        double firstVectorY = first.y() - origin.y();

        double secondVectorX = second.x() - origin.x();
        double secondVectorY = second.y() - origin.y();

        return firstVectorX * secondVectorX + firstVectorY * secondVectorY;
    }

    private boolean areSamePoint(Point first, Point second) {
        return areEqual(first.x(), second.x()) && areEqual(first.y(), second.y());
    }

    private boolean areEqual(double first, double second) {
        return Math.abs(first - second) < EPSILON;
    }

    private boolean isZero(double value) {
        return Math.abs(value) < EPSILON;
    }
}