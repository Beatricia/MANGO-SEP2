package server;

import server.networking.SocketServer;

/**
 * Class responsible for creating a <code>SocketServer</code> instance, and start it.
 * @author Gergo
 * @version 1
 * @see SocketServer
 * @see SocketServer#startServer()
 */
public class StartServer
{
  /**
   * Main entry of the server side of the <b>CanEat</b> app.
   * @param args arguments, they are ignored.
   * @see SocketServer
   */
  public static void main(String[] args) {
    try{
      SocketServer server = new SocketServer();
      server.startServer();
    } catch (Exception e){
      e.printStackTrace();
    }
  }
}
