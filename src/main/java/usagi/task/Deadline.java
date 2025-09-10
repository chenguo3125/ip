package usagi.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Deadline task with title, completion status and a by time.
 * This is a concrete class inherited from the abstract Task class.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * DateTimeFormatter for formatting LocalDateTime objects for UI display.
     */
    private static final DateTimeFormatter UI = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");

    /**
     * Constructs a new Deadline task with the specified title, completion status, and due date.
     * 
     * @param title The title/description of the deadline task
     * @param done The completion status of the task
     * @param by The due date and time for the task
     */
    public Deadline(String title, boolean done, LocalDateTime by) {
        super(title, done);
        assert by != null : "Deadline 'by' time cannot be null";
        this.by = by;
    }

    @Override
    public String type() {
        return "D";
    }

    @Override
    public String[] extra() {
        return new String[]{by.toString()};
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + UI.format(by) + ")";
    }
}
