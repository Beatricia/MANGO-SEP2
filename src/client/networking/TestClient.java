package client.networking;

import shared.Log;
import client.model.MenuModel;
import client.model.MenuModelImp;
import client.model.OrderModelCustomerImp;
import shared.UserType;
import transferobjects.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

public class TestClient implements Client
{
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  public TestClient() throws IOException {
    new Thread(() -> {
      try {
        Thread.sleep(2000);
        support.firePropertyChange(Client.LOGGED_IN_RECEIVED, null, new User("test-user", UserType.ADMINISTRATOR, "first-test-name", "last-test-name"));
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
  }

  @Override public void login(LoginRequest request) {

  }

  @Override public void register(LoginRequest request) {

  }



  @Override public void sendRequest(Request request)
  {
    Log.log("TestClient Request sent: " + request);
  }

  @Override public void sendImageRequest(ImageRequest imageRequest)
  {

  }

  @Override public void addListener(String event, PropertyChangeListener listener) {
    support.addPropertyChangeListener(event, listener);
  }

  @Override public void addListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}
