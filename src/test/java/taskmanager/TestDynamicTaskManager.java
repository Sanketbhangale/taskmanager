package taskmanager;

import com.company.Processable;
import com.company.taskmanager.constants.SortType;
import com.company.TerminationError;
import com.company.taskmanager.impl.DynamicTaskManager;
import com.company.taskmanager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDynamicTaskManager {
    private TaskManager tm;

    class DummyProcess implements Processable {
        private int id;
        private int priority;

        public DummyProcess(int id, int priority) {
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
        public int freeResources() throws TerminationError {
            System.out.println("Did some magic and free resources");
            return 0;
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        tm = new DynamicTaskManager(5);
    }

    @Test
    @DisplayName("Test get list sorting")
    public void testList() throws TerminationError{
        tm = new DynamicTaskManager(5);
        tm.addProcess(new DummyProcess(1, 1));
        tm.addProcess(new DummyProcess(2, 1));
        tm.addProcess(new DummyProcess(3, 2));
        tm.addProcess(new DummyProcess(4, 2));
        tm.addProcess(new DummyProcess(5, 3));


        assertEquals(5, tm.list(SortType.ID).size());
        assertEquals(5, tm.list(SortType.PRIORITY).size());
        ;
        assertEquals(1, tm.list(SortType.PRIORITY).stream().findFirst().get().getPriority());
        assertEquals(3, tm.list(SortType.PRIORITY).get(4).getPriority());
    }

    @Test
    @DisplayName("Adding to queue")
    public void testAdd() throws TerminationError{
        tm.addProcess(new DummyProcess(1, 1));
        tm.addProcess(new DummyProcess(2, 1));
        assertEquals(2, tm.list(SortType.ID).size());
        tm.addProcess(new DummyProcess(3, 2));
        tm.addProcess(new DummyProcess(4, 2));
        tm.addProcess(new DummyProcess(5, 3));
        assertEquals(5, tm.list(SortType.PRIORITY).size());
        tm.addProcess(new DummyProcess(6, 3));
        assertEquals(5, tm.list(SortType.ID).size());

        // The oldest element is removed
        assertEquals(2, tm.list(SortType.ID).stream().findFirst().get().getId());
        assertEquals(6, tm.list(SortType.ID).get(4).getId());

        tm.addProcess(new DummyProcess(7, 3));


        assertEquals(3, tm.list(SortType.ID).stream().findFirst().get().getId());
        assertEquals(7, tm.list(SortType.ID).get(4).getId());

    }

    @Test
    @DisplayName("Killing")
    public void testKill() throws TerminationError {
        tm.addProcess(new DummyProcess(1, 1));
        tm.addProcess(new DummyProcess(2, 1));

        tm.addProcess(new DummyProcess(3, 2));
        tm.addProcess(new DummyProcess(4, 2));
        tm.addProcess(new DummyProcess(5, 3));

        assertEquals(5, tm.list(SortType.PRIORITY).size());
        tm.kill(5);
        assertEquals(4, tm.list(SortType.ID).size());
        assertEquals(2, tm.list(SortType.PRIORITY).get(3).getPriority());
        assertEquals(4, tm.list(SortType.PRIORITY).get(3).getId());
        assertEquals(1, tm.list(SortType.ID).stream().findFirst().get().getId());
        tm.killPriority(2);
        assertEquals(2, tm.list(SortType.PRIORITY).size());
    }
}
