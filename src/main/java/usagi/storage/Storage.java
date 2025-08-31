package usagi.storage;

/**
 * Handles the persistence of tasks to and from the hard disk.
 * 
 * The Storage class is responsible for:
 * <ul>
 *     <li>Loading tasks from a text file into memory when the chatbot starts</li>
 *     <li>Saving tasks from memory into a text file whenever changes occur</li>
 * </ul>
 * 
 * The file is stored at the specified path. If the file or folder does not exist,
 * Storage will create them as needed.
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import usagi.task.Task;
import usagi.exception.UsagiException;

public class Storage {
    private final Path filePath;

    /**
     * Constructs a Storage instance with the specified file path.
     * 
     * @param filePath The path to the file where tasks will be stored
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Ensures that the parent directories of the given path exist.
     * Creates them if they do not already exist.
     *
     * @param p The file path whose parent directories should be checked
     * @return The same path p
     * @throws IOException If the directories cannot be created
     */
    private static Path ensureParentDirs(Path p) throws IOException {
        Path parent = p.getParent();
        if (parent != null) Files.createDirectories(parent);
        return p;
    }

    /**
     * Loads tasks from the storage file.
     * 
     * If the file does not exist, returns an empty list. Each non-empty line
     * is parsed into a Task using Task.fromLine(String).
     *
     * @return A list of tasks loaded from the storage file
     * @throws UsagiException If an error occurs during file reading
     */
    public List<Task> load() throws UsagiException {
        try {
            if (!Files.exists(filePath)) return new ArrayList<>();
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<Task> tasks = new ArrayList<>();
            for (String line : lines) {
                String s = line.strip();
                if (s.isEmpty()) continue;
                tasks.add(Task.fromLine(s));
            }
            return tasks;
        } catch (IOException e) {
            throw new UsagiException("Load failed from " + filePath, e);
        }
    }


    /**
     * Saves the given list of tasks to the storage file.
     * 
     * Each task is serialized into a line using Task.toLine().
     * The file is created if it doesn't exist, or overwritten if it does.
     *
     * @param tasks The list of tasks to save
     * @throws UsagiException If an error occurs during file writing
     */
    public void save(List<Task> tasks) throws UsagiException {
        try {
            ensureParentDirs(filePath);
            List<String> lines = tasks.stream()
                    .map(Task::toLine)
                    .collect(Collectors.toList());
            Files.write(filePath, lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UsagiException("Save failed to " + filePath, e);
        }
    }
}
