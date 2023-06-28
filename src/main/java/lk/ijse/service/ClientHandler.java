package lk.ijse.service;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private String userName;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));

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
                message = bufferedReader.readLine();
                sendMessage(userName + ": " + message);
            } catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        for(ClientHandler clientHandler : clientHandlers){

            String user = clientHandler.userName;

            if (!user.equals(userName)) {

                clientHandler.bufferedWriter.write(  message);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();

            }
        }
    }

    public void removeClient() throws IOException {
        clientHandlers.remove(this);
        sendMessage("SERVER : " + userName + " has left the chat!");
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
