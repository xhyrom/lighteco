package dev.xhyrom.lighteco.api.event.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents the position (index) of a parameter within an event.
 * Used for information purposes only.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Position {
    /**
     * Represents the position (index) of a parameter.
     *
     * @return the position (index)
     */
    int value();
}
