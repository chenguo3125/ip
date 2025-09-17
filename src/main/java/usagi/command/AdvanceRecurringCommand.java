package usagi.command;

import usagi.task.TaskList;
import usagi.task.RecurringTask;
import usagi.task.Task;
import usagi.storage.Storage;
import usagi.exception.UsagiException;

/**
 * Command to advance recurring tasks to their next occurrence.
 * Format: advance [index]
 * If no index is provided, advances all recurring tasks.
 * If index is provided, advances only the specified recurring task.
 */
public class AdvanceRecurringCommand implements Command {
    private final TaskList tasks;
    private final Storage storage;
    private final String input;
    
    public AdvanceRecurringCommand(TaskList tasks, Storage storage, String input) {
        this.tasks = tasks;
        this.storage = storage;
        this.input = input;
    }
    
    @Override
    public String execute() throws UsagiException {
        if (input == null || input.trim().isEmpty()) {
            throw new UsagiException("Input cannot be null or empty");
        }
        
        String content = input.substring("advance".length()).trim();
        
        if (content.isEmpty()) {
            // Advance all recurring tasks
            return advanceAllRecurringTasks();
        } else {
            // Advance specific recurring task
            return advanceSpecificRecurringTask(content);
        }
    }
    
    /**
     * Advances all recurring tasks to their next occurrence.
     * 
     * @return Success message
     * @throws UsagiException If there's an error
     */
    private String advanceAllRecurringTasks() throws UsagiException {
        int advancedCount = 0;
        
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task instanceof RecurringTask) {
                RecurringTask recurringTask = (RecurringTask) task;
                recurringTask.advanceToNextOccurrence();
                advancedCount++;
            }
        }
        
        if (advancedCount == 0) {
            return "No recurring tasks found to advance.";
        }
        
        // Save changes
        storage.save(tasks.all());
        
        return "Advanced " + advancedCount + " recurring task(s) to their next occurrence.";
    }
    
    /**
     * Advances a specific recurring task to its next occurrence.
     * 
     * @param indexStr The index string
     * @return Success message
     * @throws UsagiException If there's an error
     */
    private String advanceSpecificRecurringTask(String indexStr) throws UsagiException {
        try {
            int index = Integer.parseInt(indexStr);
            
            if (index <= 0 || index > tasks.size()) {
                throw new UsagiException("Index out of bounds. Please provide a valid task index.");
            }
            
            Task task = tasks.get(index - 1);
            
            if (!(task instanceof RecurringTask)) {
                throw new UsagiException("Task at index " + index + " is not a recurring task.");
            }
            
            RecurringTask recurringTask = (RecurringTask) task;
            java.time.LocalDate oldNextOccurrence = recurringTask.getNextOccurrence();
            recurringTask.advanceToNextOccurrence();
            java.time.LocalDate newNextOccurrence = recurringTask.getNextOccurrence();
            
            // Save changes
            storage.save(tasks.all());
            
            return "Advanced recurring task '" + recurringTask.toString().substring(4) + "' from " + 
                   oldNextOccurrence + " to " + newNextOccurrence + ".";
                   
        } catch (NumberFormatException e) {
            throw new UsagiException("Invalid index format. Please provide a valid number.");
        }
    }
}
