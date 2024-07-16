package dev.xhyrom.lighteco.common.command.exception;

import java.util.UUID;

public class LockedUserException extends Exception {
    public LockedUserException(UUID uniqueId) {
        super("User with uuid " + uniqueId + " is currently locked thus cannot be modified");
    }
}
