package lk.ijse.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        while(!serverSocket.isClosed()){
            try{

                    Socket socket = serverSocket.accept();
                    System.out.println("A new client has joined to the chat!");

                    ClientHandler clientHandler = new ClientHandler(socket);

                    Thread thread = new Thread(clientHandler);
                    thread.start();


            } catch (IOException e){
                closeServerSocket();
            }
        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startServer();
    }
}
