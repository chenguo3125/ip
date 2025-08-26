public class ToDos extends Task {
    public ToDos(String title, boolean done) {
        super(title, done);
    }

    @Override
    public String type() {
        return "T";
    }

    public String[] extra() {
        return new String[0];
    }

    public String toString() {
        return "[T]" + super.toString();
    }
}
