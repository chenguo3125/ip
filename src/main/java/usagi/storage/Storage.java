package usagi.storage;

/**
 * Handles the persistence of tasks to and from the hard disk.
 * <p>
 * The {@code Storage} class is responsible for:
 * <ul>
 *     <li>Loading tasks from a text file into memory when the chatbot starts.</li>
 *     <li>Saving tasks from memory into a text file whenever changes occur.</li>
 * </ul>
 * The file is stored in a {@code ./data/} folder relative to the project root.
 * If the file or folder does not exist, {@code Storage} will create them as needed.
 * </p>
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

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Ensures that the parent directories of the given path exist.
     * Creates them if they do not already exist.
     *
     * @param p the file path whose parent directories should be checked
     * @return the same path {@code p}
     * @throws IOException if the directories cannot be created
     */
    private static Path ensureParentDirs(Path p) throws IOException {
        Path parent = p.getParent();
        if (parent != null) Files.createDirectories(parent);
        return p;
    }

    /**
     * Loads tasks from the storage file.
     * <p>
     * If the file does not exist, returns an empty list. Each non-empty line
     * is parsed into a {@link Task} using {@link Task#fromLine(String)}.
     * </p>
     *
     * @return a list of tasks loaded from the storage file
     */
    List<Task> load() throws UsagiException {
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
     * <p>
     * Attempts to write to each candidate path in order, until successful.
     * Each task is serialized into a line using {@link Task#toLine()}.
     * </p>
     *
     * @param tasks the list of tasks to save
     */
    void save(List<Task> tasks) throws UsagiException {
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
