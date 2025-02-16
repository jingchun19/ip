package taskscommand;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;
    private static final DateTimeFormatter INPUT_FORMATTER = 
        DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = 
        DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    public Event(String description, String from, String to) {
        super(description);
        assert from != null && !from.trim().isEmpty() : "Event start time cannot be null or empty";
        assert to != null && !to.trim().isEmpty() : "Event end time cannot be null or empty";
        
        this.from = LocalDateTime.parse(from, INPUT_FORMATTER);
        this.to = LocalDateTime.parse(to, INPUT_FORMATTER);
        
        assert this.from.isBefore(this.to) : "Event start time must be before end time";
    }

    /**
     * Gets the start time of the event.
     * @return The start time in d/M/yyyy HHmm format
     */
    public String getFrom() {
        assert from != null : "Event start time should never be null";
        return from.format(INPUT_FORMATTER);
    }

    /**
     * Gets the end time of the event.
     * @return The end time in d/M/yyyy HHmm format
     */
    public String getTo() {
        assert to != null : "Event end time should never be null";
        return to.format(INPUT_FORMATTER);
    }

    @Override
    public String toString() {
        assert from != null && to != null : "Event times should never be null";
        return "[E]" + super.toString() + " (from: " + from.format(OUTPUT_FORMATTER) 
            + " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }
}
