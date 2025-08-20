import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Milly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String hi = " Hello! I'm [YOUR CHATBOT NAME]\n" +
                " What can I do for you?\n";
        System.out.println(hi);
        List<Task> todo = new ArrayList<>();

        while (true) {
            System.out.println("You: ");
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("Milly: \nBye~ See u soon :DD");
                break;
            } else if (input.equals("list")) {
                int x = 1;
                System.out.println("list: \n");
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
                    System.out.println("okk I have unmarked it :D");
                    System.out.println(t.toString());
                } else {
                    t.mark();
                    System.out.println("okk I have marked it :D");
                    System.out.println(t.toString());
                }
            } else {
                System.out.println("Milly: \n" + "added: " + input);
                Task task = new Task(input);
                todo.add(task);
            }
        }
    }
}
