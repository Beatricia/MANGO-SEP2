package server.model;

import server.databaseConn.DatabaseConn;
import transferobjects.MenuItem;
import transferobjects.SerializableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

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
    String imgPath = "Resources/MenuItemImages/" + menuItem.getName() + "." + serializableImage.getFormat();
    databaseConn.addItem(menuItem.getName(), menuItem.getIngredients(),
        menuItem.getPrice(), imgPath);

    BufferedImage image = serializableImage.toImage();
    try
    {
      ImageIO.write(image, serializableImage.getFormat(), new File(imgPath));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
