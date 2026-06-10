package org.deeper.archery.api.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record IntersectionRequest(
 @NotNull(message = "Target is required") @Valid SquareTargetRequest target,
 @NotNull(message = "Arrow is required") @Valid ArrowRequest arrow
) {}