package com.company;

public interface TaskManager {
    public void addProcess(Process p);
    public void kill(int id) throws TerminationError;
    public void killPriority(int priority) throws TerminationError;
}
