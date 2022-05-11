package transferobjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class representing a MenuItem object with name, list of all ingredients and price.
 * @author Agata
 * @version 1
 */

public class MenuItem implements Serializable
{
  private String name;
  private ArrayList<String> ingredients;
  private double price;
  private SerializableImage image;

  /**
   * Construct the MenuItem object
   * needed to transfer menu item
   * @param name item's name
   * @param ingredients list of item's ingredients
   * @param price item's price
   */

  public MenuItem(String name,ArrayList<String> ingredients, double price)
  {
    this.name = name;
    this.ingredients = ingredients;
    this.price = price;

  }

  /**
   * gets the name
   * @return item's name
   */

  public String getName()
  {
    return name;
  }

  /**
   * gets the ingredients
   * @return list of item's ingredients
   */

  public ArrayList<String> getIngredients()
  {
    return ingredients;
  }

  /**
   * gets the price
   * @return item's price
   */

  public double getPrice()
  {
    return price;
  }

  /**
   * gets the path of the image
   * @return image's path
   */
  public SerializableImage getImage()
  {
    return image;
  }

  /**
   * assigns parameter as image variable
   */

  public void setImage(SerializableImage image)
  {
    this.image = image;
  }


  public String toString()
  {
    return name;
  }

}
