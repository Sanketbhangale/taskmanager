package com.company;

public interface Process {
    int getId();
    int getPriority();

    /**
     * The client should implement what it means to kill a process
     * @throws TerminationException
     */
    void freeResources() throws TerminationException;
}
