package usagi.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a recurring task with title, completion status, and recurrence pattern.
 * This is a concrete class inherited from the abstract Task class.
 */
public class RecurringTask extends Task {
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected RecurrencePattern pattern;
    protected LocalDate nextOccurrence;
    protected int interval; // e.g., every 2 weeks, every 3 months
    
    /**
     * DateTimeFormatter for formatting LocalDateTime objects for UI display.
     */
    private static final DateTimeFormatter UI = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
    
    /**
     * Enum representing different recurrence patterns.
     */
    public enum RecurrencePattern {
        DAILY("daily"),
        WEEKLY("weekly"), 
        MONTHLY("monthly"),
        YEARLY("yearly");
        
        private final String displayName;
        
        RecurrencePattern(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public static RecurrencePattern fromString(String pattern) {
            for (RecurrencePattern p : values()) {
                if (p.displayName.equalsIgnoreCase(pattern)) {
                    return p;
                }
            }
            throw new IllegalArgumentException("Invalid recurrence pattern: " + pattern);
        }
    }
    
    /**
     * Constructs a new RecurringTask with the specified parameters.
     * 
     * @param title The title/description of the recurring task
     * @param done The completion status of the task
     * @param startTime The start time of the recurring task
     * @param endTime The end time of the recurring task
     * @param pattern The recurrence pattern (daily, weekly, monthly, yearly)
     * @param interval The interval between occurrences (e.g., 1 for every week, 2 for every 2 weeks)
     */
    public RecurringTask(String title, boolean done, LocalDateTime startTime, LocalDateTime endTime, 
                        RecurrencePattern pattern, int interval) {
        super(title, done);
        if (startTime == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("Recurrence pattern cannot be null");
        }
        if (interval <= 0) {
            throw new IllegalArgumentException("Interval must be positive, got: " + interval);
        }
        
        this.startTime = startTime;
        this.endTime = endTime;
        this.pattern = pattern;
        this.interval = interval;
        this.nextOccurrence = startTime.toLocalDate();
    }
    
    /**
     * Constructs a new RecurringTask with default interval of 1.
     * 
     * @param title The title/description of the recurring task
     * @param done The completion status of the task
     * @param startTime The start time of the recurring task
     * @param endTime The end time of the recurring task
     * @param pattern The recurrence pattern (daily, weekly, monthly, yearly)
     */
    public RecurringTask(String title, boolean done, LocalDateTime startTime, LocalDateTime endTime, 
                        RecurrencePattern pattern) {
        this(title, done, startTime, endTime, pattern, 1);
    }
    
    @Override
    public String type() {
        return "R";
    }
    
    @Override
    public String[] extra() {
        return new String[]{
            startTime.toString(),
            endTime.toString(), 
            pattern.getDisplayName(),
            String.valueOf(interval),
            nextOccurrence.toString()
        };
    }
    
    @Override
    public String toString() {
        String intervalText = interval == 1 ? "" : " (every " + interval + " " + pattern.getDisplayName() + "s)";
        return "[R]" + super.toString() + " (from: " + UI.format(startTime) + " to: " + UI.format(endTime) + 
               ", " + pattern.getDisplayName() + intervalText + ", next: " + nextOccurrence + ")";
    }
    
    /**
     * Gets the next occurrence date of this recurring task.
     * 
     * @return The next occurrence date
     */
    public LocalDate getNextOccurrence() {
        return nextOccurrence;
    }
    
    /**
     * Advances the next occurrence to the next scheduled date.
     */
    public void advanceToNextOccurrence() {
        switch (pattern) {
            case DAILY:
                nextOccurrence = nextOccurrence.plusDays(interval);
                break;
            case WEEKLY:
                nextOccurrence = nextOccurrence.plusWeeks(interval);
                break;
            case MONTHLY:
                nextOccurrence = nextOccurrence.plusMonths(interval);
                break;
            case YEARLY:
                nextOccurrence = nextOccurrence.plusYears(interval);
                break;
        }
    }
    
    /**
     * Checks if this recurring task should occur on the given date.
     * 
     * @param date The date to check
     * @return true if the task should occur on this date
     */
    public boolean occursOn(LocalDate date) {
        return date.equals(nextOccurrence);
    }
    
    /**
     * Gets the start time for a specific occurrence date.
     * 
     * @param occurrenceDate The date of the occurrence
     * @return The start time for that occurrence
     */
    public LocalDateTime getStartTimeFor(LocalDate occurrenceDate) {
        return startTime.with(occurrenceDate);
    }
    
    /**
     * Gets the end time for a specific occurrence date.
     * 
     * @param occurrenceDate The date of the occurrence
     * @return The end time for that occurrence
     */
    public LocalDateTime getEndTimeFor(LocalDate occurrenceDate) {
        return endTime.with(occurrenceDate);
    }
    
    /**
     * Gets the recurrence pattern.
     * 
     * @return The recurrence pattern
     */
    public RecurrencePattern getPattern() {
        return pattern;
    }
    
    /**
     * Gets the interval between occurrences.
     * 
     * @return The interval
     */
    public int getInterval() {
        return interval;
    }
    
    /**
     * Creates a RecurringTask from its string representation.
     * 
     * @param line The string representation of the task
     * @return A RecurringTask object created from the string representation
     * @throws IllegalArgumentException If the string format is invalid
     */
    public static RecurringTask fromLine(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Line cannot be null");
        }
        if (line.trim().isEmpty()) {
            throw new IllegalArgumentException("Line cannot be empty");
        }
        
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length != 8) {
            throw new IllegalArgumentException("Recurring task must have exactly 8 parts");
        }
        
        String type = parts[0];
        String done = parts[1];
        String title = parts[2];
        String startTimeStr = parts[3];
        String endTimeStr = parts[4];
        String patternStr = parts[5];
        String intervalStr = parts[6];
        String nextOccurrenceStr = parts[7];
        
        if (!"R".equals(type)) {
            throw new IllegalArgumentException("Invalid type for recurring task: " + type);
        }
        if (!"0".equals(done) && !"1".equals(done)) {
            throw new IllegalArgumentException("Done status must be '0' or '1', got: " + done);
        }
        
        try {
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr);
            RecurrencePattern pattern = RecurrencePattern.fromString(patternStr);
            int interval = Integer.parseInt(intervalStr);
            LocalDate nextOccurrence = LocalDate.parse(nextOccurrenceStr);
            
            RecurringTask task = new RecurringTask(title, "1".equals(done), startTime, endTime, pattern, interval);
            task.nextOccurrence = nextOccurrence;
            return task;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid recurring task format: " + e.getMessage());
        }
    }
}
