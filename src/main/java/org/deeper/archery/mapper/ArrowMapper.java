package org.deeper.archery.mapper;

import lombok.RequiredArgsConstructor;
import org.deeper.archery.api.model.request.ArrowRequest;
import org.deeper.archery.domain.Arrow;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArrowMapper {
    private final PointMapper pointMapper;

    public Arrow map(ArrowRequest request) {
        return new Arrow(
                pointMapper.map(request.start()),
                pointMapper.map(request.end())
        );
    }

}
