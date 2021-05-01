package com.company.taskmanager.impl;

import com.company.Process;
import com.company.taskmanager.AbstractTaskManagerImpl;
import com.company.taskmanager.containers.ProcessContainer;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Stubborn task manager doesn't allow you to add more process if it is full
 */
public class StubbornTaskManager extends AbstractTaskManagerImpl {

    public StubbornTaskManager(int maxSize){
        super(maxSize);
        this.queue = new ArrayBlockingQueue<>(maxSize);

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
