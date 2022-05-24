package server.model;

import javafx.beans.property.StringProperty;
import server.databaseConn.DatabaseConn;
import shared.Log;
import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;
import transferobjects.SerializableImage;
import util.DateHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Class responsible for connecting the networking part of the Server with Database connection and sending Menu Items into the Database.
 * @author Simon
 * @version 1
 */
public class MenuModelImp implements MenuModel
{

  private DatabaseConn databaseConn;

  /**
   * Constructor which is assigning database connection
   * @param databaseConn is an instance of the database connection which is connection the server with the database
   */
  public MenuModelImp(DatabaseConn databaseConn){
    this.databaseConn = databaseConn;
  }

  /**
   * Passes the menu item onto the Database connection.
   * @param menuItem which is unwrapped and passed onto the class Databaseconn
   */
  @Override public void addItem(MenuItem menuItem) throws SQLException
  {
    //SerializableImage serializableImage = menuItem.getImage();
    //String folder = "Resources/MenuItemImages/"; // path to the folder (directory)
    String imgPath = menuItem.getImgPath(); //folder + menuItem.getName() + "." + serializableImage.getFormat(); // create image path

    databaseConn.addItem(menuItem.getName(), menuItem.getIngredients(),
        menuItem.getPrice(), imgPath);

    /*BufferedImage image = serializableImage.toImage(); // convert image back to buffered image
    try
    {
      //check if the image folder exists (if not, then create it
      File directory = new File(folder);
      if(!directory.exists())
        directory.mkdir();

      //save the image into the folder
      //save the image into the folder
      ImageIO.write(image, serializableImage.getFormat(), new File(imgPath));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }*/
  }

  /**
   * get list of all menu items received from Databaseconn
   * @return ArrayList of MenuItem
   */
  @Override public ArrayList<MenuItem> getListOfMenuItems() throws SQLException
  {
    return databaseConn.getListOfMenuItems();
  }

  /**
   * Adding the list of daily menu items to the database
   * @param dailyMenuItem
   */
  @Override public void addDailyMenuItem(ArrayList<MenuItemWithQuantity> dailyMenuItem)
      throws SQLException
  {
    databaseConn.addDailyMenu(dailyMenuItem);
  }

  /**
   * Request the database for the daily menu items for the current date
   * @return a list with the menu item with quantity for the current date
   */
  @Override
  public ArrayList<MenuItemWithQuantity> requestDailyMenu() throws SQLException {
    LocalDate now = LocalDate.now();
    return databaseConn.gatDailyMenuItemList(now);
  }

  /**
   * Adding the quantity to the list of the MenuItemsWithQuantity which was previously set as 0 for the current date
   * @param listOfMenuItemsWithQuantity the list of menu items with new quantity
   */
  @Override
  public void addQuantity(ArrayList<MenuItemWithQuantity> listOfMenuItemsWithQuantity) throws SQLException {
    LocalDate now = LocalDate.now();
    for (MenuItemWithQuantity item : listOfMenuItemsWithQuantity) {
      databaseConn.addQuantity(now, item.getName(), item.getQuantity());
    }
  }

  /**
   * Getting the weekly menu from the database
   * First checking with the help of "DateHalper" the next available monday, then creating an arraylist where
   * every day of the week from that monday to the next friday is added
   * @return the array list with Monday-Friday's daily menu (a weekly menu overall)
   */
  @Override
  public ArrayList<MenuItemWithQuantity> requestWeeklyMenu() throws SQLException {
    LocalDate current = DateHelper.getCurrentAvailableMonday();

    ArrayList<MenuItemWithQuantity> requestedMenuItems = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      ArrayList<MenuItemWithQuantity>  menuItems = databaseConn.gatDailyMenuItemList(current.plusDays(i));
     requestedMenuItems.addAll(menuItems);
    }
    return requestedMenuItems;
  }

  /**
   * Deleting the items with a specific date and quantity
   * @param listOfItemsToDelete the list of the items that will be deleted
   */
  @Override
  public void deleteMenuItemFromWeeklyMenu(ArrayList<MenuItemWithQuantity> listOfItemsToDelete) throws SQLException {
    for (MenuItemWithQuantity item : listOfItemsToDelete) {
      databaseConn.deleteMenuItemFromDailyMenu(item.getDate(), item.getName());
    }
  }

  /**
   * The method is used to delete menu items from database
   * @param menuItems list of menu items to delete
   */

  @Override public void removeMenuItem(ArrayList<MenuItem> menuItems) throws SQLException {
    databaseConn.removeMenuItem(menuItems);
  }
}
