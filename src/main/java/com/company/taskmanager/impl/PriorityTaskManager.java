package com.company.taskmanager.impl;

import com.company.Process;
import com.company.TerminationException;
import com.company.taskmanager.AbstractTaskManagerImpl;
import com.company.taskmanager.containers.ProcessContainer;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Priority Task manager let's you add a proccess if the new process has a higher priority
 * than the lowest priority element. If there are more than one element with same priority the
 * oldest one is removed
 */
public class PriorityTaskManager extends AbstractTaskManagerImpl {

    /**
     * The Priority queue uses Priority, Creation time, id in descending order of
     *  importance to sort the queue.
     *  Insertion operation on the queue is O(logN)
     *  idToProcessMap works with worst case of O(N) on all it's operations
     */

    public PriorityTaskManager(int maxSize) {
        super(maxSize);
        this.queue = new PriorityBlockingQueue<>(1, new Comparator<ProcessContainer>() {
            @Override
            public int compare(ProcessContainer o1, ProcessContainer o2) {
                if (o1.getPriority() == o2.getPriority()) {
                    if ((o1.getCreationTime() == o2.getCreationTime())) {
                        return o1.getId() - o2.getId();

                    }
                    return (int) (o1.getCreationTime() - o2.getCreationTime());
                }
                return o1.getPriority() - o2.getPriority();
            }
        });
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
        idToProcessMap.put(container.getId(), container);
        return container;
    }



}
