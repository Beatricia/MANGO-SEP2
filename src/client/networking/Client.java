package client.networking;

import transferobjects.*;
import util.PropertyChangeSubject;

import java.beans.PropertyChangeListener;
import java.rmi.Remote;
import java.util.ArrayList;

// An interface that holds two variables that will be called in the socketClient for firing events
// It will handle the login and the register action
// It takes a loginRequest object as a parameter for both register and login
public interface Client extends PropertyChangeSubject
{
  String ERROR_RECEIVED = "ErrorReceived";
  String LOGGED_IN_RECEIVED = "LogInReceived";
  String PENDING_EMPLOYEES_RECEIVED = "PendingEmployeesReceived";
  String MENU_ITEMS_RECEIVED = "MenuItemsReceived";
  String DAILY_MENU_RECEIVED = "DailyMenuReceived";
  String WEEKLY_MENU_RECEIVED = "WeeklyMenuReceived";
  String CART_LIST_RECEIVED = "CartListReceived";
  String ORDER_RECEIVED = "OrderReceived";
  String IMAGE_RECEIVED = "ImageReceived";
  String ALL_UNCOLLECTED_ORDERS_RECEIVED = "AllUncollectedOrdersReceived";
  String ACCEPTED_EMPLOYEES_RECEIVED = "AcceptedEmployeesReceived";
  String OPENING_HOURS_RECEIVED = "OpeningHoursReceived";
  String PURCHASE_HISTORY_RECEIVED = "PurchaseHistoryReceived";
  String STATISTICS_RECEIVED = "StatisticsReceived";

    /**
   * The method is used to send to the Server the LoginRequest object
   *    when a person loggs in it is called
   * @param request the LoginRequest to be sent
   */
  void login(LoginRequest request);
  /**
   * The method is used to send to the Server the LoginRequest object
   *    when a person registers it is called
   * @param request the LoginRequest to be sent
   */
  void register(LoginRequest request);
  /**
   * The method is used to send to the Server the MenuItem object
   * which should me add to  the system
   * @param menuItem the MenuItem to be sent
   */
  void addItem(MenuItem menuItem);
  /**
   * The method is used to send to the Server the Request object
   * @param request the Request object to be sent
   */
  void sendRequest(Request request);
  /**
   * Request an image from the server
   * @param imageRequest the image request
   */
  void sendImageRequest(ImageRequest imageRequest);

}
