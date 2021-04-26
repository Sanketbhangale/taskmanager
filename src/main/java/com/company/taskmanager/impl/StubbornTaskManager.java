package com.company.taskmanager.impl;

import com.company.Process;
import com.company.taskmanager.TaskManager;
import com.company.taskmanager.objects.ProcessContainer;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class StubbornTaskManager extends TaskManager {

    public StubbornTaskManager(int maxSize){
        this.queue = new ArrayBlockingQueue<ProcessContainer>(maxSize);
        this.idMap = new HashMap<>();
        this.priorityMap = new HashMap<>();
        this.maxSize = maxSize;
    }


    @Override
    protected ProcessContainer addProcessToQueue(Process process) {
        ProcessContainer container = new ProcessContainer(process);
        if (queue.size() >= this.maxSize) {
            return null;
        }
        queue.add(container);
        return container;
    }
}
