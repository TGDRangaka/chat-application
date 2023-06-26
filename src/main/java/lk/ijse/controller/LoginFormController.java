package lk.ijse.controller;


import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginFormController {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    void btnGoToChatOnAction(ActionEvent event) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/chat_form.fxml"));
            root.getChildren().setAll((Node)fxmlLoader.load());

            ChatFormController controller = fxmlLoader.getController();
            controller.setUserName(txtUserName.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
