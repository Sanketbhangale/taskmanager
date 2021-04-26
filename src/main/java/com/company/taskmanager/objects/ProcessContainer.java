package com.company.taskmanager.objects;

import com.company.Process;

import java.util.Date;

public class ProcessContainer {
    private int id;
    private int priority;
    private Process process;

    private long creationTime;

    //Block access to default constructor
    private ProcessContainer() {
    }

    public Process getProcess() {
        return process;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public ProcessContainer(Process process) {
        this.process = process;
        this.id = process.getId();
        this.priority = process.getPriority();
        this.creationTime = new Date().getTime();
    }

    public int getId() {
        return id;
    }


    public int getPriority() {
        return priority;
    }

    public void kill() throws Exception {
        this.process.freeResources();
    }
}
