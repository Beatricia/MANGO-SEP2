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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import shared.Log;
import transferobjects.CartItem;
import transferobjects.MenuItem;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

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

    private ShoppingCartViewModel viewModel;

    @Override
    public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
        viewModel = viewModelFactory.getCustomerShoppingCartViewModel();
        cartTable.setItems(viewModel.getAllCartItems());
        cartTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        loadCartItems();


        //spinner thing
        SpinnerValueFactory<Integer> quantityValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,8);
        this.quantitySpinner.setValueFactory(quantityValueFactory);
        //everytime i click on another item from my shopping cart
        cartTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedItemChange(newSelection);// the new cart item selected
        });
    }

    private void loadCartItems(){
        ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
        cartItems.addAll(viewModel.getAllCartItems());
        for (int i = 0; i < cartItems.size(); i++) {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
            cartTable.setItems(cartItems);
        }
    }

    private void selectedItemChange(CartItem cartItem) {

        ArrayList<String> ingredients = cartItem.getIngredients();
        nameLabel.setText(cartItem.getName());
        priceLabel.setText(cartItem.getPrice()+" dkk");

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

    public void onOrderButton(ActionEvent actionEvent) {
        Log.log("Order button has been clicked");
        CartItem cartItem = cartTable.getSelectionModel().getSelectedItem();
        onSaveButton();
        ObservableList<CartItem> cartItems = cartTable.getItems();
        ArrayList<CartItem> cartItems1 = new ArrayList<>(cartItems);
        viewModel.placeOrder();
    }

    public void onSaveButton() {
        Log.log("Save button has been clicked in the shopping cart");
        CartItem cartItem = cartTable.getSelectionModel().getSelectedItem();
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
    }

    public void onRemoveButton(ActionEvent actionEvent) {
        Log.log("Delete button has been clicked in the shopping cart");
        CartItem cartItemToBeDeleted = cartTable.getSelectionModel().getSelectedItem();
        viewModel.deleteCartItem(cartItemToBeDeleted);
    }
}

