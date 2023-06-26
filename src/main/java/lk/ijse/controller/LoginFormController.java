package lk.ijse.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Server;

import java.io.IOException;

public class LoginFormController {
    @FXML
    private AnchorPane root;

    private Server server;

    @FXML
    void btnGoToChatOnAction(ActionEvent event) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/chat_form.fxml"));
            Node node = fxmlLoader.load();

            root.getChildren().setAll(node);
//            Scene scene = new Scene(fxmlLoader.load());
//
//            Stage stage = new Stage();
//            stage.setTitle("Chat");
//            stage.centerOnScreen();
//            stage.setScene(scene);
//
//            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setServer(Server server){
        server = this.server;
    }

}
