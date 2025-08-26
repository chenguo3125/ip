import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class Task {
    protected String title;
    protected Boolean done;
    abstract String type();                   // "T", "D", "E"
    abstract String[] extra();

    public Task(String title, boolean done) {
        this.title = title;
        this.done = done;
    }

    public void mark() {
        this.done = true;
    }

    public void unmark() {
        this.done = false;
    }

    String toLine() {
        List<String> parts = new ArrayList<>();
        parts.add(type());
        parts.add(done ? "1" : "0");
        parts.add(title);
        parts.addAll(Arrays.asList(extra()));  // append extra fields if any
        return String.join(" | ", parts);
    }


    static Task fromLine(String line) {
        String[] p = line.split("\\s*\\|\\s*"); // splits on ' | ' with spaces ok
        String t = p[0], done = p[1], desc = p[2];
        switch (t) {
            case "T": return new ToDos(desc, "1".equals(done));
            case "D": return new Deadline(desc, "1".equals(done), p[3]);
            case "E": return new Event(desc, "1".equals(done), p[3], p[4]);
            default: throw new IllegalArgumentException("Bad type: " + t);
        }
    }

    public String toString() {
        if (done) {
            return "[X] " + this.title;
        } else {
            return "[ ] " + this.title;
        }
    }
}
