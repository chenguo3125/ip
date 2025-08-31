package usagi.ui;

import java.util.List;
import java.util.Scanner;
import usagi.task.Task;

/**
 * Handles all user interface interactions including input/output operations
 * and displaying messages to the user.
 * 
 * This class encapsulates all UI-related functionality, providing a clean
 * separation between the user interface and the business logic.
 */
public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the welcome message when the application starts.
     */
    public void showWelcome() {
        String hi = "Ura! Ura-yaha-yiaha!\n" +
                "Una-una?\n";
        System.out.println(hi);
    }

    /**
     * Reads a single line of input from the user.
     * 
     * @return The user's input as a string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays a single line of text to the user.
     * 
     * @param s The text to display
     */
    public void showLine(String s) {
        System.out.println(s);
    }

    /**
     * Displays an error message when task loading fails.
     */
    public void showLoadingError() {
        System.out.println("Ura? (failed to load tasks from file)");
    }

    /**
     * Displays the list of all tasks to the user.
     * 
     * @param tasks The list of tasks to display
     */
    public void showList(List<Task> tasks) {
        System.out.println("Yiaha~(here is the list)\nlist:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }
    }

    /**
     * Displays a confirmation message when a task is successfully added.
     * 
     * @param task The task that was added
     * @param total The total number of tasks after adding
     */
    public void showAdded(Task task, int total) {
        System.out.println(task.toString() + "\nUra! (now u have " + total + " tasks in the list.)");
    }

    /**
     * Displays a confirmation message when a task is successfully deleted.
     * 
     * @param task The task that was deleted
     * @param total The total number of tasks after deletion
     */
    public void showDeleted(Task task, int total) {
        System.out.println(task.toString() + "\nUra! (remove! now u have " + total + " tasks in the list.)");
    }

    /**
     * Displays a confirmation message when a task is marked as done.
     * 
     * @param t The task that was marked
     */
    public void showMarked(Task t) {
        System.out.println("iyaaa! (marked!)\n" + t.toString());
    }

    /**
     * Displays a confirmation message when a task is unmarked.
     * 
     * @param t The task that was unmarked
     */
    public void showUnmarked(Task t) {
        System.out.println("iya-ha! (unmarked!)\n" + t.toString());
    }

    /**
     * Displays tasks that occur on a specific date.
     * 
     * @param tasks The list of tasks that occur on the specified date
     */
    public void showOnDate(List<Task> tasks) {
        System.out.println("Yiaha~(tasks on that date)\nlist:");
        if (tasks.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }
    }

    /**
     * Displays tasks that match the search keyword.
     * 
     * @param tasks The list of tasks that match the search criteria
     */
    public void showFound(List<Task> tasks) {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the matching tasks in your list:");
        if (tasks.isEmpty()) {
            System.out.println(" (none)");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i).toString());
            }
        }
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays the goodbye message when the application exits.
     */
    public void showBye() {
        System.out.println("Yiaaa~~~~haaa~~~~~(bye~~~~~)");
    }

    /**
     * Displays an error message when the user input cannot be understood.
     */
    public void showUnknown() {
        System.out.println("Ura? (Usagi cannot understand ur gibberish)");
    }
}


