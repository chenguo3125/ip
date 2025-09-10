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
        assert input != null : "Input cannot be null";
        return input.equals("bye");
    }

    /**
     * Parses and handles user input commands, performing appropriate actions
     * on the task list and storage.
     * 
     * @param input The user input command to process
     * @param tasks The task list to operate on
     * @param storage The storage component for saving tasks
     * @return The response message as a string
     * @throws UsagiException If an error occurs during command processing
     */
    public static String handle(String input, TaskList tasks, Storage storage) throws UsagiException {
        assert input != null : "Input cannot be null";
        assert tasks != null : "TaskList cannot be null";
        assert storage != null : "Storage cannot be null";
        if (input.equals("list")) {
            if (tasks.size() == 0) {
                return "You have no tasks in your list.";
            } else {
                StringBuilder sb = new StringBuilder("Here are your tasks:\n");
                for (int i = 0; i < tasks.size(); i++) {
                    sb.append((i + 1) + "." + tasks.getByIndex(i).toString() + "\n");
                }
                return sb.toString().trim();
            }
        } else if (input.startsWith("on ")) {
            String raw = input.substring(3).trim();
            LocalDate date = Task.parseDateFlexible(raw);
            List<Task> tasksOnDate = tasks.tasksOn(date);
            if (tasksOnDate.isEmpty()) {
                return "You have no tasks on " + date.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd yyyy")) + ".";
            } else {
                StringBuilder sb = new StringBuilder("Here are your tasks on " + 
                    date.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":\n");
                for (int i = 0; i < tasksOnDate.size(); i++) {
                    sb.append((i + 1) + "." + tasksOnDate.get(i).toString() + "\n");
                }
                return sb.toString().trim();
            }
        } else if (input.startsWith("find ")) {
            String keyword = input.substring(5).trim();
            if (keyword.isEmpty()) {
                return "Ura? (find must be followed by a keyword)";
            } else {
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
        } else if (input.contains("mark")) {
            String[] parts = input.split(" ");
            String s = parts[0];
            int num = Integer.parseInt(parts[1]) - 1; // Convert from 1-based to 0-based
            if (num < 0 || num >= tasks.size()) {
                return "Ura? Invalid task number.";
            }
            Task t = tasks.getByIndex(num);
            if (s.equals("unmark")) {
                t.unmark();
                storage.save(tasks.all());
                return "OK, I've marked this task as not done yet:\n  " + t.toString();
            } else {
                t.mark();
                storage.save(tasks.all());
                return "Nice! I've marked this task as done:\n  " + t.toString();
            }
        } else if (input.contains("delete")) {
            String[] parts = input.split(" ", 2);
            int num = Integer.parseInt(parts[1]) - 1; // Convert from 1-based to 0-based
            if (num < 0 || num >= tasks.size()) {
                return "Ura? Invalid task number.";
            }
            Task removed = tasks.delete(num + 1); // delete() expects 1-based index
            storage.save(tasks.all());
            return "Noted. I've removed this task:\n  " + removed.toString() + 
                   "\nNow you have " + tasks.size() + " task(s) in the list.";
        } else if (input.contains("todo") || input.contains("deadline") || input.contains("event")){
            if (input.contains("todo")) {
                if (input.trim().contains(" ")) {
                    String[] parts = input.split(" ", 2);
                    Task task = new ToDos(parts[1], false);
                    tasks.add(task);
                    storage.save(tasks.all());
                    return "Got it. I've added this task:\n  " + task.toString() + 
                           "\nNow you have " + tasks.size() + " task(s) in the list.";
                } else {
                    return "Ura? (todo must be followed by a description)";
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
                    return "Got it. I've added this task:\n  " + task.toString() + 
                           "\nNow you have " + tasks.size() + " task(s) in the list.";
                } else {
                    return "Ura? (deadline must be followed by a description and a /by)";
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
                    return "Got it. I've added this task:\n  " + task.toString() + 
                           "\nNow you have " + tasks.size() + " task(s) in the list.";
                } else {
                    return "Ura? (event must be followed by a description and a /from and /to)";
                }
            }
        }

        return "Ura? I don't understand that command. Try: list, todo, deadline, event, mark, unmark, delete, find, or bye";
    }

}


