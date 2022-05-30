package transferobjects;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The class representing a OrderItem that inherits the CartItem
 * It contains a date, that is always the current date, and a code.
 * @author Beatricia
 * @version 1
 */

public class OrderItem extends CartItem{
    private LocalDate date;
    private int code;

    /**
     * Constructor for initializing every OrderItem's field and the super class
     *
     * @param name          item's name.
     * @param ingredients   list of item's ingredients, if its null, a new list will be created.
     * @param price         item's price.
     * @param imgPath       item's image path.
     * @param username      the username of the cart's author.
     * @param quantity      item's quantity.
     * @param unselectedIngredients list of item's unselected ingredients.
     * @param code the order's code
     * @throws NullPointerException if the name is null. (message: item name is null)
     * @throws IllegalArgumentException if the name is blank (message: item name is blank).
     * @throws IllegalArgumentException if the price is less than or equal to zero (message: price must be higher than 0).
     * @throws NullPointerException if the username is null (message: username is null)
     * @throws IllegalArgumentException if the username is blank (message: username is blank)
     * @throws IllegalArgumentException if the quantity is less than or equals to zero (message: quantity is less than or equals to zero)
     * @throws IllegalArgumentException if the unselected ingredient is not in the ingredients list (message: unselected ingredient "ingredient" is not present in the ingredients list)
     */
    public OrderItem(String name, ArrayList<String> ingredients, double price, String imgPath,
        String username, int quantity, ArrayList<String> unselectedIngredients, int code) {

        super(name, ingredients, price, imgPath, username, quantity, unselectedIngredients);
        this.code=code;
        date = LocalDate.now();
    }

    /**
     * Getting the code
     * @return the code
     */
    public int getCode(){
        return  code;
    }

    /**
     * Getting the price
     * @return the price
     */
    public double getPrice()
    {
        return super.getPrice();
    }

    public LocalDate getDate()
    {
        return date;
    }
}
