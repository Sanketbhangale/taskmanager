package com.company.taskmanager;

import com.company.Process;
import com.company.TaskManager;
import com.company.TerminationException;
import com.company.taskmanager.constants.SortType;
import com.company.taskmanager.containers.ProcessContainer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public abstract class AbstractTaskManagerImpl implements TaskManager {
    /**
     * The implementation uses three data structures
     * idToProcessMap: Store mapping between id and Process for id retrieval. This is to reduce
     *                  the lookup time for id
     * priorityToProcessMap: Map to reduce the priority based lookup
     *
     * The argument where a linear search can be better than a hashmap is still debatable.
     * In theory hashmap can be as worse as O(N). But it can also be better which linear search cannot.
     */
    protected Queue<ProcessContainer> queue;
    protected int maxSize;
    protected Map<Integer, ProcessContainer> idToProcessMap;
    protected Map<Integer, LinkedList<ProcessContainer>> priorityToProcessMap;
    private final ReadWriteLock writeLock = new ReentrantReadWriteLock();

    public AbstractTaskManagerImpl(int maxSize) {
        this.idToProcessMap = new ConcurrentHashMap<>();
        this.priorityToProcessMap = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
    }

    /**
     * Kills the process with an id.
     * The locks are used to make it thread safe so that other writes are blocked
     * @param id
     * @throws TerminationException
     */
    @Override
    public void kill(int id) throws TerminationException {
        ProcessContainer container = idToProcessMap.get(id);
        writeLock.writeLock().lock();
        try {
            container.kill();
            idToProcessMap.remove(id); // Worst case O(N)
            queue.remove(container); // O(log N)
            priorityToProcessMap.get(container.getPriority()).remove(container); // Retrieval from this hash is O(1) due to constant size. Removal from the list is O(N). Total O(N)
        } catch (TerminationException e) {
            writeLock.writeLock().unlock();
            throw e;
        }
        writeLock.writeLock().unlock();
    }
    /**
     * Kills the processes with priority.
     * The locks are used to make it thread safe so that other writes are blocked
     * @param priority
     * @throws TerminationException
     */

    @Override
    public void killPriority(int priority) throws TerminationException {
        LinkedList<ProcessContainer> list = priorityToProcessMap.get(priority);
        LinkedList<ProcessContainer> residue = new LinkedList<>();
        writeLock.writeLock().lock();
        try {
            list.forEach(container -> {
                try {
                    queue.remove(container);
                    idToProcessMap.remove(container.getId());
                    container.kill();
                } catch (Exception | TerminationException e) {
                    residue.add(container);
                }
            });
            priorityToProcessMap.remove(priority);
            if (!residue.isEmpty()) {
                priorityToProcessMap.put(priority, residue);
                throw new TerminationException("Not all processes could be killed");
            }
        } catch (TerminationException e) {
            writeLock.writeLock().unlock();
            throw e;
        }
        writeLock.writeLock().unlock();
    }

    protected abstract ProcessContainer addProcessToQueue(Process process) throws TerminationException;

    /**
     * These are common procedures to do while adding a process.
     * The behavior specific code is delegated to #addProcessToQueue for the
     * implementor classes.
     * @param process
     * @throws TerminationException
     */
    @Override
    public void addProcess(Process process) throws TerminationException {
        writeLock.writeLock().lock();
        try {
            ProcessContainer container = addProcessToQueue(process);
            if (container != null) {
                idToProcessMap.put(container.getId(), container);
                LinkedList<ProcessContainer> list = priorityToProcessMap.get(container.getPriority());
                if (list == null) {
                    list = new LinkedList<ProcessContainer>();
                    priorityToProcessMap.put(container.getPriority(), list);
                }
                list.add(container);
            }
        } catch (TerminationException e) {
            writeLock.writeLock().unlock();
            throw e;
        }

        writeLock.writeLock().unlock();

    }

    @Override
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
