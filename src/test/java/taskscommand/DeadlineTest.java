package taskscommand;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlineTest {

    @Test
    public void constructor_invalidDateTimeFormat_throwsDateTimeParseException() {
        // Test invalid date-time format
        assertThrows(DateTimeParseException.class, () -> {
            new Deadline("Return book", "2-12-2023 18:00"); // Wrong format
        });
    }

    @Test
    public void toString_correctFormat() {
        // Test string representation
        Deadline deadline = new Deadline("Return book", "2/12/2023 1800");
        String expected = "[D][ ] Return book (by: Dec 2 2023, 6:00pm)";
        assertEquals(expected, deadline.toString());
    }
}
