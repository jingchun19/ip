package taskscommand;

public class Event extends Task{
    //Date start
    protected String from;
    //Date end
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        assert from != null && !from.trim().isEmpty() : "Event start time cannot be null or empty";
        assert to != null && !to.trim().isEmpty() : "Event end time cannot be null or empty";
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        assert from != null && to != null : "Event times should never be null";
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
