package usagi.command;

/**
 * Command to exit the application.
 */
public class ExitCommand implements Command {
    @Override
    public String execute() {
        return "Goodbye! See you next time!";
    }
}
