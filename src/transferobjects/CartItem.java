package transferobjects;

import java.util.ArrayList;

public class CartItem extends MenuItem{
    private String username;
    private int quantity;
    private ArrayList<String> unselectedIngredients;

    /**
     * Construct the MenuItem object
     * needed to transfer menu item
     *
     * @param name        item's name
     * @param ingredients list of item's ingredients
     * @param price       item's price
     * @param imgPath
     */
    public CartItem(String name, ArrayList<String> ingredients, double price, String imgPath, String username, int quantity, ArrayList<String> unselectedIngredients) {
        super(name, ingredients, price, imgPath);
        this.username = username;
        this.quantity = quantity;
        this.unselectedIngredients = unselectedIngredients;
    }

    public String getUsername(){
        return username;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity=quantity;
    }

    public ArrayList<String> getUnselectedIngredients(){
        return unselectedIngredients;
    }


}
