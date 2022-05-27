package client.networking;

import shared.Log;
import transferobjects.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

//TODO javadocs

/**
 *
 */
public class SocketClient implements Client{
    private ClientHandler clientHandler;
    private PropertyChangeSupport support;

    /**
     * Initializes a new SocketClient instance, tries to connect to the server, and if the connection
     * attempt succeeds, the SocketClient is listening to the server on a separate thread, so it won't
     * block the original one.
     * @throws IOException if the Server is unavailable, or if any other network error occurs
     */
    public SocketClient() throws IOException {
        support = new PropertyChangeSupport(this);
        clientHandler = new ClientHandler(this);
        Thread t = new Thread(clientHandler);
        t.setDaemon(true);
        t.start();
        Log.log("SocketClient thread started");
    }

    /**
     * Sends the LoginRequest to the server
     * @param request the LoginRequest object
     */
    @Override
    public void login(LoginRequest request) {
        clientHandler.send(request);
        Log.log("SocketClient LoginRequest(logIn) send to server");
    }

    /**
     * Sends the LoginRequest to the server
     * @param request the LoginRequest object
     */
    @Override
    public void register(LoginRequest request) {
        clientHandler.send(request);
        Log.log("SocketClient LoginRequest(register) send to server");
    }

    /**
     * Sends the MenuItem to the server
     * @param menuItem object to send to the server
     */
    @Override public void addItem(MenuItem menuItem) {
        clientHandler.send(menuItem);
        Log.log("SocketClient menuItem send to server");
    }

    /**
     * Sends the Request object to the server
     * @param request object to send to the server
     */
    @Override public void sendRequest(Request request)
    {
        Log.log("SocketClient sending request to server: " + request.toString());
        clientHandler.send(request);
    }

    /**
     * Request an image from the server
     * @param imageRequest the image request
     */
    @Override public void sendImageRequest(ImageRequest imageRequest) {
        Log.log("SocketClient sending image request " + imageRequest.getPath());

        clientHandler.send(imageRequest);
    }

    /**
     * Adds an event listener for a specific event fired in the SocketClient
     * @param event the event name to listen to
     * @param listener the listener instance
     */
    @Override
    public void addListener(String event, PropertyChangeListener listener) {
        support.addPropertyChangeListener(event,listener);
        Log.log(listener + " has been added to " + this + " as listener to " + event);
    }

    /**
     * Adds an event listener for all events fired in the SocketClient
     * @param listener the listener instance
     */
    @Override
    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
        Log.log(listener + " has been added to " + this + " as listener");
    }

    /**
     * Fires the LOGGED_IN_RECEIVED event when a User object has been received from the server
     * @param obj the User object
     */
    public void userReceivedFromServer(User obj) {
        support.firePropertyChange(LOGGED_IN_RECEIVED, null,obj);
        Log.log("SocketClient fires a LOGGED_IN_RECEIVED event");
    }

    /**
     * Fires the ERROR_RECEIVED event when an error has been received from the server
     * @param obj the ErrorMessage object
     */
    public void errorReceivedFromServer(ErrorMessage obj) {
        support.firePropertyChange(ERROR_RECEIVED, null, obj);
        Log.log("SocketClient fires an ERROR_RECEIVED event");
    }

    /**
     * Fires the PENDING_EMPLOYEES_RECEIVED event when a
     * list of pending employee has been received from the server
     * @param request the Request object
     */
    public void listOfEmployeeReceived(Request request)
    {
       support.firePropertyChange(PENDING_EMPLOYEES_RECEIVED,null, request.getObject());
        Log.log("SocketClient fires a PENDING_EMPLOYEES_RECEIVED event");
    }

    /**
     * Fires the MENU_ITEMS_RECEIVED event when a
     * list of menu items has been received from the server
     * @param request the Request object
     */

    public void listOfMenuItemsReceived(Request request)
    {
        support.firePropertyChange(MENU_ITEMS_RECEIVED,null,request.getObject());
        Log.log("SocketClient fires a MENU_ITEMS_RECEIVED event");
    }

    /**
     * Fires a DAILY_MENU_RECEIVED event when a DailyMenuItemList object has
     * been received from the server
     * @param request the Request object
     */
    public void dailyMenuReceived(Request request)
    {
        Log.log("SocketClient fires a DAILY_MENU_RECEIVED event");
        support.firePropertyChange(DAILY_MENU_RECEIVED, null, request.getObject());
    }

  public void weeklyMenuReceived(Request request)
  {
      Log.log("SocketClient fires a WEEKLY_MENU_RECEIVED event");
      support.firePropertyChange(WEEKLY_MENU_RECEIVED, null, request.getObject());
  }

    public void cartListReceived(Request request)
    {
        Log.log("SocketClient fires a CART_LIST_RECEIVED event");
        support.firePropertyChange(CART_LIST_RECEIVED, null, request.getObject());

    }

    public void imageRequestReceived(ImageRequest imageRequest){
        Log.log("SocketClient fires an IMAGE_RECEIVED event");
        support.firePropertyChange(IMAGE_RECEIVED, null, imageRequest);
    }

    public void uncollectedOrderReceived(Request request)
    {
        Log.log("SocketClient fires an ORDER_RECEIVED event");
        support.firePropertyChange(ORDER_RECEIVED, null, request.getObject());
    }

    public void allAcceptedEmployeesReceived(Request request)
    {
        Log.log("SocketClient fires an ACCEPTED_EMPLOYEES_RECEIVED event");
        support.firePropertyChange(ACCEPTED_EMPLOYEES_RECEIVED,null,request.getObject());
    }

    public void openingHoursReceived(Request request)
    {
        Log.log("SocketClient fires an OPENING_HOURS_RECEIVED event");
        support.firePropertyChange(OPENING_HOURS_RECEIVED,null,request.getObject());
    }

    public void allUncollectedOrdersReceived(Request request){
        Log.log("SocketClient fires an ALL_UNCOLLECTED_ORDERS_RECEIVED event");
        support.firePropertyChange(ALL_UNCOLLECTED_ORDERS_RECEIVED, null, request.getObject());
    }

    public void purchaseHistoryReceived(Request request){
        Log.log("SocketClient fires an ALL_UNCOLLECTED_ORDERS_RECEIVED event");
        support.firePropertyChange(PURCHASE_HISTORY_RECEIVED, null, request.getObject());
    }

    public void statisticsReceived(Request request) {
        Log.log("SocketClient fires an STATISTICS_RECEIVED event");
        support.firePropertyChange(STATISTICS_RECEIVED, null, request.getObject());
    }
}
