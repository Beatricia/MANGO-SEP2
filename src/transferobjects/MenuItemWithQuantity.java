package transferobjects;

import java.time.LocalDate;

/**
 * The class a MenuItemWithQuantity that inherits from MenuItem.
 * It contains a date and a quantity.
 */

public class MenuItemWithQuantity extends MenuItem {
  private LocalDate date;
  private int quantity;
  private boolean isTopThree;

  /**
   * The constructor that initializes the super's fields and the date and quantity
   * @param item the item from the MenuItem
   * @param date the item's date
   * @param quantity the item's quantity
   */
  public MenuItemWithQuantity(MenuItem item, LocalDate date, int quantity ) {
    super(item.getName(), item.getIngredients(), item.getPrice(), item.getImgPath());
    this.date=date;
    this.quantity=quantity;
    isTopThree = false;
  }

  /**
   * Getting the date
   * @return the date
   */
  public LocalDate getDate(){
    return  date;
  }

  /**
   * Setting the quantity
   * @param quantity the quantity to be set
   */
  public void setQuantity(int quantity){
    this.quantity=quantity;
  }

  /**
   * Getting the quantity
   * @return the quantity
   */
  public int getQuantity(){
    return quantity;
  }

  /**
   * Checking if the menu item is in the top three meals
   * @return true if it is
   */
  public boolean isTopThree(){
    return isTopThree;
  }

  /**
   * Marking the menu item as a top three meal
   */
  public void setToTopThree(){
    isTopThree = true;
  }

  /**
   * A string that contains the name of the MenuItemWithQuantity
   * @return the name of the MenuItemWithQuantity
   */
  @Override public String toString()
  {
    return getName();
  }
}
