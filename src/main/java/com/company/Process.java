package com.company;

public interface Process {
    int getId();
    int getPriority();
    void freeResources() throws TerminationException;
}
