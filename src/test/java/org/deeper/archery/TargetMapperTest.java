package org.deeper.archery;

import org.deeper.archery.api.model.request.SquareTargetRequest;
import org.deeper.archery.domain.SquareTarget;
import org.deeper.archery.mapper.PointMapper;
import org.deeper.archery.mapper.TargetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TargetMapperTest {
    private TargetMapper targetMapper;

    @BeforeEach
    void setUp() {
        targetMapper = new TargetMapper(new PointMapper());
    }

    @Test
    void map_shouldMapSquareTargetRequestToSquareTarget() {
        SquareTargetRequest request = new SquareTargetRequest(
                List.of(
                        new org.deeper.archery.api.model.Point(0.0, 1.0),
                        new org.deeper.archery.api.model.Point(1.0, 1.0),
                        new org.deeper.archery.api.model.Point(1.0, 0.0),
                        new org.deeper.archery.api.model.Point(0.0, 0.0)
                )
        );

        SquareTarget result = targetMapper.map(request);

        assertThat(result.getA().x()).isEqualTo(0.0);
        assertThat(result.getA().y()).isEqualTo(1.0);

        assertThat(result.getB().x()).isEqualTo(1.0);
        assertThat(result.getB().y()).isEqualTo(1.0);

        assertThat(result.getC().x()).isEqualTo(1.0);
        assertThat(result.getC().y()).isEqualTo(0.0);

        assertThat(result.getD().x()).isEqualTo(0.0);
        assertThat(result.getD().y()).isEqualTo(0.0);
    }
}