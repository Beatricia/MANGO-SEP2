package client.model;

import client.networking.Client;
import transferobjects.*;
import util.PropertyChangeSubject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

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
    client.addListener(ERROR_RECEIVED, this::sendError);
    client.addListener(PENDING_EMPLOYEES_RECEIVED, this::sendMenuItems);
  }

  /**
   * Fires an event with the error message to the ViewModel
   * @param event the event fired by the client
   */
  private void sendError(PropertyChangeEvent event)
  {
    support.firePropertyChange(ERROR_RECEIVED, null,event.getNewValue() );
  }

  private void sendMenuItems(PropertyChangeEvent event)
  {
    support.firePropertyChange(Menu_Items_Received,null,event.getNewValue());
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
      BufferedImage bufferedImage = ImageIO.read(new File(imgPath));
      String format = imgPath.substring(imgPath.lastIndexOf(".") + 1);
      SerializableImage serializableImage = new SerializableImage(bufferedImage, format);

      MenuItem item = new MenuItem(name,ingredients,price);
      item.setImage(serializableImage);

      client.addItem(item); //added the image path here
    } catch (IOException e) {
      PropertyChangeEvent evt = new PropertyChangeEvent(this, "", null, new ErrorMessage(e.getMessage()));
      sendError(evt);
    }

  }

  @Override public void requestMenuItems(Request request)
  {
    client.sendRequest(request);
  }

  @Override public void addItemsToDailyMenu(LocalDate date,
      ArrayList<MenuItem> menuItems)
  {
    DailyMenuItem dailyMenuItem = new DailyMenuItem(date,menuItems);
    client.addItemsToDailyMenu(dailyMenuItem);
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
  }

  /**
   * Using the PropertyChangeSubject object adds a listener for all types of
   * events
   * @param listener listener to be added
   */
  @Override public void addListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }
}
