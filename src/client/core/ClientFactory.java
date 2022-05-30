package client.core;

import client.networking.Client;
import client.networking.SocketClient;
import shared.Log;

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
        Log.log("Client was created");
        client = new SocketClient();
      }
      catch (IOException e)
      {
        Log.log("---------------ClientFactory----------------");
        Log.log("--------------------------------------------");
        Log.log("          SERVER IS NOT RUNNING");
        Log.log("--------------------------------------------");
        System.exit(0);
      }
    }


    return client;
  }
}
