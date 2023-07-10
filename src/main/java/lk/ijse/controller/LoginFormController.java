package lk.ijse.controller;


import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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

            ((Stage) root.getScene().getWindow()).setScene(new Scene(new FXMLLoader().load(getClass().getResource("/view/chat_form.fxml"))));

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
