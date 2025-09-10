package usagi.command;

import usagi.exception.UsagiException;

/**
 * Represents a command that can be executed by the Usagi chatbot.
 * This interface follows the Command pattern to encapsulate different operations.
 */
public interface Command {
    /**
     * Executes the command and returns the result.
     * 
     * @return The result of executing the command
     * @throws UsagiException If an error occurs during command execution
     */
    String execute() throws UsagiException;
}
