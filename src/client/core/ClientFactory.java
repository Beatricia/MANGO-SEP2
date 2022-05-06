package client.core;

import client.networking.Client;
import client.networking.SocketClient;
import client.networking.TestClient;

import java.io.IOException;

/**
 * A class that creates and returns a Client object
 * @author Uafa
 */
public class ClientFactory
{
  private Client client;

  /**
   * Uses lazy instantiation to creates a SocketClient object and return it
   * @return the Client object that was created
   */
  public Client getClient()
  {
    if(client == null)
    {
      try
      {
        //client = new SocketClient();
        //
        client = new TestClient();
      }
      catch (IOException e)
      {
        System.err.println("--------------------------------------------");
        System.err.println("          SERVER IS NOT RUNNING");
        System.err.println("--------------------------------------------");

        System.exit(0);
      }
    }
    return client;
  }
}
