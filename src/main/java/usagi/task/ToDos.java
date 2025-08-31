package usagi.task;

/**
 * Represents a Todo task with title and completion status.
 * This is a concrete class inherited from the abstract Task class.
 */
public class ToDos extends Task {
    /**
     * Constructs a new Todo task with the specified title and completion status.
     * 
     * @param title The title/description of the todo task
     * @param done The completion status of the task
     */
    public ToDos(String title, boolean done) {
        super(title, done);
    }

    @Override
    public String type() {
        return "T";
    }

    @Override
    public String[] extra() {
        return new String[0];
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
