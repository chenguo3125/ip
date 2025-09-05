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
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import usagi.storage.Storage;
import usagi.task.TaskList;
import usagi.task.Task;
import usagi.parser.Parser;
import usagi.exception.UsagiException;

public class Usagi {
    private Storage storage;
    private TaskList tasks;

    /**
     * Constructs a new Usagi chatbot instance with the specified file path for data storage.
     * 
     * @param filePath The path to the file where tasks will be stored and loaded from
     */
    public Usagi(String filePath) {
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (UsagiException e) {
            System.out.println("Error loading tasks from file. Starting with empty task list.");
            tasks = new TaskList();
        }
    }

    public String getResponse(String input) {
        if (Parser.isExit(input)) {
            return "Goodbye! See you next time!";
        }
        
        try {
            // Use the existing parser logic directly
            return Parser.handle(input, tasks, storage);
        } catch (UsagiException e) {
            return "Ura? (" + e.getMessage() + ")";
        }
    }

    /**
     * Starts the main application loop, handling user input and commands
     * until the user chooses to exit.
     */
    public void run() {
        System.out.println("Hello! I'm Usagi, your personal task manager!");
        System.out.println("What can I do for you?");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (Parser.isExit(input)) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }
            try {
                String response = Parser.handle(input, tasks, storage);
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
