import java.util.Scanner;

public class Milly {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String hi = " Hello! I'm [YOUR CHATBOT NAME]\n" +
                " What can I do for you?\n";
        System.out.println(hi);

        while (true) {
            System.out.println("You: ");
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("Milly: \nBye~ See u soon :DD");
                break;
            } else {
                System.out.println("Milly: \n" + input);
            }
        }
    }
}
