package org.deeper.archery.domain;

import java.util.List;

public abstract class Target {
    public abstract List<Point> intersectionPoints(Arrow arrow);

    public boolean isHitBy(Arrow arrow){
        return !intersectionPoints(arrow).isEmpty();
    }
}
