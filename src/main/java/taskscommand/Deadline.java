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
        assert by != null && !by.trim().isEmpty() : "Deadline date/time cannot be null or empty";
        this.by = LocalDateTime.parse(by, INPUT_FORMATTER);
        assert this.by != null : "Deadline date/time parsing failed";
    }

    /**
     * Returns the deadline date and time.
     * @return The deadline date and time in d/M/yyyy HHmm format.
     */
    public String getDeadline() {
        assert by != null : "Deadline date/time should never be null";
        return by.format(INPUT_FORMATTER);
    }

    @Override
    public String toString() {
        assert by != null : "Deadline date/time should never be null";
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMATTER) + ")";
    }
}
