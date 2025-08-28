/**
 * Represents a task with title, completion status and respective time information.
 *
 * This is an abstract class meant to be extended by concrete task types
 * like {@link Todo}, {@link Deadline}, and {@link Event}.
 */

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class Task {
    protected String title;
    protected Boolean isDone;

    /**
     * Returns the type of the Task from "T", "D", "E".
     */
    abstract String type();

    /**
     * Returns the additional time information of the task.
     */
    abstract String[] extra();

    public Task(String title, boolean done) {
        this.title = title;
        this.isDone = done;
    }

    /**
     * Changes the completion status of a task isDone to true.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Changes the completion status of a task isDone to false.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns a String representation of the Task for save in storage.
     */
    public String toLine() {
        List<String> parts = new ArrayList<>();
        parts.add(type());
        parts.add(isDone ? "1" : "0");
        parts.add(title);
        parts.addAll(Arrays.asList(extra()));  // append extra fields if any
        return String.join(" | ", parts);
    }

    /**
     * Returns a Task from its String representation for load in storage.
     *
     * @param line String representation of the task. (task.toLine())
     */
    public static Task fromLine(String line) {
        String[] p = line.split("\\s*\\|\\s*"); // splits on ' | ' with spaces ok
        String t = p[0], done = p[1], desc = p[2];
        switch (t) {
            case "T": return new ToDos(desc, "1".equals(done));
            case "D": return new Deadline(desc, "1".equals(done), parseDateTimeOrDate(p[3]));
            case "E": return new Event(desc, "1".equals(done), parseDateTimeFlexible(p[3]), parseDateTimeFlexible(p[4]));
            default: throw new IllegalArgumentException("Bad type: " + t);
        }
    }

    public String toString() {
        if (isDone) {
            return "[X] " + this.title;
        } else {
            return "[ ] " + this.title;
        }
    }

    /**
     * Returns a LocalDate from the user input of the date.
     *
     * @param raw String input of the date from the user.
     */
    static LocalDate parseDateFlexible(String raw) {
        try { return LocalDate.parse(raw); } catch (DateTimeParseException ignore) {}

        for (String pattern : new String[]{"d/M/yyyy", "M/d/yyyy"}) {
            try {
                return LocalDate.parse(raw, DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException ignore) {}
        }
        throw new IllegalArgumentException("Cannot parse date: " + raw);
    }

    /**
     * Returns a LocalDateTime from the user input of the date with time.
     *
     * @param raw String input of the date with time from the user.
     */
    static LocalDateTime parseDateTimeFlexible(String raw) {
        try { return LocalDateTime.parse(raw); } catch (DateTimeParseException ignore) {}

        for (String pattern : new String[]{"yyyy-MM-dd HHmm", "d/M/yyyy HHmm", "M/d/yyyy HHmm"}) {
            try {
                return LocalDateTime.parse(raw, DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException ignore) {}
        }

        try {
            return parseDateFlexible(raw).atStartOfDay();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Cannot parse date-time: " + raw);
        }
    }

    /**
     * Returns a LocalDateTime from the user input of the date or time.
     *
     * @param raw String input of the date or time from the user.
     */
    static LocalDateTime parseDateTimeOrDate(String raw) {
        return parseDateTimeFlexible(raw);
    }
}
