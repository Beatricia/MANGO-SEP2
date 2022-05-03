package client.model;

import client.networking.Client;
import transferobjects.MenuItem;

import java.util.ArrayList;

public class MenuModelImp implements MenuModel
{
  private Client client;

  /**
   * Constructor for the class. Takes a Client object as a parameter and
   * assignes it to the client private variable in the class.
   * @param client the client that information has to be send to
   */
  public MenuModelImp(Client client)
  {
    this.client = client;
  }

  /**
   * Takes a String name, an ArrayList of Strings representing the ingredients,
   * and a double value for a price and using those parameters creates a
   * MenuItem, which is send to the client with the addItem method located in
   * the client class.
   * @param name  the name of the item to be added
   * @param ingredients an ArrayList of all ingredients of the item
   * @param price the price of the item
   */
  @Override public void addItem(String name, ArrayList<String> ingredients,
      double price)
  {
    client.addItem(new MenuItem(name,ingredients,price));
  }
}
