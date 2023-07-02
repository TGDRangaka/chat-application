package lk.ijse.service;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private BufferedOutputStream bos;

    private String userName;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            bos = new BufferedOutputStream(socket.getOutputStream());

            userName = bufferedReader.readLine();

            clientHandlers.add(this);

            sendMessage("SERVER : " + userName + " has entered to the chat");
        } catch (IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    @Override
    public void run() {
        String message;

        while (socket.isConnected()){
            try {
                message = bufferedReader.readLine();    //listening to upcoming messages

                if (message.equals("_coming_an_image")){
                    sendImage();
                }else {
                    sendMessage(userName + ": " + message);
                }
            } catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    private void sendImage() throws IOException {
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        BufferedImage bufferedImage = ImageIO.read(bis);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        System.out.println("image received");

        sendMessage("_coming_an_image:" + userName);
//        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
            try {
                System.out.println("now sending image");
                sendImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }));
//        timeline.play();

    }

    private void sendImage(Image fxImage) throws IOException {
        for(ClientHandler clientHandler : clientHandlers){
            String user = clientHandler.userName;
            if (!user.equals(userName)) {


                    java.awt.Image awtImage = convertToAWTImage(fxImage);

                    BufferedImage bufferedImage = new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null),BufferedImage.TYPE_INT_ARGB);

                    Graphics graphics = bufferedImage.createGraphics();
                    graphics.drawImage(awtImage, 0, 0, null);
                    graphics.dispose();

                    ImageIO.write(bufferedImage,"png", clientHandler.bos);


            }
        }
    }

    public void sendMessage(String message) throws IOException {
        for(ClientHandler clientHandler : clientHandlers){

            String user = clientHandler.userName;

            if (!user.equals(userName)) {

                clientHandler.bufferedWriter.write(message);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();

            }
        }
    }

    public void removeClient() throws IOException {
        clientHandlers.remove(this);
        sendMessage("SERVER : " + userName + " has left the chat!");
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

            removeClient();

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
