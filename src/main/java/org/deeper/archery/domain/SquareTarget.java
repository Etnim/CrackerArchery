package org.deeper.archery.domain;

import lombok.Getter;
import org.deeper.archery.validator.SquareTargetValidator;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SquareTarget extends Target {
    private final Point a;
    private final Point b;
    private final Point c;
    private final Point d;

    private final SquareTargetValidator validator = new SquareTargetValidator();
    private static final double EPSILON = 1e-9;

    public SquareTarget(Point a, Point b, Point c, Point d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        validator.validate(this);
    }

    @Override
    public List<Point> intersectionPoints(Arrow arrow) {
        List<Point> intersections = new ArrayList<>();

        addIntersection(intersections, arrow.getStart(), arrow.getEnd(), a, b);
        addIntersection(intersections, arrow.getStart(), arrow.getEnd(), b, c);
        addIntersection(intersections, arrow.getStart(), arrow.getEnd(), c, d);
        addIntersection(intersections, arrow.getStart(), arrow.getEnd(), d, a);

        return intersections;
    }

    private void addIntersection(
            List<Point> intersections,
            Point arrowStart,
            Point arrowEnd,
            Point sideStart,
            Point sideEnd
    ) {
        List<Point> points = findSegmentIntersection(
                arrowStart,
                arrowEnd,
                sideStart,
                sideEnd
        );

        for (Point point : points) {
            addIfNotDuplicate(intersections, point);
        }
    }

    private List<Point> findSegmentIntersection(
            Point p,
            Point p2,
            Point q,
            Point q2
    ) {
        double rX = p2.x() - p.x();
        double rY = p2.y() - p.y();

        double sX = q2.x() - q.x();
        double sY = q2.y() - q.y();

        double denominator = cross(rX, rY, sX, sY);

        double qMinusPX = q.x() - p.x();
        double qMinusPY = q.y() - p.y();

        double numeratorT = cross(qMinusPX, qMinusPY, sX, sY);
        double numeratorU = cross(qMinusPX, qMinusPY, rX, rY);

        if (isZero(denominator)) {
            if (!isZero(cross(qMinusPX, qMinusPY, rX, rY))) {
                return List.of();
            }

            return findCollinearIntersectionPoints(p, p2, q, q2);
        }

        double t = numeratorT / denominator;
        double u = numeratorU / denominator;

        if (isBetweenZeroAndOne(t) && isBetweenZeroAndOne(u)) {
            return List.of(new Point(
                    p.x() + t * rX,
                    p.y() + t * rY
            ));
        }

        return List.of();
    }

    private List<Point> findCollinearIntersectionPoints(
            Point p,
            Point p2,
            Point q,
            Point q2
    ) {
        List<Point> result = new ArrayList<>();

        if (isPointOnSegment(p, q, q2)) {
            result.add(p);
        }

        if (isPointOnSegment(p2, q, q2)) {
            addIfNotDuplicate(result, p2);
        }

        if (isPointOnSegment(q, p, p2)) {
            addIfNotDuplicate(result, q);
        }

        if (isPointOnSegment(q2, p, p2)) {
            addIfNotDuplicate(result, q2);
        }

        return result;
    }

    private boolean isPointOnSegment(Point point, Point segmentStart, Point segmentEnd) {
        double crossProduct = cross(
                point.x() - segmentStart.x(),
                point.y() - segmentStart.y(),
                segmentEnd.x() - segmentStart.x(),
                segmentEnd.y() - segmentStart.y()
        );

        if (!isZero(crossProduct)) {
            return false;
        }

        return point.x() >= Math.min(segmentStart.x(), segmentEnd.x()) - EPSILON
                && point.x() <= Math.max(segmentStart.x(), segmentEnd.x()) + EPSILON
                && point.y() >= Math.min(segmentStart.y(), segmentEnd.y()) - EPSILON
                && point.y() <= Math.max(segmentStart.y(), segmentEnd.y()) + EPSILON;
    }

    private void addIfNotDuplicate(List<Point> points, Point point) {
        boolean alreadyExists = points.stream()
                .anyMatch(existing -> areSamePoint(existing, point));

        if (!alreadyExists) {
            points.add(point);
        }
    }

    private boolean areSamePoint(Point first, Point second) {
        return areEqual(first.x(), second.x())
                && areEqual(first.y(), second.y());
    }

    private double cross(double firstX, double firstY, double secondX, double secondY) {
        return firstX * secondY - firstY * secondX;
    }

    private boolean isBetweenZeroAndOne(double value) {
        return value >= -EPSILON && value <= 1 + EPSILON;
    }

    private boolean areEqual(double first, double second) {
        return Math.abs(first - second) < EPSILON;
    }

    private boolean isZero(double value) {
        return Math.abs(value) < EPSILON;
    }
}