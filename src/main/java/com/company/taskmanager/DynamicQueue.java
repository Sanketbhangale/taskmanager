package com.company.taskmanager;

import com.company.Process;
import com.company.taskmanager.objects.ProcessContainer;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class DynamicQueue extends TaskManager {

    public DynamicQueue(int max){
        this.queue = new ArrayBlockingQueue<ProcessContainer>(1);
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
        return container;
    }
}
