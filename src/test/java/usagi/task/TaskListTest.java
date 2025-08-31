package usagi.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TaskListTest {

    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void testConstructor_Empty() {
        TaskList emptyList = new TaskList();
        assertEquals(0, emptyList.size());
        assertTrue(emptyList.all().isEmpty());
    }

    @Test
    public void testConstructor_WithTasks() {
        Task todo = new ToDos("read book", false);
        Task deadline = new Deadline("return book", false, LocalDateTime.of(2023, 12, 25, 14, 30));
        
        List<Task> tasks = List.of(todo, deadline);
        TaskList newList = new TaskList(tasks);
        
        assertEquals(2, newList.size());
        assertEquals(2, newList.all().size());
        assertTrue(newList.all().contains(todo));
        assertTrue(newList.all().contains(deadline));
    }

    @Test
    public void testAdd() {
        Task todo = new ToDos("read book", false);
        taskList.add(todo);
        
        assertEquals(1, taskList.size());
        assertEquals(todo, taskList.get(1));
        assertTrue(taskList.all().contains(todo));
    }

    @Test
    public void testAdd_MultipleTasks() {
        Task todo1 = new ToDos("read book", false);
        Task todo2 = new ToDos("write report", false);
        Task deadline = new Deadline("return book", false, LocalDateTime.of(2023, 12, 25, 14, 30));
        
        taskList.add(todo1);
        taskList.add(todo2);
        taskList.add(deadline);
        
        assertEquals(3, taskList.size());
        assertEquals(todo1, taskList.get(1));
        assertEquals(todo2, taskList.get(2));
        assertEquals(deadline, taskList.get(3));
    }

    @Test
    public void testGet_ValidIndex() {
        Task todo = new ToDos("read book", false);
        taskList.add(todo);
        
        Task retrieved = taskList.get(1);
        assertEquals(todo, retrieved);
    }

    @Test
    public void testGet_InvalidIndex() {
        Task todo = new ToDos("read book", false);
        taskList.add(todo);
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.get(2);
        });
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.get(0);
        });
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.get(-1);
        });
    }

    @Test
    public void testDelete_ValidIndex() {
        Task todo1 = new ToDos("read book", false);
        Task todo2 = new ToDos("write report", false);
        taskList.add(todo1);
        taskList.add(todo2);
        
        Task deleted = taskList.delete(1);
        assertEquals(todo1, deleted);
        assertEquals(1, taskList.size());
        assertEquals(todo2, taskList.get(1));
    }

    @Test
    public void testDelete_LastTask() {
        Task todo = new ToDos("read book", false);
        taskList.add(todo);
        
        Task deleted = taskList.delete(1);
        assertEquals(todo, deleted);
        assertEquals(0, taskList.size());
        assertTrue(taskList.all().isEmpty());
    }

    @Test
    public void testDelete_InvalidIndex() {
        Task todo = new ToDos("read book", false);
        taskList.add(todo);
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.delete(2);
        });
        
        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.delete(0);
        });
    }

    @Test
    public void testSize() {
        assertEquals(0, taskList.size());
        
        taskList.add(new ToDos("read book", false));
        assertEquals(1, taskList.size());
        
        taskList.add(new ToDos("write report", false));
        assertEquals(2, taskList.size());
        
        taskList.delete(1);
        assertEquals(1, taskList.size());
    }

    @Test
    public void testAll() {
        Task todo1 = new ToDos("read book", false);
        Task todo2 = new ToDos("write report", false);
        
        taskList.add(todo1);
        taskList.add(todo2);
        
        List<Task> allTasks = taskList.all();
        assertEquals(2, allTasks.size());
        assertTrue(allTasks.contains(todo1));
        assertTrue(allTasks.contains(todo2));
        
        // Test that the returned list is the actual list (not a copy)
        allTasks.clear();
        assertEquals(0, taskList.size());
        assertEquals(0, taskList.all().size());
    }

    @Test
    public void testTasksOn_DeadlineExactMatch() {
        LocalDate targetDate = LocalDate.of(2023, 12, 25);
        Task deadline = new Deadline("return book", false, LocalDateTime.of(2023, 12, 25, 14, 30));
        taskList.add(deadline);
        
        List<Task> tasksOnDate = taskList.tasksOn(targetDate);
        assertEquals(1, tasksOnDate.size());
        assertEquals(deadline, tasksOnDate.get(0));
    }

    @Test
    public void testTasksOn_DeadlineNoMatch() {
        LocalDate targetDate = LocalDate.of(2023, 12, 25);
        Task deadline = new Deadline("return book", false, LocalDateTime.of(2023, 12, 26, 14, 30));
        taskList.add(deadline);
        
        List<Task> tasksOnDate = taskList.tasksOn(targetDate);
        assertTrue(tasksOnDate.isEmpty());
    }

    @Test
    public void testTasksOn_EventSpansDate() {
        LocalDate targetDate = LocalDate.of(2023, 12, 25);
        Task event = new Event("team meeting", false, 
            LocalDateTime.of(2023, 12, 24, 9, 0), 
            LocalDateTime.of(2023, 12, 26, 10, 0));
        taskList.add(event);
        
        List<Task> tasksOnDate = taskList.tasksOn(targetDate);
        assertEquals(1, tasksOnDate.size());
        assertEquals(event, tasksOnDate.get(0));
    }

    @Test
    public void testTasksOn_EventStartsOnDate() {
        LocalDate targetDate = LocalDate.of(2023, 12, 25);
        Task event = new Event("team meeting", false, 
            LocalDateTime.of(2023, 12, 25, 9, 0), 
            LocalDateTime.of(2023, 12, 26, 10, 0));
        taskList.add(event);
        
        List<Task> tasksOnDate = taskList.tasksOn(targetDate);
        assertEquals(1, tasksOnDate.size());
        assertEquals(event, tasksOnDate.get(0));
    }

    @Test
    public void testTasksOn_EventEndsOnDate() {
        LocalDate targetDate = LocalDate.of(2023, 12, 25);
        Task event = new Event("team meeting", false, 
            LocalDateTime.of(2023, 12, 24, 9, 0), 
            LocalDateTime.of(2023, 12, 25, 10, 0));
        taskList.add(event);
        
        List<Task> tasksOnDate = taskList.tasksOn(targetDate);
        assertEquals(1, tasksOnDate.size());
        assertEquals(event, tasksOnDate.get(0));
    }

    @Test
    public void testTasksOn_EventNoOverlap() {
        LocalDate targetDate = LocalDate.of(2023, 12, 25);
        Task event = new Event("team meeting", false, 
            LocalDateTime.of(2023, 12, 26, 9, 0), 
            LocalDateTime.of(2023, 12, 27, 10, 0));
        taskList.add(event);
        
        List<Task> tasksOnDate = taskList.tasksOn(targetDate);
        assertTrue(tasksOnDate.isEmpty());
    }

    @Test
    public void testTasksOn_TodoIgnored() {
        LocalDate targetDate = LocalDate.of(2023, 12, 25);
        Task todo = new ToDos("read book", false);
        taskList.add(todo);
        
        List<Task> tasksOnDate = taskList.tasksOn(targetDate);
        assertTrue(tasksOnDate.isEmpty());
    }

    @Test
    public void testTasksOn_MixedTasks() {
        LocalDate targetDate = LocalDate.of(2023, 12, 25);
        
        Task todo = new ToDos("read book", false);
        Task deadline = new Deadline("return book", false, LocalDateTime.of(2023, 12, 25, 14, 30));
        Task event = new Event("team meeting", false, 
            LocalDateTime.of(2023, 12, 25, 9, 0), 
            LocalDateTime.of(2023, 12, 25, 10, 0));
        Task otherDeadline = new Deadline("other task", false, LocalDateTime.of(2023, 12, 26, 14, 30));
        
        taskList.add(todo);
        taskList.add(deadline);
        taskList.add(event);
        taskList.add(otherDeadline);
        
        List<Task> tasksOnDate = taskList.tasksOn(targetDate);
        assertEquals(2, tasksOnDate.size());
        assertTrue(tasksOnDate.contains(deadline));
        assertTrue(tasksOnDate.contains(event));
        assertFalse(tasksOnDate.contains(todo));
        assertFalse(tasksOnDate.contains(otherDeadline));
    }

    @Test
    public void testTasksOn_EmptyList() {
        LocalDate targetDate = LocalDate.of(2023, 12, 25);
        List<Task> tasksOnDate = taskList.tasksOn(targetDate);
        assertTrue(tasksOnDate.isEmpty());
    }
}
