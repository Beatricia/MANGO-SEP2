package transferobjects;

import java.util.ArrayList;
/**
 * The class representing a CartItem that inherits the CartItem
 * It contains a username, quantity and an array list of unselected ingredients
 * @version 1
 */

public class CartItem extends MenuItem{
    private String username;
    private int quantity;
    private ArrayList<String> unselectedIngredients;

    /**
     * Constructor for initializing every CartItem's field and the super class
     * @param name          item's name.
     * @param ingredients   list of item's ingredients, if its null, a new list will be created.
     * @param price         item's price.
     * @param imgPath       item's image path.
     * @param username      the username of the cart's author.
     * @param quantity      item's quantity.
     * @param unselectedIngredients list of item's unselected ingredients.
     * @throws NullPointerException if the name is null. (message: item name is null)
     * @throws IllegalArgumentException if the name is blank (message: item name is blank).
     * @throws IllegalArgumentException if the price is less than or equal to zero (message: price must be higher than 0).
     * @throws NullPointerException if the username is null (message: username is null)
     * @throws IllegalArgumentException if the username is blank (message: username is blank)
     * @throws IllegalArgumentException if the quantity is less than or equals to zero (message: quantity is less than or equals to zero)
     * @throws IllegalArgumentException if the unselected ingredient is not in the ingredients list (message: unselected ingredient "ingredient" is not present in the ingredients list)
     */
    public CartItem(String name, ArrayList<String> ingredients, double price,
        String imgPath, String username, int quantity, ArrayList<String> unselectedIngredients) {
        super(name, ingredients, price, imgPath);

        if(username == null)
            throw new NullPointerException("username is null");
        if(username.isBlank())
            throw new IllegalArgumentException("username is blank");

        if(unselectedIngredients == null) {
            unselectedIngredients = new ArrayList<>();
        }
        else{
            for (String ingr : unselectedIngredients) {
                if (!ingredients.contains(ingr))
                    throw new IllegalArgumentException("unselected ingredient " + ingr + " is not present in the ingredients list");
            }
        }

        setQuantity(quantity);
        this.username = username;
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
     * @throws IllegalArgumentException if the quantity is less than or equals to zero (message: quantity is less than or equals to zero)
     */
    public void setQuantity(int quantity){
        if(quantity <= 0)
            throw new IllegalArgumentException("quantity is less than or equals to zero");

        this.quantity = quantity;
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
