package com.company.taskmanager.impl;

import com.company.Processable;
import com.company.TerminationError;
import com.company.taskmanager.TaskManager;
import com.company.taskmanager.objects.ProcessContainer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityTaskManager extends TaskManager {


    public PriorityTaskManager(int maxSize) {
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
        this.idToProcessMap = new HashMap<>();
        this.priorityToProcessMap = new HashMap<>();
        this.maxSize = maxSize;
    }

    @Override
    protected ProcessContainer addProcessToQueue(Processable process) throws TerminationError {
        ProcessContainer container = new ProcessContainer(process);
        if (!queue.isEmpty() && queue.size() >= this.maxSize && container.getPriority() > queue.peek().getPriority()) {
            queue.remove().kill();
        }
        queue.add(container);
        idToProcessMap.put(container.getId(), container);
        return container;
    }



}
