package lk.ijse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ChatFormController {

    @FXML
    private VBox vBoxChat;

    @FXML
    private JFXTextField txtField;

    @FXML
    void txtFieldOnAction(ActionEvent event) {
        String message = txtField.getText();

        TextField textField = new TextField(message);
        textField.setEditable(false);
        vBoxChat.getChildren().add(textField);

        txtField.setText(null);
    }

    @FXML
    void btnEmojiOnAction(ActionEvent event) {

    }

    @FXML
    void btnImageOnAction(ActionEvent event) {
        Image image = new Image("F:/Second Sem/chat-application/src/main/resources/img/user.png");
        ImageView imageView = new ImageView(image);
        vBoxChat.getChildren().add(imageView);
    }

}
