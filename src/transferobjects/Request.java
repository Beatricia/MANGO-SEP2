package transferobjects;

import shared.Log;

import java.io.Serializable;

public class Request implements Serializable
{
  public static final String MENU_ITEMS_REQUEST = "MenuItemRequest";
  public static final String EMPLOYEE_IS_ACCEPTED = "EmployeeIsAccepted";
  public static final String EMPLOYEE_IS_DECLINED = "EmployeeIsDeclined";
  public static final String PENDING_USER_REQUEST = "PendingUserRequest";





  private String requestName;
  private Object requestObj;

  public Request(String requestName){
    this.requestObj = requestName;
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
}
