package usagi.command;

import usagi.task.TaskList;
import usagi.task.Task;
import usagi.storage.Storage;
import usagi.exception.UsagiException;

/**
 * Command to mark or unmark a task.
 */
public class MarkCommand implements Command {
    private final TaskList tasks;
    private final Storage storage;
    private final String input;
    
    public MarkCommand(TaskList tasks, Storage storage, String input) {
        this.tasks = tasks;
        this.storage = storage;
        this.input = input;
    }
    
    @Override
    public String execute() throws UsagiException {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new UsagiException("Invalid mark command format");
        }
        
        String command = parts[0];
        int taskNumber = Integer.parseInt(parts[1]) - 1; // Convert from 1-based to 0-based
        
        if (taskNumber < 0 || taskNumber >= tasks.size()) {
            throw new UsagiException("Invalid task number");
        }
        
        Task task = tasks.getByIndex(taskNumber);
        
        if ("unmark".equals(command)) {
            task.unmark();
            storage.save(tasks.all());
            return "OK, I've marked this task as not done yet:\n  " + task.toString();
        } else if ("mark".equals(command)) {
            task.mark();
            storage.save(tasks.all());
            return "Nice! I've marked this task as done:\n  " + task.toString();
        } else {
            throw new UsagiException("Invalid mark command: " + command);
        }
    }
}
