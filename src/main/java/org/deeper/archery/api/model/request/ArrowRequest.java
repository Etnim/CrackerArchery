package org.deeper.archery.api.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.deeper.archery.api.model.Point;

public record ArrowRequest(
 @NotNull(message = "Start point coordinate is required") @Valid Point start,
 @NotNull(message = "End point coordinate is required") @Valid Point end
) {}