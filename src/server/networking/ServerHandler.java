package server.networking;

import server.model.AdminModel;
import server.model.MenuModel;
import server.model.UserModel;
import shared.Log;
import transferobjects.*;
import util.LogInException;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

/**
 * <p>The <code>ServerHandler</code> is having a role of handling a client connection on the server side
 * of the <b>CanEat</b> application.
 * </p>
 * <p>
 *   As a <code>ServerHandler</code> object can handle one client connection in it's lifetime,
 *   therefore for each connection request from the clients, a new instance is created.
 * </p>
 * <p>
 *   To fully take advantage of the features of the <code>ServerHandler</code>, it must be put on a new Thread,
 *   so that it can listen simultaneously to the client while the other parts of the program can work
 *   on their job.
 * </p>
 */
public class ServerHandler implements Runnable
{
  private Socket clientSocket; //Client socket
  private ObjectInputStream fromClient; //Stream to receive objects from client
  private ObjectOutputStream toClient; //Stream to send objects to client

  private UserModel userModel;
  private MenuModel menuModel;
  private AdminModel adminModel;

  /**
   * Constructs the ServerHandler object, sets up the base streams.
   * @param clientSocket Client Socket to be taken care of.
   * @param userModel User model to forward data to.
   */
  public ServerHandler(Socket clientSocket, UserModel userModel, MenuModel menuModel, AdminModel adminModel) {
    try{
      System.out.printf("Client connected (%s:%s)%n", clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());

      this.clientSocket = clientSocket;
      toClient = new ObjectOutputStream(clientSocket.getOutputStream());
      fromClient = new ObjectInputStream(clientSocket.getInputStream());
      this.userModel = userModel;
      this.menuModel = menuModel;
      this.adminModel = adminModel;

    } catch (IOException e){
      // If an IOException happens during the initialization of the streams, it means that there is
      // something wrong with the socket, and therefore should close the client.
      closeClient(e);
    }
  }

  /**
   * Send a <code>Serializable</code> object to the Client.
   * @param o Object to be sent.
   */
  public void sendObject(Serializable o){
    try{
      Log.log("ServerHandler object sent to the server");
      // Send the object here
      toClient.writeObject(o);
      System.out.println("Object sent: " + o);
    }
    catch (IOException | NullPointerException e) {
      // If an IOException happens during the sending process, it means that there is something
      // wrong with the stream, and therefore should close the client.
      closeClient(e);
    }
  }

  /**
   * Listen to client and handle receiving object.
   */
  @Override public void run() {
    try{
      while(true){
        //Object variable to load the object we get from the client
        Object receivedObj;

        try{
          //read an object from the client
          receivedObj = fromClient.readObject();

        } catch (ClassNotFoundException e){
          // Catch the exception above. (Catch it here, because this exception should not happen.
          //  If it happens, then don't close the whole stream, just skip to read the next
          //  object from the client.)
          e.printStackTrace();
          continue;
        }

        // Handling exceptions
        try{
          handleReceivedObject(receivedObj);
        } catch (NullPointerException e) {
          e.printStackTrace();
        } catch (Exception e) {
          // Constructing the Error Message object
          ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
          Log.log("ServerHandler catches error message and sends it to client");
          // Sending the Error Message object back to client
          sendObject(errorMessage);
        }

      }
    } catch (IOException | NullPointerException e){
      // If an IOException happens during the reading process, it means that there is something
      // wrong with the stream (e.g. lost connection), and therefore should close the client.
      closeClient(e);
    }
  }

  /**
   * The method is used to handle received object
   * @param receivedObj
   * @throws Exception
   */
  private void handleReceivedObject(Object receivedObj) throws Exception {
    // in case the received object is a LoginRequest object
    if (receivedObj instanceof LoginRequest) {
      LoginRequest request = (LoginRequest) receivedObj;

      handleLoginRequest(request);
    }
    // in case the received object is a MenuItem object
    else if (receivedObj instanceof MenuItem) {
      Log.log("ServerHandler received MenuItem object");
      MenuItem menuItem = (MenuItem) receivedObj;
      menuModel.addItem(menuItem);
    }

    //in case the received object is a DailyMenuItemList object
    else if(receivedObj instanceof DailyMenuItemList)
    {
      Log.log("ServerHandler received dailyMenuItem object");
      DailyMenuItemList dailyMenuItem = (DailyMenuItemList) receivedObj;
      menuModel.addDailyMenuItem(dailyMenuItem);
    }

    else if(receivedObj instanceof Request)
    {
      Request request = (Request) receivedObj;
      handleRequestObject(request);
    }
  }

  /**
   * The method is used to distinguish type of received Request object
   * @param request
   * @throws SQLException
   */
  private void handleRequestObject(Request request) throws SQLException
  {
    Log.log("ServerHandler received MENU_ITEMS_REQUEST");
    if(request.getRequestName().equals(Request.MENU_ITEMS_REQUEST))
    {
      Log.log("ServerHandler sends Request object to client with list of menu items");
      request.setObject(menuModel.getListOfMenuItems());
      sendObject(request);
    }

    else if(request.getRequestName().equals(Request.PENDING_USER_REQUEST))
    {
      Log.log("ServerHandler received PENDING_USER_REQUEST");
      request.setObject(adminModel.requestPendingEmployees());
      Log.log("ServerHandler sends Request object to client with list of pending employee");
      sendObject(request);
    }
    else if(request.getRequestName().equals(Request.EMPLOYEE_IS_ACCEPTED))
    {
      Log.log("ServerHandler received EMPLOYEE_IS_ACCEPTED");
      adminModel.acceptEmployee((User)request.getObject());
     // sendObject();
    }
    else if(request.getRequestName().equals(Request.EMPLOYEE_IS_DECLINED))
    {
      Log.log("ServerHandler received EMPLOYEE_IS_DECLINED");
      adminModel.declineEmployee((User)request.getObject());
    }

  }

  /**
   * The method is used to distinguish between two types of LoginRequest object
   * @param request
   * @throws SQLException
   * @throws LogInException
   */
  private void handleLoginRequest(LoginRequest request) throws SQLException, LogInException {
    // User variable to load the object we receive from the model
    User user;

    // Check if the request is actually a login or a register request
    if (request.getIsRegister())
    {
      user = userModel.register(request);

      Log.log("ServerHandler received LoginRequest(register) object");
    }

    else
      user = userModel.login(request);

    Log.log("ServerHandler received LoginRequest(logIn) object");

    // Send the Logged in - User object back to the client
    sendObject(user);
    Log.log("ServerHandler sends user object back");
  }

  /**
   * Close client and fire closing event.
   */
  public void closeClient(Exception e){

    e.printStackTrace();
    // todo fire "Closing" event here for the connection pool (later)
    //      so the pool can remove it form the list when the connection is closed

    // Close streams
    closeObject(toClient);
    Log.log("ServerHandler closes toClient stream");
    closeObject(fromClient);
    Log.log("ServerHandler closes fromClient stream");
    closeObject(clientSocket);
    Log.log("ServerHandler closes clientSocket stream ");

    //Set the objects to null, so the Garbage Collector can collect the unused streams.
    toClient = null;
    fromClient = null;
    clientSocket = null;
  }

  /**
   * Close a <code>Closeable</code> (e.g. streams or socket) without raising an exception.
   * @param closeable Object to close
   */
  private void closeObject(Closeable closeable){
    try{
      //Try to close the object
      closeable.close();
    }catch (IOException | NullPointerException ignored){
      // Ignore the exception part, as it should NOT raise any exception
    }
  }
}
