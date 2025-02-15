package taskscommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TaskTest {
    
    @Test
    public void constructor_validDescription_success() {
        // Test constructor with valid description
        Task task = new Task("Read book");
        assertEquals("Read book", task.getDescription());
        assertEquals(" ", task.getStatusIcon()); // Initially not done
    }

    @Test
    public void markAsDone_initiallyNotDone_success() {
        // Test marking a task as done
        Task task = new Task("Read book");
        task.markAsDone();
        assertEquals("X", task.getStatusIcon());
        
        // Test marking the same task as not done
        task.markAsNotDone();
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void toString_correctFormat() {
        // Test string representation
        Task task = new Task("Read book");
        String expected = "[ ] Read book";
        assertEquals(expected, task.toString());
        
        // Test string representation after marking as done
        task.markAsDone();
        String expectedDone = "[X] Read book";
        assertEquals(expectedDone, task.toString());
    }
}
