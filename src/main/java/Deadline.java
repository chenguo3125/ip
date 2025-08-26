public class Deadline extends Task{
    String by;

    public Deadline(String title, boolean done, String by) {
        super(title, done);
        this.by = by;
    }

    @Override
    public String type() {
        return "D";
    }

    public String[] extra() {
        return new String[]{by};
    }

    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by + ")";
    }
}
