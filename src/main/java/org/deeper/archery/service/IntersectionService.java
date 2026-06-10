package org.deeper.archery.service;

import lombok.AllArgsConstructor;
import org.deeper.archery.domain.Point;
import org.deeper.archery.api.model.request.IntersectionRequest;
import org.deeper.archery.api.model.response.IntersectionResponse;
import org.deeper.archery.domain.Arrow;
import org.deeper.archery.domain.Target;
import org.deeper.archery.mapper.ArrowMapper;
import org.deeper.archery.mapper.PointMapper;
import org.deeper.archery.mapper.TargetMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class IntersectionService {
    private final ArrowMapper arrowMapper;
    private final TargetMapper targetMapper;
    private final PointMapper pointMapper;

    public IntersectionResponse intersection(IntersectionRequest request) {
        Target target = targetMapper.map(request.target());
        Arrow arrow = arrowMapper.map(request.arrow());

        List<Point> intersectionPoints = target.intersectionPoints(arrow);

        return new IntersectionResponse(
                intersectionPoints.stream()
                        .map(pointMapper::map)
                        .toList());
    }
}