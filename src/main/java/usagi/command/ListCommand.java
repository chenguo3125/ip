package usagi.command;

import usagi.task.TaskList;
import usagi.task.Task;

/**
 * Command to list all tasks.
 */
public class ListCommand implements Command {
    private final TaskList tasks;
    
    public ListCommand(TaskList tasks) {
        this.tasks = tasks;
    }
    
    @Override
    public String execute() {
        if (tasks.size() == 0) {
            return "You have no tasks in your list.";
        } else {
            StringBuilder sb = new StringBuilder("Here are your tasks:\n");
            for (int i = 0; i < tasks.size(); i++) {
                sb.append((i + 1) + "." + tasks.getByIndex(i).toString() + "\n");
            }
            return sb.toString().trim();
        }
    }
}
