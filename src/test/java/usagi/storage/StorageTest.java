package usagi.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import usagi.task.Task;
import usagi.task.ToDos;
import usagi.task.Deadline;
import usagi.task.Event;
import usagi.exception.UsagiException;

import java.nio.file.Path;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

public class StorageTest {

    @TempDir
    Path tempDir;
    
    private Storage storage;
    private Path testFilePath;

    @BeforeEach
    public void setUp() {
        testFilePath = tempDir.resolve("test-tasks.txt");
        storage = new Storage(testFilePath.toString());
    }

    @Test
    public void testLoad_EmptyFile() throws Exception {
        // Create empty file
        Files.createFile(testFilePath);
        
        List<Task> tasks = storage.load();
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testLoad_NonExistentFile() throws Exception {
        List<Task> tasks = storage.load();
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testLoad_SingleTodo() throws Exception {
        String content = "T | 0 | read book\n";
        Files.write(testFilePath, content.getBytes());
        
        List<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        
        Task task = tasks.get(0);
        assertTrue(task instanceof ToDos);
        assertTrue(task.toString().contains("read book"));
        // Test completion status through toString representation
        assertTrue(task.toString().contains("[ ]"));
    }

    @Test
    public void testLoad_SingleDeadline() throws Exception {
        String content = "D | 0 | return book | 2023-12-25T14:30\n";
        Files.write(testFilePath, content.getBytes());
        
        List<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        
        Task task = tasks.get(0);
        assertTrue(task instanceof Deadline);
        assertTrue(task.toString().contains("return book"));
        // Test completion status through toString representation
        assertTrue(task.toString().contains("[ ]"));
    }

    @Test
    public void testLoad_SingleEvent() throws Exception {
        String content = "E | 0 | team meeting | 2023-12-25T09:00 | 2023-12-25T10:00\n";
        Files.write(testFilePath, content.getBytes());
        
        List<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        
        Task task = tasks.get(0);
        assertTrue(task instanceof Event);
        assertTrue(task.toString().contains("team meeting"));
        // Test completion status through toString representation
        assertTrue(task.toString().contains("[ ]"));
    }

    @Test
    public void testLoad_MultipleTasks() throws Exception {
        String content = "T | 0 | read book\n" +
                        "D | 1 | return book | 2023-12-25T14:30\n" +
                        "E | 0 | team meeting | 2023-12-25T09:00 | 2023-12-25T10:00\n";
        Files.write(testFilePath, content.getBytes());
        
        List<Task> tasks = storage.load();
        assertEquals(3, tasks.size());
        
        // Check first task (todo)
        Task todo = tasks.get(0);
        assertTrue(todo instanceof ToDos);
        assertTrue(todo.toString().contains("read book"));
        assertTrue(todo.toString().contains("[ ]"));
        
        // Check second task (deadline)
        Task deadline = tasks.get(1);
        assertTrue(deadline instanceof Deadline);
        assertTrue(deadline.toString().contains("return book"));
        assertTrue(deadline.toString().contains("[X]"));
        
        // Check third task (event)
        Task event = tasks.get(2);
        assertTrue(event instanceof Event);
        assertTrue(event.toString().contains("team meeting"));
        assertTrue(event.toString().contains("[ ]"));
    }

    @Test
    public void testLoad_WithEmptyLines() throws Exception {
        String content = "T | 0 | read book\n\n" +
                        "D | 1 | return book | 2023-12-25T14:30\n" +
                        "  \n" +
                        "E | 0 | team meeting | 2023-12-25T09:00 | 2023-12-25T10:00\n";
        Files.write(testFilePath, content.getBytes());
        
        List<Task> tasks = storage.load();
        assertEquals(3, tasks.size());
    }

    @Test
    public void testLoad_InvalidTaskType() throws Exception {
        String content = "X | 0 | invalid task\n";
        Files.write(testFilePath, content.getBytes());
        
        assertThrows(IllegalArgumentException.class, () -> {
            storage.load();
        });
    }

    @Test
    public void testLoad_MalformedLine() throws Exception {
        String content = "T | 0 | read book\n" +
                        "D | 0 | incomplete line\n" +
                        "E | 0 | team meeting | 2023-12-25T09:00 | 2023-12-25T10:00\n";
        Files.write(testFilePath, content.getBytes());
        
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            storage.load();
        });
    }

    @Test
    public void testSave_EmptyList() throws Exception {
        List<Task> tasks = List.of();
        storage.save(tasks);
        
        assertTrue(Files.exists(testFilePath));
        String content = Files.readString(testFilePath);
        assertTrue(content.isEmpty());
    }

    @Test
    public void testSave_SingleTodo() throws Exception {
        Task todo = new ToDos("read book", false);
        List<Task> tasks = List.of(todo);
        
        storage.save(tasks);
        
        String content = Files.readString(testFilePath);
        assertEquals("T | 0 | read book\n", content);
    }

    @Test
    public void testSave_SingleDeadline() throws Exception {
        Task deadline = new Deadline("return book", false, LocalDateTime.of(2023, 12, 25, 14, 30));
        List<Task> tasks = List.of(deadline);
        
        storage.save(tasks);
        
        String content = Files.readString(testFilePath);
        assertEquals("D | 0 | return book | 2023-12-25T14:30\n", content);
    }

    @Test
    public void testSave_SingleEvent() throws Exception {
        Task event = new Event("team meeting", false, 
            LocalDateTime.of(2023, 12, 25, 9, 0), 
            LocalDateTime.of(2023, 12, 25, 10, 0));
        List<Task> tasks = List.of(event);
        
        storage.save(tasks);
        
        String content = Files.readString(testFilePath);
        assertEquals("E | 0 | team meeting | 2023-12-25T09:00 | 2023-12-25T10:00\n", content);
    }

    @Test
    public void testSave_MultipleTasks() throws Exception {
        Task todo = new ToDos("read book", false);
        Task deadline = new Deadline("return book", true, LocalDateTime.of(2023, 12, 25, 14, 30));
        Task event = new Event("team meeting", false, 
            LocalDateTime.of(2023, 12, 25, 9, 0), 
            LocalDateTime.of(2023, 12, 25, 10, 0));
        
        List<Task> tasks = List.of(todo, deadline, event);
        storage.save(tasks);
        
        String content = Files.readString(testFilePath);
        String expected = "T | 0 | read book\n" +
                         "D | 1 | return book | 2023-12-25T14:30\n" +
                         "E | 0 | team meeting | 2023-12-25T09:00 | 2023-12-25T10:00\n";
        assertEquals(expected, content);
    }

    @Test
    public void testSave_OverwritesExistingFile() throws Exception {
        // Create initial content
        String initialContent = "T | 0 | old task\n";
        Files.write(testFilePath, initialContent.getBytes());
        
        // Save new content
        Task newTask = new ToDos("new task", false);
        List<Task> tasks = List.of(newTask);
        storage.save(tasks);
        
        String content = Files.readString(testFilePath);
        assertEquals("T | 0 | new task\n", content);
    }

    @Test
    public void testLoadAndSave_RoundTrip() throws Exception {
        // Create original tasks
        Task todo = new ToDos("read book", false);
        Task deadline = new Deadline("return book", true, LocalDateTime.of(2023, 12, 25, 14, 30));
        Task event = new Event("team meeting", false, 
            LocalDateTime.of(2023, 12, 25, 9, 0), 
            LocalDateTime.of(2023, 12, 25, 10, 0));
        
        List<Task> originalTasks = List.of(todo, deadline, event);
        
        // Save tasks
        storage.save(originalTasks);
        
        // Load tasks back
        List<Task> loadedTasks = storage.load();
        
        // Verify they match
        assertEquals(3, loadedTasks.size());
        
        Task loadedTodo = loadedTasks.get(0);
        assertTrue(loadedTodo instanceof ToDos);
        assertTrue(loadedTodo.toString().contains("read book"));
        assertTrue(loadedTodo.toString().contains("[ ]"));
        
        Task loadedDeadline = loadedTasks.get(1);
        assertTrue(loadedDeadline instanceof Deadline);
        assertTrue(loadedDeadline.toString().contains("return book"));
        assertTrue(loadedDeadline.toString().contains("[X]"));
        
        Task loadedEvent = loadedTasks.get(2);
        assertTrue(loadedEvent instanceof Event);
        assertTrue(loadedEvent.toString().contains("team meeting"));
        assertTrue(loadedEvent.toString().contains("[ ]"));
    }

    @Test
    public void testConstructor_WithPath() {
        Storage customStorage = new Storage("/custom/path/tasks.txt");
        assertNotNull(customStorage);
    }
}
