package lk.ijse;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    public void startServer() throws IOException {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(3002);
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            new Thread(() -> {
                try {
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    String message = dataInputStream.readUTF();
                    System.out.println(message);

                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(message + " received!");
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();
        }).start();

    }

}
