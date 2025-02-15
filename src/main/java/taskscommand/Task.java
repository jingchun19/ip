package taskscommand;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        assert description != null && !description.trim().isEmpty() : "Task description cannot be null or empty";
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    @Override
    public String toString() {
        assert description != null : "Description should never be null";
        return "[" + this.getStatusIcon() + "] " + this.description;
    }

    public String getDescription() {
        assert description != null : "Description should never be null";
        return this.description;
    }

    public void markAsDone() {
        assert !isDone : "Task is already marked as done";
        this.isDone = true;
    }

    public void markAsNotDone() {
        assert isDone : "Task is already marked as not done";
        this.isDone = false;
    }
}