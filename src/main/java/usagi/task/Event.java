package usagi.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an Event task with title, completion status, start time, and end time.
 * This is a concrete class inherited from the abstract Task class.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * DateTimeFormatter for formatting LocalDateTime objects for UI display.
     */
    private static final DateTimeFormatter UI = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");

    /**
     * Constructs a new Event task with the specified title, completion status, start time, and end time.
     * 
     * @param title The title/description of the event task
     * @param done The completion status of the task
     * @param from The start date and time of the event
     * @param to The end date and time of the event
     */
    public Event(String title, boolean done, LocalDateTime from, LocalDateTime to) {
        super(title, done);
        this.from = from;
        this.to = to;
    }

    @Override
    public String type() {
        return "E";
    }

    @Override
    public String[] extra() {
        return new String[]{from.toString(), to.toString()};
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + UI.format(from) + " to: " + UI.format(to) + ")";
    }
}