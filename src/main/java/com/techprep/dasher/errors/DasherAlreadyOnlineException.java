package com.techprep.dasher.errors;

public class DasherAlreadyOnlineException extends AssignmentException {
    public DasherAlreadyOnlineException(String dasherId) {
        super("dasher " + dasherId + " is already online");
    }
}
