package client.model;

import util.PropertyChangeSubject;

import java.util.ArrayList;

public interface MenuModel extends PropertyChangeSubject
{
  String ERROR_RECEIVED = "ErrorReceived";

  public void addItem(String name, ArrayList<String> ingredients, double price,
      String imgPath);
}
