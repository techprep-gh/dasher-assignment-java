package com.techprep.dasher.errors;

public class NoHealthyDispatchNodeException extends AssignmentException {
    public NoHealthyDispatchNodeException() {
        super("no healthy dispatch node");
    }
}
