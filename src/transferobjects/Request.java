package transferobjects;

import java.io.Serializable;

public class Request implements Serializable
{
  private String requestName;
  private Object requestObj;

  public static final String MENU_ITEMS_REQUEST = "MenuItemRequest";
  public static final String EMPLOYEE_IS_ACCEPTED = "EmployeeIsAccepted";
  public static final String EMPLOYEE_IS_DECLINED = "EmployeeIsDeclined";
  public static final String PENDING_USER_REQUEST = "PendingUserRequest";

  public Request(String requestName){
    this.requestObj = requestName;
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
