package com.techprep.dasher.errors;

public class UnknownDasherException extends AssignmentException {
    public UnknownDasherException(String dasherId) {
        super("dasher " + dasherId + " is not online");
    }
}
