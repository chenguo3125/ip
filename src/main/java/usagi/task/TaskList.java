package usagi.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of tasks with operations for adding, removing,
 * and querying tasks based on various criteria.
 * 
 * This class provides a centralized interface for task management,
 * including date-based filtering and basic CRUD operations.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a task list with the specified initial tasks.
     * 
     * @param tasks The initial list of tasks to include
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns all tasks in the list.
     * 
     * @return A list containing all tasks
     */
    public List<Task> all() {
        return tasks;
    }

    /**
     * Adds a task to the list.
     * 
     * @param task The task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes a task at the specified index (1-based).
     * 
     * @param indexOneBased The 1-based index of the task to delete
     * @return The deleted task
     */
    public Task delete(int indexOneBased) {
        Task t = tasks.get(indexOneBased - 1);
        tasks.remove(t);
        return t;
    }

    /**
     * Gets a task at the specified index (1-based).
     * 
     * @param indexOneBased The 1-based index of the task to retrieve
     * @return The task at the specified index
     */
    public Task get(int indexOneBased) {
        return tasks.get(indexOneBased - 1);
    }

    /**
     * Returns the number of tasks in the list.
     * 
     * @return The size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns all tasks that occur on the specified date.
     * 
     * For deadlines, returns tasks that are due on the exact date.
     * For events, returns tasks that span the specified date.
     * Todo tasks are ignored as they have no date association.
     * 
     * @param date The date to filter tasks by
     * @return A list of tasks occurring on the specified date
     */
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


