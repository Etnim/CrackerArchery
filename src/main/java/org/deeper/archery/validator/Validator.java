package org.deeper.archery.validator;

public interface Validator<T> {
    void validate(T value);
}