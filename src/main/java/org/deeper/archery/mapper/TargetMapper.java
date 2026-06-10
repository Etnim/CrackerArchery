package org.deeper.archery.mapper;

import lombok.RequiredArgsConstructor;
import org.deeper.archery.api.model.request.SquareTargetRequest;
import org.deeper.archery.domain.SquareTarget;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TargetMapper {
    private final PointMapper pointMapper;

    public SquareTarget map(SquareTargetRequest request) {
        return new SquareTarget(
                pointMapper.map(request.points().get(0)),
                pointMapper.map(request.points().get(1)),
                pointMapper.map(request.points().get(2)),
                pointMapper.map(request.points().get(3))
        );
    }

}
