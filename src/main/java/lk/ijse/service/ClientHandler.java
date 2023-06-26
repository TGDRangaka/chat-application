package lk.ijse.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private String userName;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            dataInputStream = new DataInputStream(this.socket.getInputStream());
            dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            userName = dataInputStream.readUTF();

            clientHandlers.add(this);

            sendMessage("SERVER : " + userName + " has entered to the chat");
        } catch (IOException e){
            closeEverything(socket, dataOutputStream, dataInputStream);
        }
    }


    @Override
    public void run() {
        String message;

        while (socket.isConnected()){
            try {
                message = dataInputStream.readUTF();
                sendMessage(message);
            } catch (IOException e){
                closeEverything(socket, dataOutputStream, dataInputStream);
                break;
            }
        }
    }

    public void sendMessage(String message) {
        for(ClientHandler clientHandler : clientHandlers){
            try{
                String user = clientHandler.userName;
                if (!user.equals(userName)) {
                    clientHandler.dataOutputStream.writeUTF(userName + ": " + message);
                    clientHandler.dataOutputStream.flush();
                }
            } catch (IOException e){
                closeEverything(socket, dataOutputStream, dataInputStream);
            }
        }
    }

    public void removeClient(){
        clientHandlers.remove(this);
        sendMessage("SERVER : " + userName + " has left the chat!");
    }

    private void closeEverything(Socket socket, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
        removeClient();

        try{
            if (socket != null){
                socket.close();
            }

            if (dataInputStream != null){
                dataInputStream.close();
            }

            if (dataOutputStream != null){
                dataOutputStream.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
