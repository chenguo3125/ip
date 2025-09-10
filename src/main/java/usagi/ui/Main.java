package usagi.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private Usagi usagi = new Usagi("data/usagi.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            stage.setTitle("Usagi - Task Manager");
            
            MainWindow controller = fxmlLoader.getController();
            if (controller != null) {
                controller.setUsagi(usagi);
            } else {
                System.err.println("Warning: MainWindow controller is null");
            }
            
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load MainWindow FXML: " + e.getMessage());
            e.printStackTrace();
            // Create a simple fallback UI
            createFallbackUI(stage);
        } catch (Exception e) {
            System.err.println("Unexpected error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a simple fallback UI if FXML loading fails.
     */
    private void createFallbackUI(Stage stage) {
        try {
            VBox root = new VBox();
            TextField input = new TextField();
            Button sendButton = new Button("Send");
            TextArea output = new TextArea();
            output.setEditable(false);
            
            sendButton.setOnAction(e -> {
                String userInput = input.getText();
                if (!userInput.trim().isEmpty()) {
                    String response = usagi.getResponse(userInput);
                    output.appendText("You: " + userInput + "\n");
                    output.appendText("Usagi: " + response + "\n\n");
                    input.clear();
                }
            });
            
            root.getChildren().addAll(output, input, sendButton);
            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);
            stage.setTitle("Usagi - Task Manager (Fallback Mode)");
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to create fallback UI: " + e.getMessage());
        }
    }

}
