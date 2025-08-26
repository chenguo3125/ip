import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class Storage {
    private static final List<Path> CANDIDATES = List.of(
            Path.of("data", "usagi.txt"),
            Path.of("..", "data", "usagi.txt")
    );

    private Path pickLoadPath() {
        for (Path p : CANDIDATES) {
            if (Files.exists(p)) return p;
        }
        return CANDIDATES.get(0);
    }

    private static Path ensureParentDirs(Path p) throws IOException {
        Path parent = p.getParent();
        if (parent != null) Files.createDirectories(parent);
        return p;
    }

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

    void save(List<Task> tasks) {
        // Always write to the “project root” candidate; fall back to the second if needed.
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
