import com.company.PriorityTaskManager;
import com.company.Process;
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
        tm = new PriorityTaskManager(2);
    }

    @Test
    @DisplayName("Simple multiplication should work")
    public void testAdd() {
        tm.addProcess(new DummyProcess(1,1));
        tm.addProcess(new DummyProcess(2,1));


    }
}
