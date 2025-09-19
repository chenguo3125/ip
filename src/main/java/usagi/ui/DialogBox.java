package usagi.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;

public class DialogBox extends HBox {

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;
    
    private String textContent;

    private DialogBox(String text, Image img) {
        // Store the text content for clipboard functionality
        this.textContent = text != null ? text : "";
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("Failed to load DialogBox FXML: " + e.getMessage());
            // Create a fallback UI programmatically
            createFallbackUI();
        }

        // Set spacing between profile picture and message bubble
        this.setSpacing(12);

        if (dialog != null) {
            dialog.setText(this.textContent);
            // Apply default user styling
            dialog.getStyleClass().add("label");
            // Add click event handler to copy text to clipboard
            dialog.setOnMouseClicked(this::handleClick);
        }
        if (displayPicture != null && img != null) {
            displayPicture.setImage(img);
        }
    }
    
    /**
     * Creates a fallback UI if FXML loading fails.
     */
    private void createFallbackUI() {
        dialog = new Label();
        dialog.getStyleClass().add("label"); // Default to user styling
        dialog.setText(this.textContent);
        dialog.setOnMouseClicked(this::handleClick);
        displayPicture = new ImageView();
        this.getChildren().addAll(dialog, displayPicture);
        this.setSpacing(12); // Consistent spacing with main constructor
        this.setAlignment(Pos.CENTER_LEFT);
    }
    
    /**
     * Handles click events to copy text to clipboard.
     */
    private void handleClick(MouseEvent event) {
        if (textContent != null && !textContent.trim().isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(textContent);
            clipboard.setContent(content);
            
            // Show "Copied!" notification
            showCopyNotification();
        }
    }
    
    /**
     * Shows a "Copied!" notification that fades in and out at the top of the dialog container.
     */
    private void showCopyNotification() {
        // Create notification label
        Label notification = new Label("Copied!");
        notification.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); " +
                             "-fx-text-fill: white; " +
                             "-fx-padding: 8px 16px; " +
                             "-fx-background-radius: 20px; " +
                             "-fx-font-size: 14px; " +
                             "-fx-font-weight: bold;");
        
        // Position notification at the top center
        StackPane notificationPane = new StackPane(notification);
        notificationPane.setAlignment(Pos.TOP_CENTER);
        notificationPane.setStyle("-fx-background-color: transparent;");
        
        // Find the dialog container (VBox) to add the notification
        Node parent = this.getParent();
        while (parent != null && !(parent instanceof VBox)) {
            parent = parent.getParent();
        }
        
        if (parent instanceof VBox) {
            VBox dialogContainer = (VBox) parent;
            
            // Add notification at the top of the dialog container
            dialogContainer.getChildren().add(0, notificationPane);
            
            // Create fade in animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), notification);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            
            // Create pause
            PauseTransition pause = new PauseTransition(Duration.millis(1000));
            
            // Create fade out animation
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), notification);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            
            // Create sequential animation
            SequentialTransition sequence = new SequentialTransition(fadeIn, pause, fadeOut);
            
            // Remove notification after animation completes
            sequence.setOnFinished(e -> {
                dialogContainer.getChildren().remove(notificationPane);
            });
            
            sequence.play();
        }
    }


    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    public static DialogBox getUsagiDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        // Apply Usagi-specific styling
        if (db.dialog != null) {
            db.dialog.getStyleClass().clear();
            db.dialog.getStyleClass().add("reply-label");
        }
        return db;
    }
    
    /**
     * Creates a Usagi dialog for error messages with red background.
     * @param text The error message to display
     * @param img The image to display
     * @return A DialogBox with red error styling
     */
    public static DialogBox getUsagiErrorDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        
        // Apply error styling
        if (db.dialog != null) {
            db.dialog.getStyleClass().clear();
            db.dialog.getStyleClass().add("error-label");
        }
        
        return db;
    }
    
    /**
     * Creates a Usagi dialog with formatted task text (bold task type and title).
     * @param text The task text to display with formatting
     * @param img The image to display
     * @return A DialogBox with formatted task text
     */
    public static DialogBox getUsagiTaskDialog(String text, Image img) {
        var db = new DialogBox("", img); // Empty text, we'll set TextFlow
        db.flip();
        
        // Store the original text for clipboard functionality
        db.textContent = text;
        
        // Create formatted task display
        TextFlow taskFlow = createFormattedTaskText(text);
        
        // Add click handler to TextFlow for clipboard functionality
        taskFlow.setOnMouseClicked(db::handleClick);
        
        // Replace the Label with TextFlow
        if (db.dialog != null) {
            db.dialog.getStyleClass().clear();
            db.dialog.getStyleClass().add("reply-label");
            
            // Remove click handler from dialog to prevent double notifications
            db.dialog.setOnMouseClicked(null);
            
            // Clear the label and add the TextFlow
            db.dialog.setText("");
            db.dialog.setGraphic(taskFlow);
        }
        
        return db;
    }
    
    /**
     * Creates formatted text for task display with bold task type and title.
     * @param taskText The raw task text (can be multi-line with multiple tasks)
     * @return TextFlow with formatted text
     */
    private static TextFlow createFormattedTaskText(String taskText) {
        TextFlow textFlow = new TextFlow();
        
        if (taskText == null || taskText.trim().isEmpty()) {
            textFlow.getChildren().add(new Text(""));
            return textFlow;
        }
        
        
        // Split the text into lines and process each line
        String[] lines = taskText.split("\n");
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            
            // Add line break between lines (except for the first line)
            if (i > 0) {
                Text lineBreak = new Text("\n");
                textFlow.getChildren().add(lineBreak);
            }
            
            // Check if this line contains a task (starts with number and task type)
            if (line.matches("^\\d+\\.\\[[TDE]\\].*")) {
                // This is a numbered task line like "1.[T][X] ps1"
                
                // Extract the number part (e.g., "1.")
                int taskStartIndex = line.indexOf("[");
                String numberPart = line.substring(0, taskStartIndex);
                String taskPart = line.substring(taskStartIndex);
                
                // Add the number part (not bold)
                Text numberText = new Text(numberPart);
                numberText.setStyle("-fx-fill: #2e2e2e;");
                textFlow.getChildren().add(numberText);
                
                // Process the task part
                if (taskPart.startsWith("[T]") || taskPart.startsWith("[D]") || 
                    taskPart.startsWith("[E]") || taskPart.startsWith("[R]")) {
                    
                    // Extract task type (first 3 characters: [T], [D], [E], or [R])
                    String taskType = taskPart.substring(0, 3);
                    
                    // Extract status (next 3 characters: [X] or [ ])
                    String status = taskPart.substring(3, 6);
                    
                    // Extract the rest (title and details)
                    String rest = taskPart.substring(6);
                    
                    
                    // Create bold task type
                    Text typeText = new Text(taskType);
                    typeText.setStyle("-fx-font-weight: bold; -fx-fill: #2e2e2e; -fx-font-size: 15px;");
                    
                    // Create status (bold) - preserve original status [X] or [ ]
                    Text statusText = new Text(status);
                    statusText.setStyle("-fx-font-weight: bold; -fx-fill: #2e2e2e; -fx-font-size: 15px;");
                    
                    // Extract title (everything before the first parenthesis)
                    String title;
                    String details = "";
                    int parenIndex = rest.indexOf('(');
                    if (parenIndex > 0) {
                        title = rest.substring(0, parenIndex).trim();
                        details = rest.substring(parenIndex);
                    } else {
                        title = rest.trim();
                    }
                    
                    // Create bold title
                    Text titleText = new Text(title);
                    titleText.setStyle("-fx-font-weight: bold; -fx-fill: #2e2e2e; -fx-font-size: 15px;");
                    
                    // Create details (not bold)
                    Text detailsText = new Text(details);
                    detailsText.setStyle("-fx-fill: #2e2e2e;");
                    
                    textFlow.getChildren().addAll(typeText, statusText, titleText, detailsText);
                } else {
                    // Fallback for task part
                    Text taskText_normal = new Text(taskPart);
                    taskText_normal.setStyle("-fx-fill: #2e2e2e;");
                    textFlow.getChildren().add(taskText_normal);
                }
            } else if (line.contains("[T]") || line.contains("[D]") || line.contains("[E]") || line.contains("[R]")) {
                // This might be a single task line without numbering (like from add commands)
                
                // Find the task part
                int taskStartIndex = line.indexOf("[");
                if (taskStartIndex >= 0) {
                    String beforeTask = line.substring(0, taskStartIndex);
                    String taskPart = line.substring(taskStartIndex);
                    
                    // Add the part before the task (not bold)
                    if (!beforeTask.isEmpty()) {
                        Text beforeText = new Text(beforeTask);
                        beforeText.setStyle("-fx-fill: #2e2e2e;");
                        textFlow.getChildren().add(beforeText);
                    }
                    
                    // Process the task part (same logic as above)
                    if (taskPart.startsWith("[T]") || taskPart.startsWith("[D]") || 
                        taskPart.startsWith("[E]") || taskPart.startsWith("[R]")) {
                        
                        String taskType = taskPart.substring(0, 3);
                        String status = taskPart.substring(3, 6);
                        String rest = taskPart.substring(6);
                        
                        Text typeText = new Text(taskType);
                        typeText.setStyle("-fx-font-weight: bold; -fx-fill: #2e2e2e; -fx-font-size: 15px;");
                        
                        Text statusText = new Text(status);
                        statusText.setStyle("-fx-font-weight: bold; -fx-fill: #2e2e2e; -fx-font-size: 15px;");
                        
                        String title;
                        String details = "";
                        int parenIndex = rest.indexOf('(');
                        if (parenIndex > 0) {
                            title = rest.substring(0, parenIndex).trim();
                            details = rest.substring(parenIndex);
                        } else {
                            title = rest.trim();
                        }
                        
                        Text titleText = new Text(title);
                        titleText.setStyle("-fx-font-weight: bold; -fx-fill: #2e2e2e; -fx-font-size: 15px;");
                        
                        Text detailsText = new Text(details);
                        detailsText.setStyle("-fx-fill: #2e2e2e;");
                        
                        textFlow.getChildren().addAll(typeText, statusText, titleText, detailsText);
                    } else {
                        Text taskText_normal = new Text(taskPart);
                        taskText_normal.setStyle("-fx-fill: #2e2e2e;");
                        textFlow.getChildren().add(taskText_normal);
                    }
                } else {
                    // No task found in this line
                    Text normalText = new Text(line);
                    normalText.setStyle("-fx-fill: #2e2e2e;");
                    textFlow.getChildren().add(normalText);
                }
            } else {
                // Regular text line (not a task)
                Text normalText = new Text(line);
                normalText.setStyle("-fx-fill: #2e2e2e;");
                textFlow.getChildren().add(normalText);
            }
        }
        
        return textFlow;
    }
}