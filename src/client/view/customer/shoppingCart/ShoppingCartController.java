package client.view.customer.shoppingCart;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;

public class ShoppingCartController implements ViewController {
    @FXML public TableColumn nameColumn;
    @FXML public TableColumn quantityColumn;
    @FXML public Label totalPriceLabel;
    @FXML public ImageView imageView;
    @FXML public Label nameLabel;
    @FXML public Label priceLabel;
    @FXML public Spinner quantitySpinner;

    private ShoppingCartViewModel viewModel;

    @Override
    public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
        viewModel = viewModelFactory.getCustomerShoppingCartViewModel();
    }

    @Override
    public void refresh() {

    }

    @FXML
    public void onOrderButton(ActionEvent actionEvent) {
    }

    @FXML
    public void onSaveButton(ActionEvent actionEvent) {
    }

    @FXML
    public void onRemoveButton(ActionEvent actionEvent) {
    }


}
