/**
 * A chatbot called Usagi.
 */

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Usagi {
    public static void main(String[] args) {
        Storage storage = new Storage();
        List<Task> todo = storage.load();
        Scanner scanner = new Scanner(System.in);
        String hi = "Ura! Ura-yaha-yiaha!\n" +
                "Una-una?\n";
        System.out.println(hi);

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("Yiaaa~~~~haaa~~~~~(bye~~~~~)");
                break;
            } else if (input.equals("list")) {
                int x = 1;
                System.out.println("Yiaha~(here is the list)\nlist:");
                while (x <= todo.size()) {
                    System.out.println(x +". " + todo.get(x - 1).toString());
                    x++;
                }
            } else if (input.contains("mark")) {
                String[] parts = input.split(" ");
                String s = parts[0];
                int num = Integer.parseInt(parts[1]);
                Task t = todo.get(num - 1);
                if (s.equals("unmark")) {
                    t.unmark();
                    System.out.println("iya-ha! (unmarked!)");
                    System.out.println(t.toString());
                } else {
                    t.mark();
                    System.out.println("iyaaa! (marked!)");
                    System.out.println(t.toString());
                }
            } else if (input.contains("delete")) {
                String[] parts = input.split(" ", 2);
                int num = Integer.parseInt(parts[1]);
                Task task = todo.get(num - 1);
                todo.remove(task);
                storage.save(todo);
                System.out.println(task.toString() + "\nUra! (remove! now u have " + todo.size() + " tasks in the list.)");

            } else if (input.startsWith("on ")) {
                String raw = input.substring(3).trim();
                try {
                    LocalDate date = Task.parseDateFlexible(raw);
                    List<Task> matches = new ArrayList<>();
                    for (Task task : todo) {
                        if (task instanceof Deadline) {
                            LocalDate d = ((Deadline) task).by.toLocalDate();
                            if (d.equals(date)) matches.add(task);
                        } else if (task instanceof Event) {
                            LocalDateTime f = ((Event) task).from;
                            LocalDateTime t = ((Event) task).to;
                            LocalDate start = f.toLocalDate();
                            LocalDate end = t.toLocalDate();
                            if ((start.isBefore(date) || start.equals(date)) && (end.isAfter(date) || end.equals(date))) {
                                matches.add(task);
                            }
                        }
                    }
                    System.out.println("Yiaha~(tasks on that date)\nlist:");
                    int i = 1;
                    for (Task m : matches) {
                        System.out.println(i + ". " + m.toString());
                        i++;
                    }
                    if (matches.isEmpty()) {
                        System.out.println("(none)");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Ura? (bad date format; try yyyy-MM-dd or d/M/yyyy)");
                }
            } else if (input.contains("todo") || input.contains("deadline") || input.contains("event")){
                if (input.contains("todo")) {
                    if (input.trim().contains(" ")) {
                        String[] parts = input.split(" ", 2);
                        Task task = new ToDos(parts[1], false);
                        todo.add(task);
                        storage.save(todo);
                        System.out.println(task.toString() + "\nUra! (now u have " + todo.size() + " tasks in the list.)");
                    } else {
                        System.out.println("Ura? (todo must be followed by a description)");
                    }
                } else if (input.contains("deadline")) {
                    if (input.trim().contains(" ")) {
                        String[] parts = input.substring(9).split("/by", 2);
                        String title = parts[0].trim();
                        String raw = parts.length > 1 ? parts[1].trim() : "";

                        // Accept multiple formats: yyyy-MM-dd[ HHmm] or d/M/yyyy[ HHmm]
                        LocalDateTime by = Task.parseDateTimeFlexible(raw);
                        Task task = new Deadline(title, false, by);
                        todo.add(task);
                        storage.save(todo);
                        System.out.println(task.toString() + "\nUra! (now u have " + todo.size() + " tasks in the list.)");
                    } else {
                        System.out.println("Ura? (deadline must be followed by a description and a /by)");
                    }
                } else {
                    if (input.trim().contains(" ")) {

                        String[] parts = input.split(" /from ");
                        String[] p1 = parts[0].split(" ", 2);
                        String[] p2 = parts[1].split(" /to ");

                        LocalDateTime from = Task.parseDateTimeFlexible(p2[0].trim());
                        LocalDateTime to   = Task.parseDateTimeFlexible(p2[1].trim());

                        Task task = new Event(p1[1], false, from, to);
                        todo.add(task);
                        storage.save(todo);

                        System.out.println(task.toString() + "\nUra! (now u have " + todo.size() + " tasks in the list.)");
                    } else {
                        System.out.println("Ura? (deadline must be followed by a description and a /from and /to)");
                    }
                }
            } else {
                System.out.println("Ura? (Usagi cannot understand ur gibberish)");
            }
        }
    }
}