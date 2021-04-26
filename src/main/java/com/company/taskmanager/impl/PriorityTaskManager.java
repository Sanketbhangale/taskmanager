package com.company.taskmanager.impl;

import com.company.Process;
import com.company.taskmanager.TaskManager;
import com.company.taskmanager.objects.ProcessContainer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class PriorityTaskManager extends TaskManager {


    public PriorityTaskManager(int max) {
        this.queue = new PriorityQueue<>(1, new Comparator<ProcessContainer>() {
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
        this.idMap = new HashMap<>();
        this.priorityMap = new HashMap<>();
        this.maxSize = max;
    }

    @Override
    protected ProcessContainer addProcessToQueue(Process process) {
        ProcessContainer container = new ProcessContainer(process);
        if (!queue.isEmpty() && queue.size() >= this.maxSize && container.getPriority() > queue.peek().getPriority()) {
            queue.remove();
        }
        queue.add(container);
        idMap.put(container.getId(), container);
        return container;
    }



}
