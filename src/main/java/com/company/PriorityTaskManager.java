package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class PriorityTaskManager implements TaskManager {
    public PriorityQueue<ProcessContainer> priorityQueue;
    public HashMap<Integer, ProcessContainer> idMap;
    public HashMap<Integer, LinkedList<ProcessContainer>> priorityMap;
    public int maxSize;

    public PriorityTaskManager(int max) {
        this.priorityQueue = new PriorityQueue<>(1, new Comparator<ProcessContainer>() {
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
    public void addProcess(Process process) {
        ProcessContainer container = new ProcessContainer(process);
        if (!priorityQueue.isEmpty() && container.getPriority() > priorityQueue.peek().getPriority()) {
            if (priorityQueue.size() >= this.maxSize) {
                priorityQueue.remove();
            }
        }
        priorityQueue.add(container);
        idMap.put(container.getId(), container);
        LinkedList<ProcessContainer> list = priorityMap.get(container.getPriority());
        if (list == null) {
            list = new LinkedList<ProcessContainer>();
            priorityMap.put(container.getPriority(), list);
        }
        list.add(container);

    }

    @Override
    public void kill(int id) throws TerminationError {
        ProcessContainer container = idMap.get(id);
        try {
            container.kill();
            idMap.remove(id); // Worst case O(N)
            priorityQueue.remove(container); // O(log N)
            priorityMap.get(container.getPriority()).remove(container); // Retrieval from this hash is O(1) due to constant size. Removal from the list is O(N). Total O(N)
        } catch (Exception e) {
            throw new TerminationError("Couldn't kill the process");
        }
    }

    @Override
    public void killPriority(int priority) throws TerminationError {
        LinkedList<ProcessContainer> list = priorityMap.get(priority);
        LinkedList<ProcessContainer> residue = new LinkedList<>();
        list.forEach(container -> {
            try {
                idMap.remove(container.getId());
                container.kill();
            } catch (Exception e) {
                residue.add(container);
            }
        });
        priorityMap.remove(priority);
        if (!residue.isEmpty()) {
            priorityMap.put(priority, residue);
            throw new TerminationError("Not all processes could be killed");
        }
    }

    @Override
    public List<Process> list(SortType st) {

        if (st == SortType.ID) { // Id based
            return priorityQueue.stream().sorted(new Comparator<ProcessContainer>() {
                @Override
                public int compare(ProcessContainer o1, ProcessContainer o2) {
                    return o1.getId() - o1.getId();
                }
            }).map(processContainer -> processContainer.getProcess()).collect(Collectors.toList());
        } else {
            return priorityQueue.stream().sorted(new Comparator<ProcessContainer>() {
                @Override
                public int compare(ProcessContainer o1, ProcessContainer o2) {
                    return o1.getPriority() - o1.getPriority();
                }
            }).map(processContainer -> processContainer.getProcess()).collect(Collectors.toList());
        }
    }
}
