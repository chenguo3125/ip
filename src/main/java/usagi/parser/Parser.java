package usagi.parser;

import usagi.command.Command;
import usagi.command.CommandFactory;
import usagi.task.TaskList;
import usagi.storage.Storage;
import usagi.exception.UsagiException;

/**
 * Handles parsing and execution of user commands using the Command pattern.
 * 
 * This class is responsible for creating command objects from user input
 * and executing them, providing a clean separation between parsing and execution.
 */
public class Parser {
    private final CommandFactory commandFactory;
    
    public Parser(TaskList tasks, Storage storage) {
        this.commandFactory = new CommandFactory(tasks, storage);
    }
    
    /**
     * Checks if the input command is an exit command.
     * 
     * @param input The user input to check
     * @return true if the input is an exit command, false otherwise
     */
    public static boolean isExit(String input) {
        return "bye".equals(input);
    }

    /**
     * Parses and handles user input commands, performing appropriate actions
     * on the task list and storage.
     * 
     * @param input The user input command to process
     * @return The response message as a string
     * @throws UsagiException If an error occurs during command processing
     */
    public String handle(String input) throws UsagiException {
        if (input == null) {
            throw new UsagiException("Input cannot be null");
        }
        
        Command command = commandFactory.createCommand(input);
        return command.execute();
    }
}