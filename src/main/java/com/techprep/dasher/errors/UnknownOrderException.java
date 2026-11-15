package com.techprep.dasher.errors;

public class UnknownOrderException extends AssignmentException {
    public UnknownOrderException(String orderId) {
        super("order " + orderId + " has no active assignment");
    }
}
