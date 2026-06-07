package org.deeper.crackerarchery.api.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.deeper.crackerarchery.api.model.Point;

public record CrackerRequest (
  @Valid @NotNull Point topLeft,
  @Valid @NotNull Point topRight,
  @Valid @NotNull Point bottomLeft,
  @Valid @NotNull Point bottomRight
){}