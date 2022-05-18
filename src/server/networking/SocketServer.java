package server.networking;

import server.databaseConn.DatabaseConn;
import server.databaseConn.DatabaseConnImp;
import server.model.*;
import shared.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;

/**
 * The class which is responsible for initializing the <code>DatabaseConn</code>, the model, and accept
 * incoming socket connections.
 * @author Gergo
 * @version 1
 */
public class SocketServer
{
  /**
   * Initializing the <code>ServerSocket</code> and waiting for the new <code>Socket</code> connections to accept.
   * @throws IOException Throws IOException if a Fatal error happens.
   */
  public void startServer() throws IOException {
    Log.log("SocketServer: Server Running");
    // Create server socket on specific port
    try(ServerSocket serverSocket = new ServerSocket(1111)){

      DatabaseConn databaseConn = new DatabaseConnImp();
      UserModel userModel = new UserModelImp(databaseConn);
      MenuModel menuModel = new MenuModelImp(databaseConn);
      AdminModel adminModel = new AdminModelImp(databaseConn);
      CartModel cartModel = new CartModelImpl(databaseConn);

      while(true){

        // Accept a new socket
        Socket clientSocket = serverSocket.accept();
        Log.log("SocketServer accepts a new socket");
        // Init server socket handler
        ServerHandler handler = new ServerHandler(clientSocket, userModel, menuModel, adminModel);
        // put the handler on a different thread
        Thread handlerThread = new Thread(handler);
        Log.log("SocketServer puts handler into another thread");
        handlerThread.start();
    }
    }
  }
}
