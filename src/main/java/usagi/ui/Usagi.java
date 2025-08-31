package usagi.ui;

/**
 * Main chatbot application class that orchestrates the interaction between
 * user interface, storage, task management, and command parsing.
 * 
 * This class serves as the entry point for the Usagi chatbot application,
 * managing the main application loop and coordinating between different
 * components of the system.
 */

import java.util.Scanner;
import usagi.storage.Storage;
import usagi.task.TaskList;
import usagi.parser.Parser;
import usagi.exception.UsagiException;

public class Usagi {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a new Usagi chatbot instance with the specified file path for data storage.
     * 
     * @param filePath The path to the file where tasks will be stored and loaded from
     */
    public Usagi(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (UsagiException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Starts the main application loop, handling user input and commands
     * until the user chooses to exit.
     */
    public void run() {
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (Parser.isExit(input)) {
                ui.showBye();
                break;
            }
            try {
                Parser.handle(input, tasks, storage, ui);
            } catch (UsagiException e) {
                ui.showLine("Ura? (" + e.getMessage() + ")");
            }
        }
    }

    /**
     * Main entry point for the Usagi chatbot application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        new Usagi("data/usagi.txt").run();
    }
}