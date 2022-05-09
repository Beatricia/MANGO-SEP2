package transferobjects;

import java.io.Serializable;

public class Request implements Serializable
{
  private String requestName;
  private Object requestObj;

  public Object getObject(){
    return requestObj;
  }

  public void setObject(Object obj){
    requestObj = obj;
  }
}
