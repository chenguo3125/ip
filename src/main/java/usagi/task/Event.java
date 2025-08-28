package usagi.task;

/**
 * Represents a Event task with title, completion status a from and a to time.
 * This is a concrete class inherited from the abstract Task class.
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task{
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Returns a DateTimeFormatter that takes in a LocalDateTime and formats it into a String.
     */
    private static final DateTimeFormatter UI = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");

    public Event(String title, boolean done, LocalDateTime from, LocalDateTime to) {
        super(title, done);
        this.from = from;
        this.to = to;
    }

    @Override
    public String type() {
        return "E";
    }

    public String[] extra() {
        return new String[]{from.toString(), to.toString()};
    }

    public String toString() {
        return "[E]" + super.toString() + " (from: " + UI.format(from) + " to: " + UI.format(to) + ")";
    }
}