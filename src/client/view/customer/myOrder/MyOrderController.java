package client.view.customer.myOrder;

import client.core.ViewModelFactory;
import client.view.TabController;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import shared.Log;
import transferobjects.OrderItem;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;


/**
 * Controller for MyOrderView.fxml
 * @author Agata
 */

public class MyOrderController implements TabController
{

  public Label dateLabel;
  public Label codeLabel;
  public TableView<OrderItem> table;
  public Label priceLabel;
  public TableColumn<OrderItem, String> nameColumn;
  public TableColumn<OrderItem, String> ingredientsColumn;
  public TableColumn<OrderItem, Integer> quantityColumn;
  public Button cancelButton;

  private MyOrderViewModel viewModel;

  /**
   * Initializes the controller
   * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
   */

  @Override public void init(ViewModelFactory viewModelFactory)
  {
    viewModel = viewModelFactory.getCustomerMyOrderViewModel();

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    ingredientsColumn.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
    ingredientsColumn.setCellValueFactory(
        obj -> {
          OrderItem orderItem = obj.getValue();
          ArrayList<String> selectedIngredients = new ArrayList<>(orderItem.getIngredients());
          selectedIngredients.removeAll(orderItem.getUnselectedIngredients());

          String list = String.join(", ", selectedIngredients);
          return new SimpleStringProperty(list);
        });
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    quantityColumn.setStyle("-fx-alignment: CENTER;");
    table.setItems(viewModel.getAllOrderItems());

    viewModel.getAllOrderItems().addListener((InvalidationListener) obj ->
    {
      if (viewModel.getAllOrderItems().size() == 0)
      {
        cancelButton.setDisable(true);
      }
      else {
        cancelButton.setDisable(false);
      }
    });


    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    dateLabel.setText(today);

    codeLabel.textProperty().bind(viewModel.getOrderCode());
    priceLabel.textProperty().bind(viewModel.getTotalPrice());

    if (viewModel.getAllOrderItems().size() == 0)
    {
      cancelButton.setDisable(true);
    }
    else {
      cancelButton.setDisable(false);
    }
  }

  @Override public void refresh()
  {
    viewModel.refresh();
  }

  /**
   * If the cancel button is clicked, the onCancel method is called in case the whole order should be deleted
   * method is called on the viewModel
   */
  public void onCancel(ActionEvent actionEvent)
  {
    Log.log("MyOrderController: button canceled was pressed");

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setContentText("Do you really want to cancel your order?");
    Optional<ButtonType> optional = alert.showAndWait();

    if(optional.isPresent()){
      if(optional.get().getButtonData().isCancelButton())
        return;
    }

    viewModel.cancelOrder();
    refresh();
  }

}
