package usagi.command;

import usagi.task.TaskList;
import usagi.task.Task;
import usagi.task.ToDos;
import usagi.storage.Storage;
import usagi.exception.UsagiException;

/**
 * Command to add a new todo task.
 */
public class AddTodoCommand implements Command {
    private final TaskList tasks;
    private final Storage storage;
    private final String input;
    
    public AddTodoCommand(TaskList tasks, Storage storage, String input) {
        this.tasks = tasks;
        this.storage = storage;
        this.input = input;
    }
    
    @Override
    public String execute() throws UsagiException {
        if (!input.trim().contains(" ")) {
            throw new UsagiException("todo must be followed by a description");
        }
        
        String[] parts = input.split(" ", 2);
        String description = parts[1].trim();
        
        if (description.isEmpty()) {
            throw new UsagiException("todo description cannot be empty");
        }
        
        Task task = new ToDos(description, false);
        tasks.add(task);
        storage.save(tasks.all());
        return "Got it. I've added this task:\n  " + task.toString() + 
               "\nNow you have " + tasks.size() + " task(s) in the list.";
    }
}
