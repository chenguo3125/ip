package usagi.command;

import usagi.task.TaskList;
import usagi.task.RecurringTask;
import usagi.task.Task;
import usagi.exception.UsagiException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to show upcoming recurring tasks.
 * Format: upcoming [days]
 * If no days specified, shows tasks for the next 7 days.
 * If days specified, shows tasks for the next N days.
 */
public class UpcomingRecurringCommand implements Command {
    private final TaskList tasks;
    private final String input;
    
    public UpcomingRecurringCommand(TaskList tasks, String input) {
        this.tasks = tasks;
        this.input = input;
    }
    
    @Override
    public String execute() throws UsagiException {
        if (input == null || input.trim().isEmpty()) {
            throw new UsagiException("Input cannot be null or empty");
        }
        
        String content = input.substring("upcoming".length()).trim();
        int days = 7; // Default to 7 days
        
        if (!content.isEmpty()) {
            try {
                days = Integer.parseInt(content);
                if (days <= 0) {
                    throw new UsagiException("Days must be positive, got: " + days);
                }
            } catch (NumberFormatException e) {
                throw new UsagiException("Invalid days format. Please provide a valid number.");
            }
        }
        
        return showUpcomingRecurringTasks(days);
    }
    
    /**
     * Shows recurring tasks that are due within the specified number of days.
     * 
     * @param days Number of days to look ahead
     * @return Formatted string of upcoming recurring tasks
     */
    private String showUpcomingRecurringTasks(int days) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(days);
        
        List<String> upcomingTasks = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        
        List<Task> allTasks = tasks.all();
        for (int i = 0; i < allTasks.size(); i++) {
            Task task = allTasks.get(i);
            if (task instanceof RecurringTask) {
                RecurringTask recurringTask = (RecurringTask) task;
                LocalDate nextOccurrence = recurringTask.getNextOccurrence();
                
                // Check if the next occurrence is within the specified range
                if (!nextOccurrence.isBefore(today) && !nextOccurrence.isAfter(endDate)) {
                    String status = recurringTask.toString().startsWith("[R][X]") ? "[X]" : "[ ]";
                    String formattedDate = nextOccurrence.format(formatter);
                    String taskTitle = recurringTask.toString().substring(4); // Remove "[R]" prefix
                    upcomingTasks.add("  " + (i + 1) + ". " + status + " " + taskTitle + 
                                    " (due: " + formattedDate + ")");
                }
            }
        }
        
        if (upcomingTasks.isEmpty()) {
            return "No recurring tasks due in the next " + days + " day(s).";
        }
        
        StringBuilder result = new StringBuilder();
        result.append("Here are your recurring tasks due in the next ").append(days).append(" day(s):\n");
        result.append(String.join("\n", upcomingTasks));
        
        return result.toString();
    }
}
