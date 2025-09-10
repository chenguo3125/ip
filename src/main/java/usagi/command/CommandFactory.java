package usagi.command;

import usagi.task.TaskList;
import usagi.storage.Storage;
import usagi.exception.UsagiException;

/**
 * Factory class for creating command objects based on user input.
 * This class is responsible for parsing user input and creating appropriate command objects.
 */
public class CommandFactory {
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_ON = "on";
    
    private final TaskList tasks;
    private final Storage storage;
    
    public CommandFactory(TaskList tasks, Storage storage) {
        this.tasks = tasks;
        this.storage = storage;
    }
    
    /**
     * Creates a command based on the user input.
     * 
     * @param input The user input string
     * @return A command object that can be executed
     * @throws UsagiException If the input is invalid or command cannot be created
     */
    public Command createCommand(String input) throws UsagiException {
        if (input == null) {
            throw new UsagiException("Input cannot be null");
        }
        
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new UsagiException("Input cannot be empty");
        }
        
        // Validate that we have the required dependencies
        if (tasks == null) {
            throw new UsagiException("Task list is not initialized");
        }
        if (storage == null) {
            throw new UsagiException("Storage is not initialized");
        }
        
        if (COMMAND_BYE.equals(trimmedInput)) {
            return new ExitCommand();
        } else if (COMMAND_LIST.equals(trimmedInput)) {
            return new ListCommand(tasks);
        } else if (trimmedInput.startsWith(COMMAND_ON + " ")) {
            return new TasksOnDateCommand(tasks, trimmedInput);
        } else if (trimmedInput.startsWith(COMMAND_FIND + " ")) {
            return new FindCommand(tasks, trimmedInput);
        } else if (trimmedInput.contains(COMMAND_MARK) || trimmedInput.contains(COMMAND_UNMARK)) {
            return new MarkCommand(tasks, storage, trimmedInput);
        } else if (trimmedInput.contains(COMMAND_DELETE)) {
            return new DeleteCommand(tasks, storage, trimmedInput);
        } else if (trimmedInput.startsWith(COMMAND_TODO + " ")) {
            return new AddTodoCommand(tasks, storage, trimmedInput);
        } else if (trimmedInput.startsWith(COMMAND_DEADLINE + " ")) {
            return new AddDeadlineCommand(tasks, storage, trimmedInput);
        } else if (trimmedInput.startsWith(COMMAND_EVENT + " ")) {
            return new AddEventCommand(tasks, storage, trimmedInput);
        } else {
            throw new UsagiException("I don't understand that command. Try: list, todo, deadline, event, mark, unmark, delete, find, or bye");
        }
    }
}
