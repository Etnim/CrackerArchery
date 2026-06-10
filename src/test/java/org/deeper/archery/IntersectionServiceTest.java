package org.deeper.archery;

import org.deeper.archery.api.model.Point;
import org.deeper.archery.api.model.request.ArrowRequest;
import org.deeper.archery.api.model.request.IntersectionRequest;
import org.deeper.archery.api.model.request.SquareTargetRequest;
import org.deeper.archery.api.model.response.IntersectionResponse;
import org.deeper.archery.mapper.ArrowMapper;
import org.deeper.archery.mapper.PointMapper;
import org.deeper.archery.mapper.TargetMapper;
import org.deeper.archery.service.IntersectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntersectionServiceTest {
    private IntersectionService intersectionService;
    PointMapper pointMapper = new PointMapper();

    @BeforeEach
    void setUp() {
        intersectionService = new IntersectionService(
                new ArrowMapper(pointMapper),
                new TargetMapper(pointMapper),
                pointMapper
        );
    }

    @ParameterizedTest
    @MethodSource("intersectionCases")
    void intersection_shouldReturnExpectedPoints(
            String caseName,
            ArrowRequest arrowRequest,
            List<Point> expectedPoints
    ) {
        IntersectionRequest request = new IntersectionRequest(
                squareTargetRequest(),
                arrowRequest
        );

        IntersectionResponse response = intersectionService.intersection(request);

        assertEquals(response.intersectionPoints(), expectedPoints);
    }

    private static Stream<Arguments> intersectionCases() {
        return Stream.of(
                Arguments.of(
                        "arrow crosses square horizontally",
                        new ArrowRequest(
                                new Point(-1.0, 0.5),
                                new Point(2.0, 0.5)
                        ),
                        List.of(
                                new Point(1.0, 0.5),
                                new Point(0.0, 0.5)
                        )
                ),
                Arguments.of(
                        "arrow does not touch square",
                        new ArrowRequest(
                                new Point(-1.0, 2.0),
                                new Point(2.0, 2.0)
                        ),
                        List.of()
                ),
                Arguments.of(
                        "arrow touches square corner",
                        new ArrowRequest(
                                new Point(-1.0, 2.0),
                                new Point(0.0, 1.0)
                        ),
                        List.of(
                                new Point(0.0, 1.0)
                        )
                ),
                Arguments.of(
                        "arrow starts inside square and exits",
                        new ArrowRequest(
                                new Point(0.5, 0.5),
                                new Point(2.0, 0.5)
                        ),
                        List.of(
                                new Point(1.0, 0.5)
                        )
                ),
                Arguments.of(
                        "arrow is fully inside square",
                        new ArrowRequest(
                                new Point(0.25, 0.5),
                                new Point(0.75, 0.5)
                        ),
                        List.of()
                ),
                Arguments.of(
                        "arrow overlaps square side",
                        new ArrowRequest(
                                new Point(-0.5, 1.0),
                                new Point(0.5, 1.0)
                        ),
                        List.of(
                                new Point(0.5, 1.0),
                                new Point(0.0, 1.0)
                        )
                )
        );
    }

    private static SquareTargetRequest squareTargetRequest() {
        return new SquareTargetRequest(
                List.of(
                        new Point(0.0, 1.0),
                        new Point(1.0, 1.0),
                        new Point(1.0, 0.0),
                        new Point(0.0, 0.0)
                )
        );
    }
}
