package usagi.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a task with title, completion status and respective time information.
 *
 * This is an abstract class meant to be extended by concrete task types
 * like ToDos, Deadline, and Event.
 */

public abstract class Task {
    protected String title;
    protected Boolean isDone;

    /**
     * Returns the type identifier of the task.
     * 
     * @return The type identifier ("T" for Todo, "D" for Deadline, "E" for Event)
     */
    abstract String type();

    /**
     * Returns additional information specific to the task type.
     * 
     * @return An array of strings containing task-specific information
     */
    abstract String[] extra();

    /**
     * Constructs a new Task with the specified title and completion status.
     * 
     * @param title The title/description of the task
     * @param done The completion status of the task
     */
    public Task(String title, boolean done) {
        assert title != null : "Task title cannot be null";
        assert !title.trim().isEmpty() : "Task title cannot be empty";
        this.title = title;
        this.isDone = done;
    }

    /**
     * Marks the task as completed.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void unmark() {
        this.isDone = false;
    }



    /**
     * Returns a string representation of the task for storage.
     * 
     * @return A string representation suitable for saving to file
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
     * Creates a Task from its string representation.
     *
     * @param line The string representation of the task (from task.toLine())
     * @return A Task object created from the string representation
     * @throws IllegalArgumentException If the string format is invalid
     */
    public static Task fromLine(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Line cannot be null");
        }
        if (line.trim().isEmpty()) {
            throw new IllegalArgumentException("Line cannot be empty");
        }
        
        String[] p = line.split("\\s*\\|\\s*"); // splits on ' | ' with spaces ok
        if (p.length < 3) {
            throw new ArrayIndexOutOfBoundsException("Line must have at least 3 parts (type, done, description)");
        }
        
        String t = p[0], done = p[1], desc = p[2];
        if (!"0".equals(done) && !"1".equals(done)) {
            throw new IllegalArgumentException("Done status must be '0' or '1', got: " + done);
        }
        
        switch (t) {
        case "T": 
            if (p.length != 3) {
                throw new ArrayIndexOutOfBoundsException("Todo task must have exactly 3 parts");
            }
            return new ToDos(desc, "1".equals(done));
        case "D": 
            if (p.length != 4) {
                throw new ArrayIndexOutOfBoundsException("Deadline task must have exactly 4 parts");
            }
            return new Deadline(desc, "1".equals(done), parseDateTimeOrDate(p[3]));
        case "E": 
            if (p.length != 5) {
                throw new ArrayIndexOutOfBoundsException("Event task must have exactly 5 parts");
            }
            return new Event(desc, "1".equals(done), parseDateTimeFlexible(p[3]), parseDateTimeFlexible(p[4]));
        case "R":
            if (p.length != 8) {
                throw new ArrayIndexOutOfBoundsException("Recurring task must have exactly 8 parts");
            }
            return RecurringTask.fromLine(line);
        default: throw new IllegalArgumentException("Bad type: " + t);
        }
    }

    @Override
    public String toString() {
        if (isDone) {
            return "[X] " + this.title;
        } else {
            return "[ ] " + this.title;
        }
    }

    /**
     * Parses a date string in various formats and returns a LocalDate.
     * 
     * Supports ISO format (yyyy-MM-dd), day/month/year, and month/day/year formats.
     *
     * @param raw The date string to parse
     * @return A LocalDate object representing the parsed date
     * @throws IllegalArgumentException If the date string cannot be parsed
     */
    public static LocalDate parseDateFlexible(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Date string cannot be null");
        }
        if (raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be empty");
        }
        
        try { return LocalDate.parse(raw); } catch (DateTimeParseException ignore) {}

        for (String pattern : new String[]{"d/M/yyyy", "M/d/yyyy"}) {
            try {
                return LocalDate.parse(raw, DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException ignore) {}
        }
        throw new IllegalArgumentException("Cannot parse date: " + raw);
    }

    /**
     * Parses a date-time string in various formats and returns a LocalDateTime.
     * 
     * Supports ISO format, date with time (HHmm), and falls back to start of day
     * if only a date is provided.
     *
     * @param raw The date-time string to parse
     * @return A LocalDateTime object representing the parsed date and time
     * @throws IllegalArgumentException If the date-time string cannot be parsed
     */
    public static LocalDateTime parseDateTimeFlexible(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("DateTime string cannot be null");
        }
        if (raw.trim().isEmpty()) {
            throw new IllegalArgumentException("DateTime string cannot be empty");
        }
        
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
     * Parses a date or date-time string and returns a LocalDateTime.
     * 
     * This is a convenience method that delegates to parseDateTimeFlexible.
     *
     * @param raw The date or date-time string to parse
     * @return A LocalDateTime object representing the parsed date and time
     * @throws IllegalArgumentException If the string cannot be parsed
     */
    public static LocalDateTime parseDateTimeOrDate(String raw) {
        return parseDateTimeFlexible(raw);
    }
}
