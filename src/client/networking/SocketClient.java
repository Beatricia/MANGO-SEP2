package client.networking;

import transferobjects.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;

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
        t.start();
    }

    /**
     * Sends the LoginRequest to the server
     * @param request the LoginRequest object
     */
    @Override
    public void login(LoginRequest request) {
        clientHandler.send(request);
    }

    /**
     * Sends the LoginRequest to the server
     * @param request the LoginRequest object
     */
    @Override
    public void register(LoginRequest request) {
        clientHandler.send(request);
    }

    /**
     * Sends the MenuItem to the server
     * @param menuItem object to send to the server
     */
    @Override public void addItem(MenuItem menuItem) {
        clientHandler.send(menuItem);
    }

    @Override public void sendRequest(Request request)
    {
        clientHandler.send(request);
    }

    @Override public void addItemsToDailyMenu(DailyMenuItem dailyMenuItem)
    {
        clientHandler.send(dailyMenuItem);
    }

    @Override public void acceptEmployee(User user)
    {
        User userAccepted = new User(user.getUsername(),user.getUserType(),user.getFirstName(),user.getLastName(),true);
        clientHandler.send(userAccepted);
    }

    @Override public void declineEmployee(User user)
    {
        User userDeclined = new User(user.getUsername(),user.getUserType(),user.getFirstName(),user.getLastName(),false);
        clientHandler.send(userDeclined);
    }

    /**
     * Adds an event listener for a specific event fired in the SocketClient
     * @param event the event name to listen to
     * @param listener the listener instance
     */
    @Override
    public void addListener(String event, PropertyChangeListener listener) {
        support.addPropertyChangeListener(event,listener);
    }

    /**
     * Adds an event listener for all events fired in the SocketClient
     * @param listener the listener instance
     */
    @Override
    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Fires the LOGGED_IN_RECEIVED event when a User object has been received from the server
     * @param obj the User object
     */
    public void userReceivedFromServer(User obj) {
        support.firePropertyChange(LOGGED_IN_RECEIVED, null,obj);
    }

    /**
     * Fires the ERROR_RECEIVED event when an error has been received from the server
     * @param obj the ErrorMessage object
     */
    public void errorReceivedFromServer(ErrorMessage obj) {
        support.firePropertyChange(ERROR_RECEIVED, null, obj);
    }

    public void listOfEmployeeReceived(Request request)
    {
       support.firePropertyChange(PENDING_EMPLOYEES_RECEIVED,null, request.getObject());
    }

    public void listOfMenuItemsReceived(Request request)
    {
        support.firePropertyChange(MENU_ITEMS_RECEIVED,null,request.getObject());
    }

}
