package com.techprep.dasher.errors;

public class InvalidAvailabilityStateException extends AssignmentException {
    public InvalidAvailabilityStateException(String state) {
        super("unknown availability state '" + state + "'");
    }
}
