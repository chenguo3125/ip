/**
 * Represents a Deadline task with title, completion status and a by time.
 * This is a concrete class inherited from the abstract Task class.
 */

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task{
    protected LocalDateTime by;

    /**
     * Returns a DateTimeFormatter that takes in a LocalDateTime and formats it into a String.
     */
    private static final DateTimeFormatter UI = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");

    public Deadline(String title, boolean done, LocalDateTime by) {
        super(title, done);
        this.by = by;
    }

    @Override
    public String type() {
        return "D";
    }

    public String[] extra() {
        return new String[]{by.toString()};
    }

    public String toString() {
        return "[D]" + super.toString() + " (by: " + UI.format(by) + ")";
    }
}
