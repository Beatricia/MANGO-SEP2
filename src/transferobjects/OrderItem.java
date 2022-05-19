package transferobjects;

import java.time.LocalDate;
import java.util.ArrayList;

public class OrderItem extends CartItem{
    private LocalDate date;
    private int code;

    /**
     * Construct the MenuItem object
     * needed to transfer menu item
     *
     * @param name                  item's name
     * @param ingredients           list of item's ingredients
     * @param price                 item's price
     * @param imgPath
     * @param username
     * @param quantity
     * @param unselectedIngredients
     */
    public OrderItem(String name, ArrayList<String> ingredients, double price, String imgPath, String username, int quantity, ArrayList<String> unselectedIngredients, int code) {
        super(name, ingredients, price, imgPath, username, quantity, unselectedIngredients);
        this.code=code;
        date = LocalDate.now();
    }

    public int getCode(){
        return code;
    }

}
