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
    private Parser parser;

    /**
     * Constructs a new Usagi chatbot instance with the specified file path for data storage.
     * 
     * @param filePath The path to the file where tasks will be stored and loaded from
     * @throws IllegalArgumentException if filePath is null or empty
     */
    public Usagi(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be empty");
        }
        
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (UsagiException e) {
            System.out.println("Error loading tasks from file. Starting with empty task list.");
            tasks = new TaskList();
        }
        parser = new Parser(tasks, storage);
    }

    /**
     * Gets a response for the given input without running the full application loop.
     * This is useful for GUI applications.
     * 
     * @param input The user input to process
     * @return The response message
     */
    public String getResponse(String input) {
        if (Parser.isExit(input)) {
            return "Goodbye! See you next time!";
        }
        
        try {
            return parser.handle(input);
        } catch (UsagiException e) {
            return "Ura? (" + e.getMessage() + ")";
        }
    }

    /**
     * Starts the main application loop, handling user input and commands
     * until the user chooses to exit.
     */
    public void run() {
        System.out.println("Hello! I'm Usagi, your personal task manager.");
        System.out.println("What can I do for you?");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (Parser.isExit(input)) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }
            try {
                String response = parser.handle(input);
                System.out.println(response);
            } catch (UsagiException e) {
                System.out.println("Ura? (" + e.getMessage() + ")");
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
