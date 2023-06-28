package lk.ijse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.ResourceBundle;

public class ChatFormController implements Initializable {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vBoxChat;

    @FXML
    private JFXTextField txtField;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String userName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName = LoginFormController.userName;
        new Thread(() -> {
            try{
                socket = new Socket("localhost", 1234);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));

                sendMessage(userName);

                while (true){
                    messageListner();
                }

            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                new Alert(Alert.AlertType.ERROR, "Client connectring to server error!").show();
            }

        }).start();

        vBoxChat.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scrollPane.setVvalue((Double) t1);
            }
        });
    }

    @FXML
    void txtFieldOnAction(ActionEvent event) {
        String message = txtField.getText();

        try {

            sendMessage(message);

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            new Alert(Alert.AlertType.ERROR, "Message Sending Error!").show();
        }

        setMessage("Me: " + message, Side.RIGHT);
        txtField.setText(null);
    }
    public void sendMessage(String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void messageListner() throws IOException {
        String message = bufferedReader.readLine();

        Platform.runLater(() -> {
            setMessage(message, Side.LEFT);
        });
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

    private void setMessage(String message, Side side){
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 10, 5, 10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setMaxWidth(500);
        textFlow.setPadding(new Insets(5, 10, 5, 10));

        if (side.equals(Side.LEFT)){
            hBox.setAlignment(Pos.CENTER_LEFT);
            textFlow.setStyle(
                    "-fx-background-color: #5ea1e3;" +
                    "-fx-background-radius: 20px;" +
                    "-fx-font-size: 16px;");
        }else {
            hBox.setAlignment(Pos.CENTER_RIGHT);
            textFlow.setStyle(
                    "-fx-background-color: rgba(53,241,57,0.99);" +
                    "-fx-background-radius: 20px;" +
                    "-fx-font-size: 16px;");
        }

        hBox.getChildren().add(textFlow);
        vBoxChat.getChildren().add(hBox);
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try{
            if (socket != null){
                socket.close();
            }

            if (bufferedReader != null){
                bufferedReader.close();
            }

            if (bufferedWriter != null){
                bufferedWriter.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
