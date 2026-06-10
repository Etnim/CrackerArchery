package org.deeper.archery.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deeper.archery.api.model.request.IntersectionRequest;
import org.deeper.archery.api.model.response.IntersectionResponse;
import org.deeper.archery.api.model.response.RequestCountResponse;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Intersection API", description = "Operations for checking target and arrow interactions")
@RequestMapping("/archery")
public interface IntersectionApi {

    /**
     * Checks how a given arrow (line) interacts with a target target (square).
     *
     * @param request contains the coordinates of the square and the line
     * @return list of intersection points
     */
    @Operation(
            summary = "Check target-arrow interaction",
            description = """
                    Returns list of points where an arrow (line segment)
                    intersects with a target (square) based on provided coordinates.
                    If they don't interact empty list is returned
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Intersection check successful",
                    content = @Content(schema = @Schema(implementation = IntersectionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid coordinates provided",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/intersection/check")
    IntersectionResponse checkIntersection(IntersectionRequest request);


    /**
     * Returns information about how many requests are currently being processed by the application.
     *
     * @return total request count
     */
    @Operation(
            summary = "Get processed request count",
            description = "Returns the number of intersection requests processed by the application since startup. Requires a valid JWT token."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Request count retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RequestCountResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - missing or invalid JWT token",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/stats/requests")
    RequestCountResponse getRequestCount();
}
