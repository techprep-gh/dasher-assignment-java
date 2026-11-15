package com.techprep.dasher.errors;

public class UnknownDispatchNodeException extends AssignmentException {
    public UnknownDispatchNodeException(String node) {
        super("dispatch node " + node + " is not registered");
    }
}
