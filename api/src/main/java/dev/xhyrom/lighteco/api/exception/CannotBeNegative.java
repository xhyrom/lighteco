package dev.xhyrom.lighteco.api.exception;

public class CannotBeNegative extends IllegalArgumentException {
    public CannotBeNegative(String message) {
        super(message);
    }
}
