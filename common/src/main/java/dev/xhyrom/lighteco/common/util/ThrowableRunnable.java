package dev.xhyrom.lighteco.common.util;

@FunctionalInterface
public interface ThrowableRunnable {
    void run() throws Exception;
}

