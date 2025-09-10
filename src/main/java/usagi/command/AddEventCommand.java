package usagi.command;

import usagi.task.TaskList;
import usagi.task.Task;
import usagi.task.Event;
import usagi.storage.Storage;
import usagi.exception.UsagiException;
import java.time.LocalDateTime;

/**
 * Command to add a new event task.
 */
public class AddEventCommand implements Command {
    private final TaskList tasks;
    private final Storage storage;
    private final String input;
    
    public AddEventCommand(TaskList tasks, Storage storage, String input) {
        this.tasks = tasks;
        this.storage = storage;
        this.input = input;
    }
    
    @Override
    public String execute() throws UsagiException {
        if (!input.trim().contains(" ")) {
            throw new UsagiException("event must be followed by a description and a /from and /to");
        }
        
        String[] parts = input.split(" /from ");
        if (parts.length != 2) {
            throw new UsagiException("event must include /from followed by start date/time");
        }
        
        String[] p1 = parts[0].split(" ", 2);
        if (p1.length != 2) {
            throw new UsagiException("event must be followed by a description");
        }
        
        String[] p2 = parts[1].split(" /to ");
        if (p2.length != 2) {
            throw new UsagiException("event must include /to followed by end date/time");
        }
        
        String title = p1[1].trim();
        String rawFrom = p2[0].trim();
        String rawTo = p2[1].trim();
        
        if (title.isEmpty()) {
            throw new UsagiException("event description cannot be empty");
        }
        if (rawFrom.isEmpty()) {
            throw new UsagiException("event start date/time cannot be empty");
        }
        if (rawTo.isEmpty()) {
            throw new UsagiException("event end date/time cannot be empty");
        }
        
        LocalDateTime from = Task.parseDateTimeFlexible(rawFrom);
        LocalDateTime to = Task.parseDateTimeFlexible(rawTo);
        
        Task task = new Event(title, false, from, to);
        tasks.add(task);
        storage.save(tasks.all());
        return "Got it. I've added this task:\n  " + task.toString() + 
               "\nNow you have " + tasks.size() + " task(s) in the list.";
    }
}
