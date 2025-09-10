package usagi.command;

import usagi.task.TaskList;
import usagi.task.Task;
import usagi.task.Deadline;
import usagi.storage.Storage;
import usagi.exception.UsagiException;
import java.time.LocalDateTime;

/**
 * Command to add a new deadline task.
 */
public class AddDeadlineCommand implements Command {
    private final TaskList tasks;
    private final Storage storage;
    private final String input;
    
    public AddDeadlineCommand(TaskList tasks, Storage storage, String input) {
        this.tasks = tasks;
        this.storage = storage;
        this.input = input;
    }
    
    @Override
    public String execute() throws UsagiException {
        if (!input.trim().contains(" ")) {
            throw new UsagiException("deadline must be followed by a description and a /by");
        }
        
        String[] parts = input.substring(9).split("/by", 2); // Remove "deadline "
        if (parts.length != 2) {
            throw new UsagiException("deadline must include /by followed by date/time");
        }
        
        String title = parts[0].trim();
        String rawDateTime = parts[1].trim();
        
        if (title.isEmpty()) {
            throw new UsagiException("deadline description cannot be empty");
        }
        if (rawDateTime.isEmpty()) {
            throw new UsagiException("deadline date/time cannot be empty");
        }
        
        LocalDateTime by = Task.parseDateTimeFlexible(rawDateTime);
        Task task = new Deadline(title, false, by);
        tasks.add(task);
        storage.save(tasks.all());
        return "Got it. I've added this task:\n  " + task.toString() + 
               "\nNow you have " + tasks.size() + " task(s) in the list.";
    }
}
