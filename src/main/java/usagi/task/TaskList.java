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
        assert tasks != null : "Task list cannot be null";
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
        assert task != null : "Task cannot be null";
        tasks.add(task);
    }

    /**
     * Deletes a task at the specified index (1-based).
     * 
     * @param indexOneBased The 1-based index of the task to delete
     * @return The deleted task
     */
    public Task delete(int indexOneBased) {
        if (indexOneBased <= 0) {
            throw new IndexOutOfBoundsException("Index must be positive (1-based), got: " + indexOneBased);
        }
        if (indexOneBased > tasks.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds, got: " + indexOneBased + ", size: " + tasks.size());
        }
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
        if (indexOneBased <= 0) {
            throw new IndexOutOfBoundsException("Index must be positive (1-based), got: " + indexOneBased);
        }
        if (indexOneBased > tasks.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds, got: " + indexOneBased + ", size: " + tasks.size());
        }
        return tasks.get(indexOneBased - 1);
    }

    /**
     * Gets a task at the specified index (0-based).
     * 
     * @param indexZeroBased The 0-based index of the task to retrieve
     * @return The task at the specified index
     */
    public Task getByIndex(int indexZeroBased) {
        assert indexZeroBased >= 0 : "Index must be non-negative (0-based), got: " + indexZeroBased;
        assert indexZeroBased < tasks.size() : "Index out of bounds, got: " + indexZeroBased + ", size: " + tasks.size();
        return tasks.get(indexZeroBased);
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
        assert date != null : "Date cannot be null";
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

    /**
     * Returns all tasks that contain the specified keyword in their description.
     * 
     * The search is case-insensitive and matches any task whose title
     * contains the keyword as a substring.
     * 
     * @param keyword The keyword to search for
     * @return A list of tasks containing the keyword in their description
     */
    public List<Task> find(String keyword) {
        assert keyword != null : "Keyword cannot be null";
        assert !keyword.trim().isEmpty() : "Keyword cannot be empty";
        List<Task> matches = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.title.toLowerCase().contains(lowerKeyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}


