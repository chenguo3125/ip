package usagi.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Usagi usagi;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.png"));
    private Image usagiImage = new Image(this.getClass().getResourceAsStream("/images/usagi.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Usagi instance */
    public void setUsagi(Usagi u) {
        usagi = u;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Usagi's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        if (usagi == null) {
            System.err.println("Usagi instance not initialized");
            return;
        }
        
        String input = userInput.getText();
        if (input == null || input.trim().isEmpty()) {
            return; // Don't process empty input
        }
        
        try {
            String response = usagi.getResponse(input);
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    createUsagiResponseDialog(response, usagiImage)
            );
            userInput.clear();
        } catch (Exception e) {
            // Handle errors with red error bubble
            String errorMessage = e.getMessage() != null ? e.getMessage() : "An unexpected error occurred";
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(input, userImage),
                    DialogBox.getUsagiErrorDialog(errorMessage, usagiImage)
            );
            userInput.clear();
        }
    }
    
    /**
     * Creates an appropriate Usagi dialog based on the response content.
     * If the response contains task information, uses formatted task display.
     * Otherwise, uses regular dialog.
     */
    private DialogBox createUsagiResponseDialog(String response, Image usagiImage) {
        // Check if response contains task information
        if (response != null && (response.contains("Here are your tasks") || 
                                response.contains("Here are the matching tasks") ||
                                response.contains("Got it. I've added this task") ||
                                response.contains("Got it. I've added this recurring task") ||
                                response.contains("Noted. I've removed this task") ||
                                response.contains("Nice! I've marked this task as done") ||
                                response.contains("OK, I've marked this task as not done yet"))) {
            return DialogBox.getUsagiTaskDialog(response, usagiImage);
        } else {
            return DialogBox.getUsagiDialog(response, usagiImage);
        }
    }
}
