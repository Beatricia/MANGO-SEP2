package client.view.customer.shoppingCart;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.model.CartModel;
import client.view.ViewController;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import shared.Log;
import transferobjects.CartItem;
import transferobjects.MenuItem;

import java.lang.reflect.Field;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for ShoppingCartView.fxml
 * @author Beatricia
 */
public class ShoppingCartController implements ViewController {
    @FXML public TableColumn<CartItem, String> nameColumn;
    @FXML public TableColumn<CartItem, Integer> quantityColumn;
    @FXML public Label totalPriceLabel;
    @FXML public ImageView imageView;
    @FXML public Label nameLabel;
    @FXML public Label priceLabel;
    @FXML public Spinner quantitySpinner;
    public TableView<CartItem> cartTable;
    public ScrollPane ingredientsScrollPane;
    public VBox ingredientsVBox;
    public Pane detailsPane;
    private String lastSelectedItem;

    private ShoppingCartViewModel viewModel;

    /**
     * Initializes the controller
     * @param viewHandler instance of ViewHandler class, which is responsible for managing the GUI views
     * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
     */
    @Override
    public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
        viewModel = viewModelFactory.getCustomerShoppingCartViewModel();
        cartTable.setItems(viewModel.getAllCartItems());
        cartTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        //loadCartItems();

        viewModel.getAllCartItems().addListener(this::listChange);

        clearDetailsPane();

        //spinner thing
        SpinnerValueFactory<Integer> quantityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,8);
        this.quantitySpinner.setValueFactory(quantityValueFactory);
        //everytime i click on another item from my shopping cart
        cartTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedItemChange(newSelection);// the new cart item selected
        });
    }

    private void listChange(Observable observable) {
        List<CartItem> cartItems = (List<CartItem>) observable;
        for (CartItem c : cartItems)
        {
            if(c.getName().equals(lastSelectedItem))
                cartTable.getSelectionModel().select(c);
        }
    }

    private void clearDetailsPane(){
        if(cartTable.getSelectionModel().isEmpty()){
            detailsPane.getChildren().clear();
        }
        else detailsPane.getChildren();

    }
    /**
     * Loading all cart items to the table, they are taken from the view model
     */
    private void loadCartItems(){
        ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
        cartItems.addAll(viewModel.getAllCartItems());
        for (int i = 0; i < cartItems.size(); i++) {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
            cartTable.setItems(cartItems);
        }
    }

    /**
     * If the cartItem is selected in the table it will appear on the right side
     * with name, price, image and all the specified ingredient
     * @param cartItem the cart item to be displayed
     */
    private void selectedItemChange(CartItem cartItem) {

        if(cartItem==null){
            return;
        }

        lastSelectedItem = cartItem.getName();
        ArrayList<String> ingredients = cartItem.getIngredients();
        nameLabel.setText(cartItem.getName());
        totalPriceLabel.setText(cartItem.getPrice()+"dkk");
        Log.log("Image path: " + cartItem.getImgPath());
        //Image img = new Image(cartItem.getImgPath());
        //imageView.setImage(img);
        SpinnerValueFactory<Integer> quantityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,8, cartItem.getQuantity());
        this.quantitySpinner.setValueFactory(quantityValueFactory);

        ingredientsVBox.getChildren().clear();
        for (int i = 0; i < ingredients.size(); i++) {
            String ingredient = ingredients.get(i);

            CheckBox checkBox = new CheckBox(ingredient);
            if(cartItem.getUnselectedIngredients().contains(ingredient)){
                checkBox.setSelected(false);
            }
            else
                checkBox.setSelected(true);

            ingredientsVBox.getChildren().add(checkBox);
        }
    }

    @Override
    public void refresh() {
        viewModel.refresh();
    }

    /**
     * If the order button is clicked, the onSaveButton method is called in case there are any changes and the placeOrder
     * method is called on the viewModel
     */
    public void onOrderButton() {
        Log.log("Order button has been clicked");
        onSaveButton();
        viewModel.placeOrder();
    }

    /**
     * If the save button is clicked, the current item where the changes were made is saved in a cartItem,
     * then the quantity for the cartItem is set through the spinners value.
     * The list of all unselectedIngredients is cleared for the cartItem and by running through every ingredient we are
     * checking if it is not ticket, if not, it is added to the unselectedIngredients with their name
     */
    public void onSaveButton() {
        Log.log("Save button has been clicked in the shopping cart");
        CartItem cartItem = cartTable.getSelectionModel().getSelectedItem();
        if(cartItem == null) {
            return;
        }
        cartItem.setQuantity((Integer) quantitySpinner.getValue());
        cartItem.getUnselectedIngredients().clear();

        for (int i = 0; i < ingredientsVBox.getChildren().size(); i++) {
            CheckBox checkBox = (CheckBox) ingredientsVBox.getChildren().get(i);
            String name = checkBox.getText();
            boolean ticked = checkBox.isSelected();
            if(!ticked){
                cartItem.getUnselectedIngredients().add(name);
            }
        }
        viewModel.editCartItem(cartItem);
        refresh();
    }

    /**
     * If the remove button is clicked, the current item saved in a cartItemToBeDeleted of type CartItem and sent to the
     * viewModel through the deleteCartItem method
     */
    public void onRemoveButton() {
        Log.log("Delete button has been clicked in the shopping cart");
        CartItem cartItemToBeDeleted = cartTable.getSelectionModel().getSelectedItem();
        viewModel.deleteCartItem(cartItemToBeDeleted);
        refresh();
    }
}

