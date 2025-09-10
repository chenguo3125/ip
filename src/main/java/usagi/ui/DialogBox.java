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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class DialogBox extends HBox {

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
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

        if (dialog != null) {
            dialog.setText(text != null ? text : "");
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
        displayPicture = new ImageView();
        this.getChildren().addAll(dialog, displayPicture);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);
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
        return db;
    }
}
