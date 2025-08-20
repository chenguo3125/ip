public class Task {
    protected String title;
    protected Boolean done;

    public Task(String title) {
        this.title = title;
        this.done = false;
    }

    public void mark() {
        this.done = true;
    }

    public void unmark() {
        this.done = false;
    }

    public String toString() {
        if (done) {
            return "[X] " + this.title;
        } else {
            return "[ ] " + this.title;
        }
    }
}
