package taskscommand;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    //DeadLine
    protected LocalDateTime by;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    /**
     * Constructs a new Deadline task.
     * @param description The task description.
     * @param by The deadline date and time in d/M/yyyy HHmm format.
     * @throws DateTimeParseException if the date format is invalid.
     */
    public Deadline(String description, String by) throws DateTimeParseException {
        super(description);
        this.by = LocalDateTime.parse(by, INPUT_FORMATTER);
    }

    /**
     * Returns the deadline date and time.
     * @return The deadline date and time.
     */
    public LocalDateTime getDeadline() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMATTER) + ")";
    }
}
