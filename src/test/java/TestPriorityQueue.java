import com.company.PriorityTaskManager;
import com.company.Process;
import com.company.SortType;
import com.company.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPriorityQueue {
    private TaskManager tm;
    class DummyProcess implements Process{
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
        public int freeResources() throws Exception {
            System.out.println("Did some magic and free resources");
            return 0;
        }
    }
    @BeforeEach
    public void setUp() throws Exception {
        tm = new PriorityTaskManager(5);
    }

    @Test
    @DisplayName("Test get list sorting")
    public void testList() {
        tm.addProcess(new DummyProcess(1,1));
        tm.addProcess(new DummyProcess(2,1));
        tm.addProcess(new DummyProcess(3,2));
        tm.addProcess(new DummyProcess(4,2));
        tm.addProcess(new DummyProcess(5,3));


        assertEquals(5, tm.list(SortType.ID).size());
        assertEquals(5, tm.list(SortType.PRIORITY).size());;
        assertEquals(1, tm.list(SortType.PRIORITY).stream().findFirst().get().getPriority());
        assertEquals(3, tm.list(SortType.PRIORITY).get(4).getPriority());
    }

    @Test
    @DisplayName("Add and kill")
    public void testAddKill() {
        tm.addProcess(new DummyProcess(1,1));
        tm.addProcess(new DummyProcess(2,1));



        assertEquals(2, tm.list(SortType.ID).size());

        tm.addProcess(new DummyProcess(3,2));
        tm.addProcess(new DummyProcess(4,2));
        tm.addProcess(new DummyProcess(5,3));

        assertEquals(5, tm.list(SortType.PRIORITY).size());

        tm.addProcess(new DummyProcess(6,3));
        assertEquals(5, tm.list(SortType.ID).size());
        // When sorted by id the first element should be with id 2.
        // Since the recent addition removed (id:1, priority: 1)
        assertEquals(2, tm.list(SortType.ID).stream().findFirst().get().getId());

    }
}
