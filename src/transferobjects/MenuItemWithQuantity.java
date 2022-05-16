package transferobjects;

import java.time.LocalDate;

public class MenuItemWithQuantity extends MenuItem {
  private LocalDate date;
  private int quantity;

  public MenuItemWithQuantity(MenuItem item, LocalDate date, int quantity ) {
    super(item.getName(), item.getIngredients(), item.getPrice(), item.getImgPath());
    this.date=date;
    this.quantity=quantity;
  }

  public LocalDate getDate(){
    return  date;
  }

  public void setQuantity(int quantity){
    this.quantity=quantity;
  }

  public int getQuantity(){
    return quantity;
  }

  @Override public String toString()
  {
    return getName();
  }
}
