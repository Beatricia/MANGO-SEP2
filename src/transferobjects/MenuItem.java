package transferobjects;

import shared.Log;

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
  private String imgPath;

  /**
   * Construct the MenuItem object needed to transfer menu item.
   * @param name item's name.
   * @param ingredients list of item's ingredients, if its null, a new list will be created.
   * @param price item's price.
   * @throws NullPointerException if the name is null. (message: item name is null)
   * @throws IllegalArgumentException if the name is blank (message: item name is blank).
   * @throws IllegalArgumentException if the price is less than or equal to zero (message: price must be higher than 0).
   */

  public MenuItem(String name, ArrayList<String> ingredients, double price, String imgPath)
  {
    if(name == null)
      throw new NullPointerException("item name is null");
    if(name.isBlank())
      throw new IllegalArgumentException("item name is blank");

    if(ingredients == null)
      ingredients = new ArrayList<>();

    if(price <= 0)
      throw new IllegalArgumentException("price must be higher than 0");


    this.name = name;
    this.ingredients = ingredients;
    this.price = price;
    this.imgPath = imgPath;

    Log.log("MenuItem transferobject created");
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
   * Getting the image path
   * @return the image path
   */
  public String getImgPath(){
    return imgPath;
  }

  /**
   * A string representation with a name
   * @return the name
   */
  public String toString()
  {
    return name;
  }

}
