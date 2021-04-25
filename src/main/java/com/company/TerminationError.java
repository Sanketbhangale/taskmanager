package com.company;

public class TerminationError extends Throwable {
    public TerminationError(String message) {
        System.out.println(message);
    }
}
