package com.company;

public interface Process {
    public int getId();
    public int getPriority();
    public int freeResources() throws Exception;
}
