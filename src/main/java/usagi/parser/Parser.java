package usagi.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import usagi.task.Task;
import usagi.task.TaskList;
import usagi.task.Deadline;
import usagi.task.Event;
import usagi.task.ToDos;
import usagi.storage.Storage;
import usagi.ui.Ui;
import usagi.exception.UsagiException;

/**
 * Handles parsing and execution of user commands.
 * 
 * This class is responsible for interpreting user input and converting it
 * into appropriate actions on the task list and storage.
 */
public class Parser {
    /**
     * Checks if the input command is an exit command.
     * 
     * @param input The user input to check
     * @return true if the input is an exit command, false otherwise
     */
    public static boolean isExit(String input) {
        return input.equals("bye");
    }

    /**
     * Parses and handles user input commands, performing appropriate actions
     * on the task list and storage.
     * 
     * @param input The user input command to process
     * @param tasks The task list to operate on
     * @param storage The storage component for saving tasks
     * @param ui The user interface component for displaying messages
     * @throws UsagiException If an error occurs during command processing
     */
    public static void handle(String input, TaskList tasks, Storage storage, Ui ui) throws UsagiException {
        if (input.equals("list")) {
            ui.showList(tasks.all());
            return;
        } else if (input.startsWith("on ")) {
            String raw = input.substring(3).trim();
            LocalDate date = Task.parseDateFlexible(raw);
            ui.showOnDate(tasks.tasksOn(date));
            return;
        } else if (input.startsWith("find ")) {
            String keyword = input.substring(5).trim();
            if (keyword.isEmpty()) {
                ui.showLine("Ura? (find must be followed by a keyword)");
            } else {
                List<Task> matches = tasks.find(keyword);
                ui.showFound(matches);
            }
            return;
        } else if (input.contains("mark")) {
            String[] parts = input.split(" ");
            String s = parts[0];
            int num = Integer.parseInt(parts[1]);
            Task t = tasks.get(num);
            if (s.equals("unmark")) {
                t.unmark();
                ui.showUnmarked(t);
            } else {
                t.mark();
                ui.showMarked(t);
            }
            return;
        } else if (input.contains("delete")) {
            String[] parts = input.split(" ", 2);
            int num = Integer.parseInt(parts[1]);
            Task removed = tasks.delete(num);
            storage.save(tasks.all());
            ui.showDeleted(removed, tasks.size());
            return;
        } else if (input.contains("todo") || input.contains("deadline") || input.contains("event")){
            if (input.contains("todo")) {
                if (input.trim().contains(" ")) {
                    String[] parts = input.split(" ", 2);
                    Task task = new ToDos(parts[1], false);
                    tasks.add(task);
                    storage.save(tasks.all());
                    ui.showAdded(task, tasks.size());
                } else {
                    ui.showLine("Ura? (todo must be followed by a description)");
                }
            } else if (input.contains("deadline")) {
                if (input.trim().contains(" ")) {
                    String[] parts = input.substring(9).split("/by", 2);
                    String title = parts[0].trim();
                    String raw = parts.length > 1 ? parts[1].trim() : "";

                    LocalDateTime by = Task.parseDateTimeFlexible(raw);
                    Task task = new Deadline(title, false, by);
                    tasks.add(task);
                    storage.save(tasks.all());
                    ui.showAdded(task, tasks.size());
                } else {
                    ui.showLine("Ura? (deadline must be followed by a description and a /by)");
                }
            } else {
                if (input.trim().contains(" ")) {
                    String[] parts = input.split(" /from ");
                    String[] p1 = parts[0].split(" ", 2);
                    String[] p2 = parts[1].split(" /to ");

                    LocalDateTime from = Task.parseDateTimeFlexible(p2[0].trim());
                    LocalDateTime to   = Task.parseDateTimeFlexible(p2[1].trim());

                    Task task = new Event(p1[1], false, from, to);
                    tasks.add(task);
                    storage.save(tasks.all());
                    ui.showAdded(task, tasks.size());
                } else {
                    ui.showLine("Ura? (deadline must be followed by a description and a /from and /to)");
                }
            }
            return;
        }

        ui.showUnknown();
    }
}


