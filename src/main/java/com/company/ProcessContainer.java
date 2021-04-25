package com.company;

public class ProcessContainer {
    public int id;
    public int priority;
    public Process process;
    //Block access to default constructor
    private ProcessContainer(){}

    public ProcessContainer(Process process){
        this.process = process;
        this.id = process.getId();
        this.priority = process.getPriority();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public void kill() throws Exception{
        this.process.freeResources();
    }
}
