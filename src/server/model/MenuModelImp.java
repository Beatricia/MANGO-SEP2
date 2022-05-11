package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.DailyMenuItemList;
import transferobjects.MenuItem;
import transferobjects.SerializableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
    SerializableImage serializableImage = menuItem.getImage();
    String folder = "Resources/MenuItemImages/"; // path to the folder (directory)
    String imgPath = folder + menuItem.getName() + "." + serializableImage.getFormat(); // create image path

    databaseConn.addItem(menuItem.getName(), menuItem.getIngredients(),
        menuItem.getPrice(), imgPath);

    BufferedImage image = serializableImage.toImage(); // convert image back to buffered image
    try
    {
      //check if the image folder exists (if not, then create it
      File directory = new File(folder);
      if(!directory.exists())
        directory.mkdir();

      //save the image into the folder
      ImageIO.write(image, serializableImage.getFormat(), new File(imgPath));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
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
   * Passes the daily menu item onto the Database connection.
   * @param dailyMenuItemList which is unwrapped and passed onto the class Databaseconn
   */

  @Override public void addDailyMenuItem(DailyMenuItemList dailyMenuItemList)
      throws SQLException
  {
    databaseConn.addDailyMenu(dailyMenuItemList.getDate(), dailyMenuItemList.getMenuItems());
  }
}
