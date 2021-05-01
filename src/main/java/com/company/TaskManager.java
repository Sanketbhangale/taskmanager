package com.company;

import com.company.taskmanager.constants.SortType;

import java.util.List;

public interface TaskManager {
    void kill(int id) throws TerminationException;

    void killPriority(int priority) throws TerminationException;

    void addProcess(Process process) throws TerminationException;

    List<Process> list(SortType st);
}
