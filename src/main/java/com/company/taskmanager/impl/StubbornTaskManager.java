package com.company.taskmanager.impl;

import com.company.Processable;
import com.company.taskmanager.TaskManager;
import com.company.taskmanager.objects.ProcessContainer;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class StubbornTaskManager extends TaskManager {

    public StubbornTaskManager(int maxSize){
        this.queue = new ArrayBlockingQueue<>(maxSize);
        this.idToProcessMap = new HashMap<>();
        this.priorityToProcessMap = new HashMap<>();
        this.maxSize = maxSize;
    }


    @Override
    protected ProcessContainer addProcessToQueue(Processable process) {
        ProcessContainer container = new ProcessContainer(process);
        if (queue.size() >= this.maxSize) {
            return null;
        }
        queue.add(container);
        return container;
    }
}
