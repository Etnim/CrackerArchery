package org.deeper.crackerarchery.api.model.response;

import org.deeper.crackerarchery.api.model.Point;

import java.util.List;

public record IntersectionResponse(
  List<Point> intersectionPoints
) {}