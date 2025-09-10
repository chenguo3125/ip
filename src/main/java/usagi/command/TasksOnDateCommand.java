package usagi.command;

import usagi.task.TaskList;
import usagi.task.Task;
import usagi.exception.UsagiException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Command to find tasks on a specific date.
 */
public class TasksOnDateCommand implements Command {
    private final TaskList tasks;
    private final String input;
    
    public TasksOnDateCommand(TaskList tasks, String input) {
        this.tasks = tasks;
        this.input = input;
    }
    
    @Override
    public String execute() throws UsagiException {
        String raw = input.substring(3).trim(); // Remove "on "
        if (raw.isEmpty()) {
            throw new UsagiException("Date cannot be empty");
        }
        
        LocalDate date = Task.parseDateFlexible(raw);
        List<Task> tasksOnDate = tasks.tasksOn(date);
        
        if (tasksOnDate.isEmpty()) {
            return "You have no tasks on " + date.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ".";
        } else {
            StringBuilder sb = new StringBuilder("Here are your tasks on " + 
                date.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":\n");
            for (int i = 0; i < tasksOnDate.size(); i++) {
                sb.append((i + 1) + "." + tasksOnDate.get(i).toString() + "\n");
            }
            return sb.toString().trim();
        }
    }
}
