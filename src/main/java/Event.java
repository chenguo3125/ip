public class Event extends Task{
    String from;
    String to;

    public Event(String title, boolean done, String from, String to) {
        super(title, done);
        this.from = from;
        this.to = to;
    }

    @Override
    public String type() {
        return "E";
    }

    public String[] extra() {
        return new String[]{from, to};
    }

    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from + " to: " + this.to + ")";
    }
}
