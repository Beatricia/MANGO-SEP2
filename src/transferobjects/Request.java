package transferobjects;

import shared.Log;

import java.io.Serializable;

/**
 * Class for sending request through socket connection
 * @author Mango
 * @version 1
 */
public class Request implements Serializable
{
  public static final String MENU_ITEMS_REQUEST = "MenuItemRequest";
  public static final String EMPLOYEE_IS_ACCEPTED = "EmployeeIsAccepted";
  public static final String EMPLOYEE_IS_DECLINED = "EmployeeIsDeclined";
  public static final String PENDING_USER_REQUEST = "PendingUserRequest";
  public static final String DAILY_MENU_REQUEST = "DailyMenuRequest";
  public static final String WEEKLY_MENU_REQUEST = "WeeklyMenuRequest";
  public static final String ADD_ITEMS_TO_DAILY_MENU = "AddItemsToDailyMenu";
  public static final String ADD_QUANTITY_TO_DAILY_MENU = "AddQuantityToDailyMenu";
  public static final String DELETE_FROM_WEEKLY_MENU = "DeleteFromWeeklyMenu";
  public static final String ADD_ITEM_TO_CART = "AddItemToCart";
  public static final String EDIT_CART_ITEM = "EditCartItem";
  public static final String DELETE_CART_ITEM = "DeleteCartItem";
  public static final String CART_LIST_REQUEST = "CartListRequest";
  public static final String CUSTOMER_UNCOLLECTED_ORDER_REQUEST = "OrderRequest";
  public static final String CANCEL_ORDER = "CancelOrder";
  public static final String PLACE_ORDER = "PlaceOrder";


  private String requestName;
  private Object requestObj;

  /**
   * Constructor for initializing a request's name
   * @param requestName name that specifies type of the request
   */
  public Request(String requestName){

    this.requestName= requestName;
    Log.log("Request (" + requestName + ") transferobject created");
  }

  /**
   * Method for getting  the object which is being sent
   */
  public Object getObject(){
    return requestObj;
  }

  /**
   * Method for setting the object which is being sent
   * @param obj object to be sent through the socket connection
   */
  public void setObject(Object obj){
    requestObj = obj;
  }

  /**
   * Method for getting the name of the request
   */
  public String getRequestName()
  {
    return requestName;
  }

  /**
   * Method which prints out the necessary information about the Request object.
   * E.g. name of the request and the object being sent
   */
  @Override public String toString()
  {
    return "Request{" + "requestName='" + requestName + '\'' + ", requestObj="
        + requestObj + '}';
  }
}
