package usagi.task;

/**
 * Represents a Deadline task with title and completion status.
 * This is a concrete class inherited from the abstract Task class.
 */

public class ToDos extends Task {
    public ToDos(String title, boolean done) {
        super(title, done);
    }

    @Override
    public String type() {
        return "T";
    }

    public String[] extra() {
        return new String[0];
    }

    public String toString() {
        return "[T]" + super.toString();
    }
}
