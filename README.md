# Taskmanager

The task manager consists of a task manager that manages clients processes.
A ``process`` is left to the client to be defined using an interface. This component exposes interfaces for the client to utilize and define the behavior of the process and access the TaskManager.
Kindly read the comments in the code for implementation details.


# Usage of the component

To use the task manager accepts a process object that implements the ``Process`` interface.

        class MySystemProcess implements Process {
        private int id;
        private int priority;

                public MySystemProcess(int id, int priority) {
                    this.id = id;
                    this.priority = priority;
                }

                @Override
                public int getId() {
                    return id;
                }

                @Override
                public int getPriority() {
                    return priority;
                }

                @Override
                public void freeResources() throws TerminationError {
                    //Implement logic to free resources. This will be called 
                    // when a kill on this process is executed
                }
    }


You can instantiate a TaskManager interface using below three subclasses

``DynamicTaskManager``

``StubbornTaskManager``

``PriorityTaskManager``

Example:


    TaskManager tm = new PriorityTaskManager(5);
    tm.addProcess(new MySystemProcess(1,1));


# Run tests

 ``mvn clean install``
 
 ``mvn test`` 
