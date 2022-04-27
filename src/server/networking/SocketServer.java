package server.networking;

import server.databaseConn.DatabaseConn;
import server.databaseConn.DatabaseConnImp;
import server.model.UserModel;
import server.model.UserModelImp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer
{
  public void startServer() throws IOException {

    // Create server socket on specific port
    ServerSocket serverSocket = new ServerSocket(1111);
    System.out.println("Server Running");


    DatabaseConn databaseConn = new DatabaseConnImp();
    UserModel userModel = new UserModelImp(databaseConn);

    while(true){
      // Accept a new socket
      Socket clientSocket = serverSocket.accept();
      // Init server socket handler
      ServerHandler handler = new ServerHandler(clientSocket, userModel);
      // put the handler on a different thread
      Thread handlerThread = new Thread(handler);
      handlerThread.start();
    }
  }
}
