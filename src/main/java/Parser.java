import java.time.LocalDate;
import java.time.LocalDateTime;

public class Parser {
    public static boolean isExit(String input) {
        return input.equals("bye");
    }

    public static void handle(String input, TaskList tasks, Storage storage, Ui ui) throws UsagiException {
        if (input.equals("list")) {
            ui.showList(tasks.all());
            return;
        } else if (input.startsWith("on ")) {
            String raw = input.substring(3).trim();
            LocalDate date = Task.parseDateFlexible(raw);
            ui.showOnDate(tasks.tasksOn(date));
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


