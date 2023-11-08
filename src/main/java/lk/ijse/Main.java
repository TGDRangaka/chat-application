package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.controller.CreateLoginFormController;
import lk.ijse.service.Server;

import java.net.ServerSocket;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Server server = new Server(new ServerSocket(1234));
        Thread thread = new Thread(server);
        thread.start();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/create_login_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Login Creater");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.setX(1);
        stage.setY(1);

        stage.show();
    }
}
