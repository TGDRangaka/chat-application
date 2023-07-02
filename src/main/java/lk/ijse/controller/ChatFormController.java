package lk.ijse.controller;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.ResourceBundle;

public class ChatFormController implements Initializable {
    public Pane paneCamImgBtns;
    public Pane paneImage;
    public ImageView imgView;
    public JFXButton btnImageCapture;
    public JFXButton btnSendImg;
    @FXML
    private JFXButton btnSend;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vBoxChat;

    @FXML
    private JFXTextField txtField;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;

    private Webcam webcam;
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

        webcam = Webcam.getDefault();
        webcam.setViewSize(new java.awt.Dimension(320,240));

        vBoxChat.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                scrollPane.setVvalue((Double) t1);
            }
        });

        btnSend.setOnAction((e) -> {
            txtFieldOnAction(e);
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

        setMessage(message, Side.RIGHT);
        txtField.setText(null);
    }
    public void sendMessage(String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    public void messageListner() throws IOException {
        String message = bufferedReader.readLine();
        String[] split = message.split(":");

        if (split[0].equals("_coming_an_image")){
            String sender = split[1];
            imageListner(sender);
        }else {
            Platform.runLater(() -> {
                setMessage(message, Side.LEFT);
            });
        }
    }

    private void imageListner(String senderUserName) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        BufferedImage bufferedImage = ImageIO.read(bis);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        setImage(image, senderUserName, Side.LEFT);
    }

    private void setImage(Image image, String senderUserName, Side side) {
        Text text = new Text(senderUserName);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5, 10, 5 ,10));
        ImageView imageView = resizeImage(image, 400);
        VBox vBox = new VBox();
        HBox hBox = new HBox();

        if (side.equals(Side.LEFT)){
            vBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setAlignment(Pos.CENTER_LEFT);
            textFlow.setMaxWidth(senderUserName.length() * 15);
            hBox.setPadding(new Insets(5, 10, 5, 10));
            textFlow.setStyle(
                    "-fx-background-color: #FFFFFD;" +
                    "-fx-background-radius: 5px 20px 0px 0px;" +
                    "-fx-start-margin: 10px");

            vBox.getChildren().add(textFlow);
            vBox.getChildren().add(imageView);
        }else {
            vBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            vBox.setPadding(new Insets(5, 10, 5, 10));
            hBox.setPadding(new Insets(5, 10, 5, 10));

//            textFlow.setMaxWidth(senderUserName.length() * 15);
//            textFlow.setStyle(
//                    "-fx-background-color: rgba(53,241,57,0.99);" +
//                            "-fx-background-radius: 20px 5px 0px 0px;" +
//                            "-fx-font-size: 16px;");

            vBox.getChildren().add(imageView);
        }

        hBox.getChildren().add(vBox);

        Platform.runLater(() -> {
            vBoxChat.getChildren().add(hBox);
        });


    }

    @FXML
    void btnEmojiOnAction(ActionEvent event) {

    }

    @FXML
    void btnImageOnAction(ActionEvent event) {
        paneCamImgBtns.setVisible(!paneCamImgBtns.isVisible());
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
                    "-fx-background-color: #a6c4ff;" +
                    "-fx-background-radius: 5px 20px 20px 20px;" +
                    "-fx-font-size: 16px;");
        }else {
            hBox.setAlignment(Pos.CENTER_RIGHT);
            textFlow.setStyle(
                    "-fx-background-color: #6affce;" +
                    "-fx-background-radius: 20px 5px 20px 20px;" +
                    "-fx-font-size: 16px;");
        }

        hBox.getChildren().add(textFlow);
        vBoxChat.getChildren().add(hBox);
    }

    private java.awt.Image convertToAWTImage(Image fxImage) {
        int width = (int) fxImage.getWidth();
        int height = (int) fxImage.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        SwingFXUtils.fromFXImage(fxImage, bufferedImage);

        return bufferedImage;
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

    public void btnCameraOnAction(ActionEvent actionEvent) {
        paneCamImgBtns.setVisible(false);
        paneImage.setVisible(!paneImage.isVisible());
        webcam.open();
        btnImageCapture.setVisible(true);
        btnSendImg.setDisable(true);

        new Thread(() -> {
            while (webcam.isOpen()){
                imgView.setImage(SwingFXUtils.toFXImage(webcam.getImage(), null));
                try{Thread.sleep(20);} catch (InterruptedException e){e.printStackTrace();}
            }
        }).start();

    }

    public void btnImageChooserOnAction(ActionEvent actionEvent) {
        paneCamImgBtns.setVisible(false);
        paneImage.setVisible(!paneImage.isVisible());
        btnSendImg.setDisable(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\"));
        File file = fileChooser.showOpenDialog(new Stage());

        String filePath = file.getPath();

        imgView.setImage(new Image(filePath));
        btnSendImg.setDisable(false);
    }

    public void btnCloseOnActioin(ActionEvent actionEvent) {
        if (webcam.isOpen()){
            webcam.close();
        }

        btnImageCapture.setVisible(false);
        paneImage.setVisible(false);
        imgView.setImage(null);
    }

    public void btnImageCaptureOnAction(ActionEvent actionEvent) {
        webcam.close();
        btnSendImg.setDisable(false);


    }

    public void btnSendImgOnAction(ActionEvent actionEvent) {
        btnImageCapture.setVisible(false);

        try {
            bufferedWriter.write("_coming_an_image");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            Image fxImage = imgView.getImage();
            java.awt.Image awtImage = convertToAWTImage(fxImage);

            BufferedImage bufferedImage = new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null),BufferedImage.TYPE_INT_ARGB);

            Graphics graphics = bufferedImage.createGraphics();
            graphics.drawImage(awtImage, 0, 0, null);
            graphics.dispose();

            ImageIO.write(bufferedImage,"png", bos);

            setImage(fxImage, userName, Side.RIGHT);

        } catch (IOException e){
            e.printStackTrace();
        }

        paneImage.setVisible(false);
        btnSendImg.setDisable(true);
    }

    public ImageView resizeImage(Image highResImage, double maxWidth) {
        double originalWidth = highResImage.getWidth();
        if (originalWidth <= maxWidth){
            return new ImageView(highResImage);
        }
        double originalHeight = highResImage.getHeight();

        double ratio = originalWidth / originalHeight;

        double newWidth = Math.min(originalWidth, maxWidth);
        double newHeight = newWidth / ratio;

        ImageView iv = new ImageView(highResImage);
        iv.setFitHeight(newHeight);
        iv.setFitWidth(newWidth);

        return iv;
    }
}
