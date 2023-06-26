package lk.ijse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatFormController implements Initializable {

    @FXML
    private VBox vBoxChat;

    @FXML
    private JFXTextField txtField;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private String userName = "test";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try{
                socket = new Socket("localhost", 1234);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("User name : " + userName);
                dataOutputStream.writeUTF(userName);

                while (true){
                    messageListner();
                }

            }catch (IOException e){
                System.out.println(e);
            }

        }).start();
    }

    @FXML
    void txtFieldOnAction(ActionEvent event) {
        String message = txtField.getText();

        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        setMessage(message);
        txtField.setText(null);
    }

    public void messageListner() throws IOException {
        dataInputStream = new DataInputStream(socket.getInputStream());
        String message = dataInputStream.readUTF();

        Platform.runLater(() -> {
            setMessage(message);
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

    private void setMessage(String message){
        TextField textField = new TextField(message);
        textField.setEditable(false);
        vBoxChat.getChildren().add(textField);
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
}
