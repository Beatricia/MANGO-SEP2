package client.model;

import client.networking.Client;
import shared.Log;
import transferobjects.*;
import transferobjects.MenuItem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

//TODO javadocs

/**
 * A class that connects the view model and the Client side of networking.
 * In this class the information about items is collected into a MenuItem object
 * and send to the Client side of networking. The class is the subject of the
 * MenuEmplViewModel and send it Error objects.
 * @author Uafa
 */

public class MenuModelImp implements MenuModel
{
  private Client client;
  private PropertyChangeSupport support;

  /**
   * Constructor for the class. Takes a Client object as a parameter and
   * assigns it to the client private variable in the class. Adds the class as
   * to be a Listener for the Client object.
   * @param client the client that information has to be send to
   */
  public MenuModelImp(Client client)
  {
    support = new PropertyChangeSupport(this);
    this.client = client;
    client.addListener(Client.ERROR_RECEIVED, this::sendError);
    client.addListener(Client.MENU_ITEMS_RECEIVED, this::sendMenuItems);
    client.addListener(Client.DAILY_MENU_RECEIVED, this::sendMenuWithIngredients);
    client.addListener(Client.WEEKLY_MENU_RECEIVED, this::sendWeeklyMenu);
  }

  private void sendWeeklyMenu(PropertyChangeEvent event)
  {
    Log.log("MenuModelImp: WEEKLY_MENU_REQUEST received from server");
     support.firePropertyChange(WEEKLY_MENU_RECEIVED, null, event.getNewValue());
  }

  /**
   * Fires an event with the error message to the ViewModel
   * @param event the event fired by the client
   */
  private void sendError(PropertyChangeEvent event)
  {
    support.firePropertyChange(ERROR_RECEIVED, null,event.getNewValue() );

    Log.log("MenuModelImp fires an ERROR_RECEIVED property");
  }

  /**
   * Fires an event with the list of menu items to the ViewModel
   * @param event the event fired by the client
   */
  private void sendMenuItems(PropertyChangeEvent event)
  {
    support.firePropertyChange(MENU_ITEMS_RECEIVED,null,event.getNewValue());
    Log.log("MenuModelImp fires an MENU_ITEMS_RECEIVED property");
  }

  /**
   * Fires an event with the list of menu items and ingredients to the ViewModel
   * @param event
   */
  private void sendMenuWithIngredients(PropertyChangeEvent event)
  {
    support.firePropertyChange(DAILY_MENU_RECEIVED,null, event.getNewValue());
    Log.log("MenuModelImpl fires an DAILY_MENU_RECEIVED");
  }


  /**
   * Takes a String name, an ArrayList of Strings representing the ingredients,
   * a double value for the price and the image path. Using those parameters
   * creates a MenuItem, which is send to the client with the addItem method
   * located in the client class.
   * @param name  the name of the item to be added
   * @param ingredients an ArrayList of all ingredients of the item
   * @param price the price of the item
   */
  @Override public void addItem(String name, ArrayList<String> ingredients,
      double price, String imgPath)
  {
    try {
      //read image from the client computer
      BufferedImage bufferedImage = ImageIO.read(new File(imgPath));

      //get image format (png, jpg)
      String format = imgPath.substring(imgPath.lastIndexOf(".") + 1);

      //create image path for our application (now the server and the client can read the images from here)
      String appImgPath = "Resources/MenuItemImages/" + name + "." + format;

      //save the image to this path so we can later read it back
      ImageIO.write(bufferedImage,format, new File(appImgPath));
      //SerializableImage serializableImage = new SerializableImage(bufferedImage, format);

      MenuItem item = new MenuItem(name, ingredients, price, appImgPath);
      //item.setImage(serializableImage);
      Log.log("MenuModelImpl created a new MenuItem and set its image path");

      client.addItem(item); //added the image path here
    } catch (IOException e) {
      PropertyChangeEvent evt = new PropertyChangeEvent(this, "", null, new ErrorMessage(e.getMessage()));
      sendError(evt);
    }

  }

  /**
   * The method creates request object with correct name
   * and sends this object to the client
   */
  @Override public void requestMenuItems()
  {
    Request request = new Request(Request.MENU_ITEMS_REQUEST);
    client.sendRequest(request);

    Log.log("MenuModelImpl send a MENU_ITEMS_REQUEST object to the Client (sendRequest method)");
  }

  /**
   * Creating a request to add the specific items to the daily menu, first creating a list with
   * menuItemWithQuantity where each element has everything that a MenuItem has, a given date and the quantity set to 0
   * @param date the date on which the menuItemWithQuantity will be
   * @param menuItems the list of all menuItems for the specific date
   */
  @Override public void addItemsToDailyMenu(LocalDate date,
      ArrayList<MenuItem> menuItems) {
    MenuItemWithQuantity menuItemWithQuantity;
    ArrayList<MenuItemWithQuantity> itemsWithQuantity = new ArrayList<>();
    for (int i = 0; i < menuItems.size(); i++) {
      menuItemWithQuantity = new MenuItemWithQuantity(menuItems.get(i), date, 0);
      itemsWithQuantity.add(menuItemWithQuantity);
    }

    Request request = new Request(Request.ADD_ITEMS_TO_DAILY_MENU);
    request.setObject(itemsWithQuantity);
    Log.log("MenuModelImpl sends a new  ADD_ITEMS_TO_DAILY_MENU request to the Client (addItemsToDailyMenu method)");
    client.sendRequest(request);
  }

  /**
   * Creating a request for the daily menu, which is sent to the client
   */
  @Override
  public void requestDailyMenu() {
    Request request = new Request(Request.DAILY_MENU_REQUEST);
    Log.log("MenuModelImpl sends a new DAILY_MENU_REQUEST object to the Client (requestDailyMenu method)");
    client.sendRequest(request);
  }

  /**
   * Creating a request to add the quantity to items from the dailyMenu, which is sent to the client
   * @param listOfItemsWithQuantity the list of menu items with quantity
   */
  @Override
  public void addQuantity(ArrayList<MenuItemWithQuantity> listOfItemsWithQuantity) {
    Request request = new Request(Request.ADD_QUANTITY_TO_DAILY_MENU);
    request.setObject(listOfItemsWithQuantity);
    Log.log("MenuModelImpl send a new ADD_QUANTITY_TO_DAILY_MENU request to the Client(addQuantity method)");
    client.sendRequest(request);
  }

  /**
   * Creating a request for the current weekly menu, which is sent to the client
   */
  @Override
  public void requestWeeklyMenu() {
    Request request = new Request(Request.WEEKLY_MENU_REQUEST);
    Log.log("MenuModelImpl send a new WEEKLY_MENU_REQUEST request to the Client(requestWeeklyMenu method)");
    client.sendRequest(request);
  }

  /**
   * Creating a request with the list of the menu items to be deleted from the system, which is sent to the client
   * @param listOfMenuItemsToDelete the list of menu items to be deleted
   */
  @Override
  public void deleteMenuItemFromWeeklyMenu(ArrayList<MenuItemWithQuantity> listOfMenuItemsToDelete) {
    Request request = new Request(Request.DELETE_FROM_WEEKLY_MENU);
    request.setObject(listOfMenuItemsToDelete);
    Log.log("MenuModelImpl send a new DELETE_FROM_WEEKLY_MENU request to the Client(deleteMenuItemFromWeeklyMenu method)");
    client.sendRequest(request);
  }


  /**
   * Using the PropertyChangeSubject object adds a listener for a specific event
   * @param event  event to be listened
   * @param listener listener to be added
   */
  @Override public void addListener(String event,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(event,listener);

    Log.log(listener.getClass().getName() + "has been added as a listener to " + this + " for " + event);
  }

  /**
   * Using the PropertyChangeSubject object adds a listener for all types of
   * events
   * @param listener listener to be added
   */
  @Override public void addListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);

    Log.log(listener.getClass().getName() + "has been added as a listener to " + this);
  }

}
