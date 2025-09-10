package usagi.command;

import usagi.task.TaskList;
import usagi.task.Task;
import usagi.exception.UsagiException;
import java.util.List;

/**
 * Command to find tasks containing a keyword.
 */
public class FindCommand implements Command {
    private final TaskList tasks;
    private final String input;
    
    public FindCommand(TaskList tasks, String input) {
        this.tasks = tasks;
        this.input = input;
    }
    
    @Override
    public String execute() throws UsagiException {
        String keyword = input.substring(5).trim(); // Remove "find "
        if (keyword.isEmpty()) {
            throw new UsagiException("find must be followed by a keyword");
        }
        
        List<Task> matches = tasks.find(keyword);
        if (matches.isEmpty()) {
            return "No tasks found matching your search.";
        } else {
            StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
            for (int i = 0; i < matches.size(); i++) {
                sb.append((i + 1) + "." + matches.get(i).toString() + "\n");
            }
            return sb.toString().trim();
        }
    }
}
