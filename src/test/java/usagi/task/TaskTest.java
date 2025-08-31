package usagi.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TaskTest {

    @Test
    public void testParseDateFlexible_ISOFormat() {
        LocalDate result = Task.parseDateFlexible("2023-12-25");
        assertEquals(LocalDate.of(2023, 12, 25), result);
    }

    @Test
    public void testParseDateFlexible_DMYFormat() {
        LocalDate result = Task.parseDateFlexible("25/12/2023");
        assertEquals(LocalDate.of(2023, 12, 25), result);
    }

    @Test
    public void testParseDateFlexible_MDYFormat() {
        LocalDate result = Task.parseDateFlexible("12/25/2023");
        assertEquals(LocalDate.of(2023, 12, 25), result);
    }

    @Test
    public void testParseDateFlexible_InvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            Task.parseDateFlexible("invalid-date");
        });
    }

    @Test
    public void testParseDateFlexible_EmptyString() {
        assertThrows(IllegalArgumentException.class, () -> {
            Task.parseDateFlexible("");
        });
    }

    @Test
    public void testParseDateTimeFlexible_ISOFormat() {
        LocalDateTime result = Task.parseDateTimeFlexible("2023-12-25T14:30");
        assertEquals(LocalDateTime.of(2023, 12, 25, 14, 30), result);
    }

    @Test
    public void testParseDateTimeFlexible_DateWithTime() {
        LocalDateTime result = Task.parseDateTimeFlexible("2023-12-25 1430");
        assertEquals(LocalDateTime.of(2023, 12, 25, 14, 30), result);
    }

    @Test
    public void testParseDateTimeFlexible_DMYWithTime() {
        LocalDateTime result = Task.parseDateTimeFlexible("25/12/2023 1430");
        assertEquals(LocalDateTime.of(2023, 12, 25, 14, 30), result);
    }

    @Test
    public void testParseDateTimeFlexible_DateOnly() {
        LocalDateTime result = Task.parseDateTimeFlexible("2023-12-25");
        assertEquals(LocalDateTime.of(2023, 12, 25, 0, 0), result);
    }

    @Test
    public void testParseDateTimeFlexible_InvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            Task.parseDateTimeFlexible("invalid-datetime");
        });
    }

    @Test
    public void testFromLine_Todo() {
        String line = "T | 0 | read book";
        Task task = Task.fromLine(line);
        assertTrue(task instanceof ToDos);
        assertEquals("[T][ ] read book", task.toString());
        assertFalse(task.isDone);
    }

    @Test
    public void testFromLine_TodoCompleted() {
        String line = "T | 1 | read book";
        Task task = Task.fromLine(line);
        assertTrue(task instanceof ToDos);
        assertEquals("[T][X] read book", task.toString());
        assertTrue(task.isDone);
    }

    @Test
    public void testFromLine_Deadline() {
        String line = "D | 0 | return book | 2023-12-25";
        Task task = Task.fromLine(line);
        assertTrue(task instanceof Deadline);
        assertTrue(task.toString().contains("return book"));
        assertFalse(task.isDone);
        Deadline deadline = (Deadline) task;
        assertEquals(LocalDateTime.of(2023, 12, 25, 0, 0), deadline.by);
    }

    @Test
    public void testFromLine_Event() {
        String line = "E | 0 | team meeting | 2023-12-25T09:00 | 2023-12-25T10:00";
        Task task = Task.fromLine(line);
        assertTrue(task instanceof Event);
        assertTrue(task.toString().contains("team meeting"));
        assertFalse(task.isDone);
        Event event = (Event) task;
        assertEquals(LocalDateTime.of(2023, 12, 25, 9, 0), event.from);
        assertEquals(LocalDateTime.of(2023, 12, 25, 10, 0), event.to);
    }

    @Test
    public void testFromLine_InvalidType() {
        String line = "X | 0 | invalid task";
        assertThrows(IllegalArgumentException.class, () -> {
            Task.fromLine(line);
        });
    }

    @Test
    public void testToLine_Todo() {
        Task task = new ToDos("read book", false);
        String result = task.toLine();
        assertEquals("T | 0 | read book", result);
    }

    @Test
    public void testToLine_TodoCompleted() {
        Task task = new ToDos("read book", true);
        String result = task.toLine();
        assertEquals("T | 1 | read book", result);
    }

    @Test
    public void testToLine_Deadline() {
        Task task = new Deadline("return book", false, LocalDateTime.of(2023, 12, 25, 14, 30));
        String result = task.toLine();
        assertEquals("D | 0 | return book | 2023-12-25T14:30", result);
    }

    @Test
    public void testToLine_Event() {
        Task task = new Event("team meeting", false, 
            LocalDateTime.of(2023, 12, 25, 9, 0), 
            LocalDateTime.of(2023, 12, 25, 10, 0));
        String result = task.toLine();
        assertEquals("E | 0 | team meeting | 2023-12-25T09:00 | 2023-12-25T10:00", result);
    }

    @Test
    public void testMarkAndUnmark() {
        Task task = new ToDos("test task", false);
        assertFalse(task.isDone);
        
        task.mark();
        assertTrue(task.isDone);
        
        task.unmark();
        assertFalse(task.isDone);
    }

    @Test
    public void testToString_Todo() {
        Task task = new ToDos("read book", false);
        assertEquals("[T][ ] read book", task.toString());
        
        task.mark();
        assertEquals("[T][X] read book", task.toString());
    }

    @Test
    public void testToString_Deadline() {
        Task task = new Deadline("return book", false, LocalDateTime.of(2023, 12, 25, 14, 30));
        assertEquals("[D][ ] return book (by: Dec 25 2023 14:30)", task.toString());
    }

    @Test
    public void testToString_Event() {
        Task task = new Event("team meeting", false, 
            LocalDateTime.of(2023, 12, 25, 9, 0), 
            LocalDateTime.of(2023, 12, 25, 10, 0));
        assertEquals("[E][ ] team meeting (from: Dec 25 2023 09:00 to: Dec 25 2023 10:00)", task.toString());
    }
}
