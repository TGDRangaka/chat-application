package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.controller.LoginFormController;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Server server = new Server();
        server.startServer();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        LoginFormController loginFormController = fxmlLoader.getController();
        loginFormController.setServer(server);

        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.setScene(scene);

        stage.show();
    }
}
