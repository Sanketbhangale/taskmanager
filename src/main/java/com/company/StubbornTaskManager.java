package com.company;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class StubbornTaskManager extends TaskManager{

    public StubbornTaskManager(int max){
        this.queue = new ArrayBlockingQueue<ProcessContainer>(1);
        this.idMap = new HashMap<>();
        this.priorityMap = new HashMap<>();
        this.maxSize = max;
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
