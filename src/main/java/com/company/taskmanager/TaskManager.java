package com.company.taskmanager;

import com.company.Process;
import com.company.TerminationError;
import com.company.taskmanager.constants.SortType;
import com.company.taskmanager.objects.ProcessContainer;

import java.util.*;
import java.util.stream.Collectors;

public abstract class TaskManager {
    public Queue<ProcessContainer> queue;
    public int maxSize;
    public Map<Integer, ProcessContainer> idMap;
    public Map<Integer, LinkedList<ProcessContainer>> priorityMap;

    public synchronized void kill(int id) throws TerminationError {
        ProcessContainer container = idMap.get(id);
        try {
            container.kill();
            idMap.remove(id); // Worst case O(N)
            queue.remove(container); // O(log N)
            priorityMap.get(container.getPriority()).remove(container); // Retrieval from this hash is O(1) due to constant size. Removal from the list is O(N). Total O(N)
        } catch (Exception e) {
            throw new TerminationError("Couldn't kill the process");
        }
    }

    public synchronized void killPriority(int priority) throws TerminationError {
        LinkedList<ProcessContainer> list = priorityMap.get(priority);
        LinkedList<ProcessContainer> residue = new LinkedList<>();
        list.forEach(container -> {
            try {
                queue.remove(container);
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

    protected abstract ProcessContainer addProcessToQueue(Process process);

    public synchronized void addProcess(Process process) {
        ProcessContainer container = addProcessToQueue(process);
        if (container != null) {
            idMap.put(container.getId(), container);
            LinkedList<ProcessContainer> list = priorityMap.get(container.getPriority());
            if (list == null) {
                list = new LinkedList<ProcessContainer>();
                priorityMap.put(container.getPriority(), list);
            }
            list.add(container);
        }
    }

    public List<Process> list(SortType st) {

        if (st == SortType.ID) { // Id based
            return queue.stream().sorted(new Comparator<ProcessContainer>() {
                @Override
                public int compare(ProcessContainer o1, ProcessContainer o2) {
                    return o1.getId() - o1.getId();
                }
            }).map(processContainer -> processContainer.getProcess()).collect(Collectors.toList());
        } else {
            return queue.stream().sorted(new Comparator<ProcessContainer>() {
                @Override
                public int compare(ProcessContainer o1, ProcessContainer o2) {
                    return o1.getPriority() - o1.getPriority();
                }
            }).map(processContainer -> processContainer.getProcess()).collect(Collectors.toList());
        }
    }
}
