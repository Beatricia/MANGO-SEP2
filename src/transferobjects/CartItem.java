package transferobjects;

import java.util.ArrayList;
/**
 * The class representing a CartItem that inherits the CartItem
 * It contains a username, quantity and an array list of unselected ingredients
 * @author
 * @version 1
 */

public class CartItem extends MenuItem{
    private String username;
    private int quantity;
    private ArrayList<String> unselectedIngredients;

    /**
     * Constructor for initializing every CartItem's field and the super class
     * @param name          item's name
     * @param ingredients   list of item's ingredients
     * @param price         item's price
     * @param imgPath       item's image path
     * @param username      the username of the cart's author
     * @param quantity      item's quantity
     * @param unselectedIngredients list of item's unselected ingredients
     */
    public CartItem(String name, ArrayList<String> ingredients, double price,
        String imgPath, String username, int quantity, ArrayList<String> unselectedIngredients) {
        super(name, ingredients, price, imgPath);
        this.username = username;
        this.quantity = quantity;
        if(unselectedIngredients == null)
            unselectedIngredients = new ArrayList<>();

        this.unselectedIngredients = unselectedIngredients;
    }

    /**
     * Getting the username
     * @return the username
     */
    public String getUsername(){
        return username;
    }

    /**
     * Getting the quantity
     * @return the quantity
     */
    public int getQuantity(){
        return quantity;
    }

    /**
     * Setting the quantity
     * @param quantity the quantity to be set
     */
    public void setQuantity(int quantity){
        this.quantity=quantity;
    }

    /**
     * Getting the array list of all unselected ingredients
     * @return an array list of unselected ingredients
     */
    public ArrayList<String> getUnselectedIngredients(){
        return unselectedIngredients;
    }

    @Override public String toString()
    {
        return  super.toString() + "CartItem{" + "username='" + username + '\'' + ", quantity="
            + quantity + ", unselectedIngredients=" + unselectedIngredients
            + '}';
    }
}
