package usagi.command;

import usagi.task.TaskList;
import usagi.task.Task;
import usagi.storage.Storage;
import usagi.exception.UsagiException;

/**
 * Command to delete a task.
 */
public class DeleteCommand implements Command {
    private final TaskList tasks;
    private final Storage storage;
    private final String input;
    
    public DeleteCommand(TaskList tasks, Storage storage, String input) {
        this.tasks = tasks;
        this.storage = storage;
        this.input = input;
    }
    
    @Override
    public String execute() throws UsagiException {
        String[] parts = input.split(" ", 2);
        if (parts.length != 2) {
            throw new UsagiException("Invalid delete command format");
        }
        
        int taskNumber = Integer.parseInt(parts[1]) - 1; // Convert from 1-based to 0-based
        
        if (taskNumber < 0 || taskNumber >= tasks.size()) {
            throw new UsagiException("Invalid task number");
        }
        
        Task removed = tasks.delete(taskNumber + 1); // delete() expects 1-based index
        storage.save(tasks.all());
        return "Noted. I've removed this task:\n  " + removed.toString() + 
               "\nNow you have " + tasks.size() + " task(s) in the list.";
    }
}
