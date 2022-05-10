package transferobjects;

import java.io.Serializable;

public class Request implements Serializable
{
  private String requestName;
  private Object requestObj;

  public Request(String requestName){
    this.requestObj = requestName;
  }

  public Object getObject(){
    return requestObj;
  }

  public void setObject(Object obj){
    requestObj = obj;
  }
}
