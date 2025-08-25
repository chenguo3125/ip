import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Usagi {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String hi = "Ura! Ura-yaha-yiaha!\n" +
                "Una-una?\n";
        System.out.println(hi);
        List<Task> todo = new ArrayList<>();

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("Yiaaa~~~~haaa~~~~~(bye~~~~~)");
                break;
            } else if (input.equals("list")) {
                int x = 1;
                System.out.println("Yiaha~(here is the list)\nlist:");
                while (x <= todo.size()) {
                    System.out.println(x +". " + todo.get(x - 1).toString());
                    x++;
                }
            } else if (input.contains("mark")) {
                String[] parts = input.split(" ");
                String s = parts[0];
                int num = Integer.parseInt(parts[1]);
                Task t = todo.get(num - 1);
                if (s.equals("unmark")) {
                    t.unmark();
                    System.out.println("iya-ha! (unmarked!)");
                    System.out.println(t.toString());
                } else {
                    t.mark();
                    System.out.println("iyaaa! (marked!)");
                    System.out.println(t.toString());
                }
            } else {
                if (input.contains("todo")) {
                    String[] parts = input.split(" ", 2);
                    Task task = new ToDos(parts[1]);
                    todo.add(task);
                } else if (input.contains("deadline")) {
                    String[] parts = input.split(" /by ");
                    String[] p = parts[0].split(" ", 2);
                    Task task = new Deadline(p[1], parts[1]);
                    todo.add(task);
                } else if (input.contains("event")) {
                    String[] parts = input.split(" /from ");
                    String[] p1 = parts[0].split(" ", 2);
                    String[] p2 = parts[1].split(" /to ");
                    Task task = new Event(p1[1], p2[0], p2[1]);
                    todo.add(task);
                } else {
                    System.out.println("Ura? (Usagi cant understand the gibberish u r talking about?");
                }
                System.out.println(todo.get(todo.size() - 1).toString() + "\nUra! (now u have " + todo.size() + " tasks in the list.)");
            }
        }
    }
}
