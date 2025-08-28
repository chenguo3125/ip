package usagi.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public List<Task> all() {
        return tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int indexOneBased) {
        Task t = tasks.get(indexOneBased - 1);
        tasks.remove(t);
        return t;
    }

    public Task get(int indexOneBased) {
        return tasks.get(indexOneBased - 1);
    }

    public int size() {
        return tasks.size();
    }

    public List<Task> tasksOn(LocalDate date) {
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
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
        return matches;
    }
}


