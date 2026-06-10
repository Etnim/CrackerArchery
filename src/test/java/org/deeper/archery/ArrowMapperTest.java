package org.deeper.archery;

import org.deeper.archery.api.model.request.ArrowRequest;
import org.deeper.archery.domain.Arrow;
import org.deeper.archery.mapper.ArrowMapper;
import org.deeper.archery.mapper.PointMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ArrowMapperTest {
    private ArrowMapper arrowMapper;

    @BeforeEach
    void setUp() {
        arrowMapper = new ArrowMapper(new PointMapper());
    }

    @Test
    void map_shouldMapArrowRequestToDomainArrow() {
        ArrowRequest request = new ArrowRequest(
                new org.deeper.archery.api.model.Point(1.0, 2.0),
                new org.deeper.archery.api.model.Point(3.0, 4.0)
        );

        Arrow result = arrowMapper.map(request);

        assertThat(result.getStart().x()).isEqualTo(1.0);
        assertThat(result.getStart().y()).isEqualTo(2.0);
        assertThat(result.getEnd().x()).isEqualTo(3.0);
        assertThat(result.getEnd().y()).isEqualTo(4.0);
    }
}