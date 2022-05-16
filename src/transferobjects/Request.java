package transferobjects;

import shared.Log;

import java.io.Serializable;

public class Request implements Serializable
{
  public static final String MENU_ITEMS_REQUEST = "MenuItemRequest";
  public static final String EMPLOYEE_IS_ACCEPTED = "EmployeeIsAccepted";
  public static final String EMPLOYEE_IS_DECLINED = "EmployeeIsDeclined";
  public static final String PENDING_USER_REQUEST = "PendingUserRequest";
  public static final String DAILY_MENU_REQUEST = "DailyMenuRequest";
  public static final String ADD_ITEMS_TO_DAILY_MENU = "AddItemsToDailyMenu";
  public static final String ADD_QUANTITY_TO_DAILY_MENU = "AddQuantityToDailyMenu";
  public static final String DELETE_FROM_WEEKLY_MENU = "DeleteFromWeeklyMenu";
  public static final String WEEKLY_MENU_REQUEST = "WeeklyMenuRequest";


  private String requestName;
  private Object requestObj;

  public Request(String requestName){

    this.requestName= requestName;
    Log.log("Request (" + requestName + ") transferobject created");
  }


  public Object getObject(){
    return requestObj;
  }

  public void setObject(Object obj){
    requestObj = obj;
  }

  public String getRequestName()
  {
    return requestName;
  }

  @Override public String toString()
  {
    return "Request{" + "requestName='" + requestName + '\'' + ", requestObj="
        + requestObj + '}';
  }
}
