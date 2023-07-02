package lk.ijse.controller;


import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField txtUserName;

    public static String userName;

    @FXML
    void btnGoToChatOnAction(ActionEvent event) {
        userName = txtUserName.getText();

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/chat_form.fxml"));
            root.getChildren().setAll((Node)fxmlLoader.load());

        } catch (IOException e) {
            e.printStackTrace();
        }

        userName = null;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtUserName.setOnAction((e) -> {
            btnGoToChatOnAction(e);
        });
    }
}
