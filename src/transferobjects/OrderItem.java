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
     * @param name                  item's name
     * @param ingredients           list of item's ingredients
     * @param price                 item's price
     * @param imgPath               item's image path
     * @param username              the username of the order's authod
     * @param quantity              item's quantity
     * @param unselectedIngredients the unselected ingredients
     * @param code                  the order's code
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


    /**
     * Getting the unselected ingredients
     * @return the ingredients without a comma
     */
    public String getUnselected()
    {
        String str= "";
        ArrayList<String> ing = getUnselectedIngredients();

        for (int i = 0; i < ing.size(); i++)
        {
           str += ing.get(i) + ", ";
        }

       return str.substring(0, str.length()-2);
    }

}
