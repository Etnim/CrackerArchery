package org.deeper.archery.domain;

import lombok.Getter;
import org.deeper.archery.validator.ArrowValidator;

@Getter
public class Arrow {
    private final Point start;
    private final Point end;
    private final ArrowValidator validator = new ArrowValidator();

    public Arrow(Point start, Point end) {
        this.start = start;
        this.end = end;
        validator.validate(this);
    }
}