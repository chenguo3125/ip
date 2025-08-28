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

public class Storage {
    /**
     * Candidate file paths for storing data.
     * <p>
     * The first path corresponds to {@code ./data/usagi.txt} (from project root).
     * The second path corresponds to {@code ../data/usagi.txt} (useful when running
     * from a subfolder such as {@code text-ui-test}).
     * </p>
     */
    private static final List<Path> CANDIDATES = List.of(
            Path.of("data", "usagi.txt"),
            Path.of("..", "data", "usagi.txt")
    );

    /**
     * Selects a path to load tasks from.
     * <p>
     * Prefers the first candidate path that exists on disk.
     * If none exist, defaults to the first candidate ({@code ./data/usagi.txt}).
     * </p>
     *
     * @return the chosen path for loading tasks
     */
    private Path pickLoadPath() {
        for (Path p : CANDIDATES) {
            if (Files.exists(p)) return p;
        }
        return CANDIDATES.get(0);
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
    List<Task> load() {
        Path p = pickLoadPath();
        if (!Files.exists(p)) return new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            List<Task> tasks = new ArrayList<>();
            for (String line : lines) {
                String s = line.strip();
                if (s.isEmpty()) continue;
                tasks.add(Task.fromLine(s));
            }
            return tasks;
        } catch (IOException e) {
            System.err.println("Load failed from " + p + ": " + e.getMessage());
            return new ArrayList<>();
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
    void save(List<Task> tasks) {
        for (Path target : CANDIDATES) {
            try {
                ensureParentDirs(target);
                List<String> lines = tasks.stream()
                        .map(Task::toLine)
                        .collect(Collectors.toList());
                Files.write(target, lines,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
                return; // success
            } catch (IOException e) {
                // try next candidate
            }
        }
        System.err.println("Save failed: could not write to any candidate path.");
    }
}
