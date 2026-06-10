package org.deeper.archery.api.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.deeper.archery.api.model.Point;

import java.util.List;

public record SquareTargetRequest(
       @NotNull(message = "Square target coordinate points are required")
       @Valid
       @Size(
               min = 4,
               max = 4,
               message = "Square target must contain exactly 4 points"
       )
       List<Point> points
){}