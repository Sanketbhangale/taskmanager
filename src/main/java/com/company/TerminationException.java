package com.company;

public class TerminationException extends Throwable {
    public TerminationException(String message) {
        System.out.println(message);
    }
}
