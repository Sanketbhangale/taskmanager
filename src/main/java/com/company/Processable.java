package com.company;

public interface Processable {
    int getId();
    int getPriority();
    int freeResources() throws TerminationError;
}
