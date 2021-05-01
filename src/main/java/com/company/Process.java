package com.company;

public interface Process {
    int getId();
    int getPriority();
    int freeResources() throws TerminationException;
}
