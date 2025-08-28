/**
 * A chatbot called Usagi.
 */

import java.util.Scanner;

public class Usagi {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Usagi(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (UsagiException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (Parser.isExit(input)) {
                ui.showBye();
                break;
            }
            try {
                Parser.handle(input, tasks, storage, ui);
            } catch (UsagiException e) {
                ui.showLine("Ura? (" + e.getMessage() + ")");
            }
        }
    }

    public static void main(String[] args) {
        new Usagi("data/usagi.txt").run();
    }
}