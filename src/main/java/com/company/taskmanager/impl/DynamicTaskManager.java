package com.company.taskmanager.impl;

import com.company.Processable;
import com.company.TerminationError;
import com.company.taskmanager.TaskManager;
import com.company.taskmanager.objects.ProcessContainer;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * A Dynamic task manager allows you to add a process by removing the oldest element
 * in the queue and killing it.
 *
 * It uses a normal queue for FIFO behavior.
 * An element cannot be added if removal or termination of process fails
 */
public class DynamicTaskManager extends TaskManager {

    public DynamicTaskManager(int maxSize){
        this.queue = new ArrayBlockingQueue<ProcessContainer>(maxSize);
        this.idToProcessMap = new HashMap<>();
        this.priorityToProcessMap = new HashMap<>();
        this.maxSize = maxSize;
    }


    @Override
    protected ProcessContainer addProcessToQueue(Processable process) throws TerminationError {
        ProcessContainer container = new ProcessContainer(process);
        if (!queue.isEmpty() && queue.size() >= this.maxSize && container.getPriority() > queue.peek().getPriority()) {
                queue.peek().kill();
                queue.remove();
        }
        queue.add(container);
        return container;
    }
}
