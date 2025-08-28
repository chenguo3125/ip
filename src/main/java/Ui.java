import java.util.List;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        String hi = "Ura! Ura-yaha-yiaha!\n" +
                "Una-una?\n";
        System.out.println(hi);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine(String s) {
        System.out.println(s);
    }

    public void showLoadingError() {
        System.out.println("Ura? (failed to load tasks from file)");
    }

    public void showList(List<Task> tasks) {
        System.out.println("Yiaha~(here is the list)\nlist:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }
    }

    public void showAdded(Task task, int total) {
        System.out.println(task.toString() + "\nUra! (now u have " + total + " tasks in the list.)");
    }

    public void showDeleted(Task task, int total) {
        System.out.println(task.toString() + "\nUra! (remove! now u have " + total + " tasks in the list.)");
    }

    public void showMarked(Task t) {
        System.out.println("iyaaa! (marked!)\n" + t.toString());
    }

    public void showUnmarked(Task t) {
        System.out.println("iya-ha! (unmarked!)\n" + t.toString());
    }

    public void showOnDate(List<Task> tasks) {
        System.out.println("Yiaha~(tasks on that date)\nlist:");
        if (tasks.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }
    }

    public void showBye() {
        System.out.println("Yiaaa~~~~haaa~~~~~(bye~~~~~)");
    }

    public void showUnknown() {
        System.out.println("Ura? (Usagi cannot understand ur gibberish)");
    }
}


