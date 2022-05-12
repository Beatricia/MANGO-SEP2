package client.view.employee.AddDish;

import client.model.MenuModel;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.Log;
import transferobjects.ErrorMessage;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A View Model class, which connects the MenuModel and the MenuEmpl view. This
 * class listens to the Model to get any ErrorMessage objects that have to be
 * displayed in the view. Also this class send the provided in the view
 * information about the Items to the model.
 *
 * @author Uafa
 */

public class MenuEmplViewModel
{
  private StringProperty errorMessage;
  private MenuModel menuModel;

  /**
   * Constructor for the class, initializes the private variable errorMassage to
   * be a SimpleStringProperty, and adds the class to be a Listener to the
   * MenuModel class.
   *
   * @param menuModel the model that is subject for the class
   */
  public MenuEmplViewModel(MenuModel menuModel)
  {
    this.menuModel = menuModel;
    errorMessage = new SimpleStringProperty("");

    menuModel.addListener(MenuModel.ERROR_RECEIVED, this::errorReceived);
  }

  /**
   * Returns the Error Message send by the model
   *
   * @return the error message
   */
  public Property<String> getErrorMessage()
  {
    return errorMessage;
  }

  /**
   * Send the provided in the GUI information about an item to the model.
   * Calls the private method separateIngredients to convert the String
   * provided by the user to create an ArrayList of all ingredients. Also
   * parses the price value to a double.
   *
   * @param name        the unique name of an item
   * @param ingredients a String that has all the ingredients separated
   *                    by a coma
   * @param price       a String that holds the value of the price
   * @param imgPath     the path of the selected image
   */
  public void addItem(String name, String ingredients, String price,
      String imgPath)
  {
    if (name.isBlank() || ingredients.isBlank() || price.isBlank())
    {
      printErrorMessage("Empty field");
    }
    else
    {
      try
      {
        if (Double.parseDouble(price) < 0)
        {
          printErrorMessage("Incorrect price format");
          return;
        }
        menuModel.addItem(name, separateIngredients(ingredients),
            Double.parseDouble(price), imgPath);
      }
      catch (NumberFormatException e) // idk what about this but it did not catch the letters
      {
        printErrorMessage("Incorrect price format");
      }

      Log.log("MenuEmplViewModel calls the addItem method on the MenuModel");
    }
  }

  /**
   * A private method that takes the String containing all ingredients separated
   * by commas and adds each individual ingredient to an ArrayList 7. This is
   * done with a Regex Pattern and a matcher that groups each ingredient
   * one by one.
   *
   * @param ingredients a String that has all the ingredients separated
   *                    *               by a coma
   * @return an ArrayList containing all separated ingredients
   */
  private ArrayList<String> separateIngredients(String ingredients)
  {
    // The pattern will group word/words that do not have comma in between
    String pattern = "((\\w|\\s)+),?";

    // Create a Pattern object
    Pattern r = Pattern.compile(pattern);

    // Now create matcher object.
    Matcher m = r.matcher(ingredients);

    ArrayList<String> ingr = new ArrayList<>();

    // Add each ingredient (without the comma or any space) to the ArrayList
    while (m.find())
    {
      System.out.println("Found value: " + m.group(1).trim());
      ingr.add(m.group(1).trim());
    }

    return ingr;
  }

  /**
   * A method that catches the event fired by the MenuModel class and
   * gets the value of the ErrorMessage object that has been sent.
   * The method calls the private method printErrorMessage to safely modify
   * the private String errorMessage.
   *
   * @param event the event that has been fired by the MenuModel class
   */
  private void errorReceived(PropertyChangeEvent event)
  {
    ErrorMessage errorMess = (ErrorMessage) event.getNewValue();

    printErrorMessage(errorMess.getMessage());

    Log.log("MenuEmplViewModel receives an error Message");
  }

  /**
   * Safely modifies the errorMessage private variable
   *
   * @param message the value of the ErrorMessage that has been sent by the
   *                MenuModel class
   */
  private void printErrorMessage(String message)
  {
    Platform.runLater(() -> errorMessage.setValue("Error: " + message));
  }

}
