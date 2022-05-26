package client.networking;

import shared.Log;
import transferobjects.ErrorMessage;
import transferobjects.ImageRequest;
import transferobjects.Request;
import transferobjects.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

//TODO protocol

/**
 * A class that is responsible for handling all the new incoming client connections
 *
 * @author Beatricia
 * @version 1
 */

public class ClientHandler implements Runnable
{
  private final Socket socket; // An unconnected socket
  private final ObjectInputStream fromServer; //Stream to receive objects from server
  private final ObjectOutputStream toServer; //Stream to send objects to server
  private final SocketClient client;

  /**
   * Creates a stream socket and connects it to the 1111 port number at the "localhost" address
   * Sets up the base streams
   *
   * @param client to be taken care of
   * @throws IOException thrown if an I/O error occurs when creating the socket
   */
  public ClientHandler(SocketClient client) throws IOException
  {
    socket = new Socket("localhost", 1111);
    fromServer = new ObjectInputStream(socket.getInputStream());
    toServer = new ObjectOutputStream(socket.getOutputStream());
    this.client = client;
  }

  /**
   * Listen to Server and handle the receiving object
   */
  @Override public void run()
  {
    try
    {
      while (true)
      {
        Object obj = fromServer.readObject();

        //in case the received object is a User
        if (obj instanceof User)
        {
          Log.log("ClientHandler User object received from server");
          client.userReceivedFromServer((User) obj);
        }
        // in case the received object is an ErrorMessage
        else if (obj instanceof ErrorMessage)
        {
          Log.log("ClientHandler ErrorMessage object received from server");
          client.errorReceivedFromServer((ErrorMessage) obj);
        }
        else if (obj instanceof Request)
        {
          Request request = (Request) obj;
          if (request.getRequestName().equals(Request.MENU_ITEMS_REQUEST))
          {
            Log.log("ClientHandler MENU_ITEMS_REQUEST received from server");
            client.listOfMenuItemsReceived(request);
          }
          else if (request.getRequestName().equals(Request.PENDING_USER_REQUEST))
          {
            Log.log("ClientHandler PENDING_USER_REQUEST received from server");
            client.listOfEmployeeReceived(request);
          }
          else if (request.getRequestName().equals(Request.DAILY_MENU_REQUEST))
          {
            Log.log("ClientHandler DAILY_MENU_REQUEST received from server");
            client.dailyMenuReceived(request);
          }
          else if (request.getRequestName().equals(Request.WEEKLY_MENU_REQUEST))
          {
            Log.log("ClientHandler WEEKLY_MENU_REQUEST received from server");
            client.weeklyMenuReceived(request);
          }
          else if (request.getRequestName().equals(Request.CART_LIST_REQUEST))
          {
            Log.log("ClientHandler CART_LIST_REQUEST received from server");
            client.cartListReceived(request);
          }
          else if(request.getRequestName().equals(Request.CUSTOMER_UNCOLLECTED_ORDER_REQUEST))
          {
            Log.log("ClientHandler CUSTOMER_UNCOLLECTED_ORDER_REQUEST received from server");
            client.uncollectedOrderReceived(request);
          }
          else if(request.getRequestName().equals(Request.ALL_ACCEPTED_EMPLOYEES_REQUEST))
          {
            client.allAcceptedEmployeesReceived(request);
          }
          else if (request.getRequestName().equals(Request.OPENING_HOURS_REQUEST))
          {
            client.openingHoursReceived(request);
          }
        }
        else if(obj instanceof ImageRequest)
        {
          ImageRequest imageRequest = (ImageRequest) obj;
          client.imageRequestReceived(imageRequest);
        }
      }
    }
    catch (IOException | ClassNotFoundException e)
    {
      e.printStackTrace();
      System.exit(0);
    }
  }

  /**
   * Sends the Serializable object to the Server
   *
   * @param serializable to be sent
   */
  public void send(Serializable serializable)
  {
    try
    {
      toServer.reset();
      toServer.writeUnshared(serializable);
      Log.log("ClientHandler send object to server");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
