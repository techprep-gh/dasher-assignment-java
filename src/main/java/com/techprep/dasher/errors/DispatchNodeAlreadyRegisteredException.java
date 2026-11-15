package com.techprep.dasher.errors;

public class DispatchNodeAlreadyRegisteredException extends AssignmentException {
    public DispatchNodeAlreadyRegisteredException(String node) {
        super("dispatch node " + node + " is already registered");
    }
}
