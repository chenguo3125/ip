package usagi.command;

import usagi.task.TaskList;
import usagi.task.RecurringTask;
import usagi.task.Task;
import usagi.storage.Storage;
import usagi.exception.UsagiException;

/**
 * Command to add a new recurring task to the task list.
 * Format: recurring <title> /from <start-time> /to <end-time> /every <pattern> [interval]
 * Example: recurring Weekly team meeting /from 2024-01-15 14:00 /to 2024-01-15 15:00 /every weekly
 * Example: recurring Monthly review /from 2024-01-01 09:00 /to 2024-01-01 10:00 /every monthly 1
 */
public class AddRecurringTaskCommand implements Command {
    private static final String COMMAND_PREFIX = "recurring";
    private static final String FROM_MARKER = "/from";
    private static final String TO_MARKER = "/to";
    private static final String EVERY_MARKER = "/every";
    private static final String DEFAULT_INTERVAL = "1";
    
    private final TaskList tasks;
    private final Storage storage;
    private final String input;
    
    public AddRecurringTaskCommand(TaskList tasks, Storage storage, String input) {
        this.tasks = tasks;
        this.storage = storage;
        this.input = input;
    }
    
    @Override
    public String execute() throws UsagiException {
        if (input == null || input.trim().isEmpty()) {
            throw new UsagiException("Input cannot be null or empty");
        }
        
        // Parse the input to extract task details
        String[] parts = parseRecurringTaskInput(input);
        String title = parts[0];
        String startTimeStr = parts[1];
        String endTimeStr = parts[2];
        String patternStr = parts[3];
        int interval = parts.length > 4 ? Integer.parseInt(parts[4]) : 1;
        
        // Validate inputs
        if (title.trim().isEmpty()) {
            throw new UsagiException("Task title cannot be empty");
        }
        
        if (interval <= 0) {
            throw new UsagiException("Interval must be positive, got: " + interval);
        }
        
        if (interval > 365) {
            throw new UsagiException("Interval too large, maximum allowed is 365");
        }
        
        try {
            // Parse start and end times
            java.time.LocalDateTime startTime = Task.parseDateTimeFlexible(startTimeStr);
            java.time.LocalDateTime endTime = Task.parseDateTimeFlexible(endTimeStr);
            
            // Parse recurrence pattern
            RecurringTask.RecurrencePattern pattern = RecurringTask.RecurrencePattern.fromString(patternStr);
            
            // Create the recurring task
            RecurringTask recurringTask = new RecurringTask(title.trim(), false, startTime, endTime, pattern, interval);
            
            // Add to task list
            tasks.add(recurringTask);
            
            // Save to storage
            storage.save(tasks.all());
            
            return "Got it. I've added this recurring task:\n" + 
                   "  " + recurringTask + "\n" +
                   "Now you have " + tasks.size() + " task(s) in the list.";
                   
        } catch (IllegalArgumentException e) {
            throw new UsagiException("Invalid input format: " + e.getMessage() + 
                "\nCorrect format: " + COMMAND_PREFIX + " <title> " + FROM_MARKER + " <start-time> " + TO_MARKER + " <end-time> " + EVERY_MARKER + " <pattern> [interval]");
        } catch (Exception e) {
            throw new UsagiException("Error creating recurring task: " + e.getMessage());
        }
    }
    
    /**
     * Parses the recurring task input string to extract components.
     * 
     * @param input The input string
     * @return Array containing [title, startTime, endTime, pattern, interval]
     * @throws UsagiException If the input format is invalid
     */
    private String[] parseRecurringTaskInput(String input) throws UsagiException {
        // Remove the command prefix
        String content = input.substring(COMMAND_PREFIX.length()).trim();
        
        // Find the required markers
        int fromIndex = content.indexOf(FROM_MARKER);
        int toIndex = content.indexOf(TO_MARKER);
        int everyIndex = content.indexOf(EVERY_MARKER);
        
        if (fromIndex == -1 || toIndex == -1 || everyIndex == -1) {
            throw new UsagiException("Missing required markers. Use: " + FROM_MARKER + ", " + TO_MARKER + ", " + EVERY_MARKER);
        }
        
        if (fromIndex >= toIndex || toIndex >= everyIndex) {
            throw new UsagiException("Markers must be in order: " + FROM_MARKER + ", " + TO_MARKER + ", " + EVERY_MARKER);
        }
        
        try {
            String title = content.substring(0, fromIndex).trim();
            String startTimeStr = content.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
            String endTimeStr = content.substring(toIndex + TO_MARKER.length(), everyIndex).trim();
            String patternAndInterval = content.substring(everyIndex + EVERY_MARKER.length()).trim();
            
            // Parse pattern and optional interval
            String[] patternParts = patternAndInterval.split("\\s+");
            String patternStr = patternParts[0];
            String intervalStr = patternParts.length > 1 ? patternParts[1] : DEFAULT_INTERVAL;
            
            return new String[]{title, startTimeStr, endTimeStr, patternStr, intervalStr};
            
        } catch (Exception e) {
            throw new UsagiException("Error parsing input: " + e.getMessage());
        }
    }
}
