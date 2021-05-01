package com.company.taskmanager.impl;

import com.company.Process;
import com.company.TerminationException;
import com.company.taskmanager.AbstractTaskManagerImpl;
import com.company.taskmanager.containers.ProcessContainer;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * A Dynamic task manager allows you to add a process by removing the oldest element
 * in the queue and killing it.
 *
 * It uses a normal queue for FIFO behavior.
 * An element cannot be added if removal or termination of process fails
 */
public class DynamicTaskManager extends AbstractTaskManagerImpl {

    /**
     * The Dynamic task manager uses an Array Queue. The removal and addition
     * in the Queue is O(1)
     * As for other maps it is O(N) worst case.
     * @param maxSize
     */
    public DynamicTaskManager(int maxSize){
        super(maxSize);
        this.queue = new ArrayBlockingQueue<ProcessContainer>(maxSize);
    }


    /**
     * Note that this protected and cannot be accessed outside.
     * This is always called in the thread safe block in the addProcess method in super class
     * @param process
     * @return
     * @throws TerminationException
     */
    @Override
    protected ProcessContainer addProcessToQueue(Process process) throws TerminationException {
        ProcessContainer container = new ProcessContainer(process);
        if (!queue.isEmpty() && queue.size() >= this.maxSize && container.getPriority() > queue.peek().getPriority()) {
                queue.peek().kill();
                queue.remove();
        }
        queue.add(container);
        return container;
    }
}
