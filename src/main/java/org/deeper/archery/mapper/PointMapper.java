package org.deeper.archery.mapper;

import org.deeper.archery.domain.Point;
import org.springframework.stereotype.Component;

@Component
public class PointMapper {
    public Point map(org.deeper.archery.api.model.Point request) {
        return new Point(
                request.x(),
                request.y()
        );
    }

    public org.deeper.archery.api.model.Point map(Point request) {
        return new org.deeper.archery.api.model.Point(
                request.x(),
                request.y()
        );
    }
}
