package org.deeper.archery.api.model;

import jakarta.validation.constraints.NotNull;

public record Point(
  @NotNull(message = "X coordinate is required") Double x,
  @NotNull(message = "Y coordinate is required") Double y
) {}