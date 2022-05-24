package client.view.employee.CollectOrder;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import transferobjects.OrderItem;

import java.util.ArrayList;

/**
 * Controller for the CollectOrderView
 */
public class CollectOrderViewController implements ViewController
{
  @FXML public TableView<ArrayList<OrderItem>> ordersTable;
  @FXML public TableColumn<ArrayList<OrderItem>, String> orderCode;
  @FXML public TableColumn<ArrayList<OrderItem>, String> orderPrice;

  @FXML public TableView<OrderItem> detailsTable;
  @FXML public TableColumn<OrderItem, String> itemName;
  @FXML public TableColumn<OrderItem, String> unselectedIngredients;
  @FXML public TableColumn<OrderItem, Integer> quantity;
  @FXML public Button button;

  private CollectOrderViewModel viewModel;

  /**
   * Method that initalizes the controller
   * @param viewHandler instance of ViewHandler class, which is responsible for managing the GUI views
   * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
   */
  @Override public void init(ViewHandler viewHandler,
      ViewModelFactory viewModelFactory)
  {
    viewModel = viewModelFactory.getCollectOrderViewModel();


    ordersTable.setItems(viewModel.getAllOrders());
    ordersTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);



    orderCode.setCellValueFactory(itemList -> {
      ArrayList<OrderItem> orderItems = itemList.getValue();
      return new SimpleObjectProperty<>(orderItems == null ? null: orderItems.get(0).getCode()+"");
    });


    orderPrice.setCellValueFactory(itemList -> {
      ArrayList<OrderItem> orderItems = itemList.getValue();
      double total = 0;
      for (int i = 0; i < orderItems.size(); i++)
      {
        total += orderItems.get(i).getPrice() * orderItems.get(i).getQuantity();
      }
      return new SimpleObjectProperty<>(total+"");
     });

    
    ordersTable.getSelectionModel().selectedItemProperty().addListener((observableValue, orderItems, newSelection) -> {
      selectedOrderChange(newSelection);
    });
    button.setDisable(true);

  }

  /**
   * A method responsible with filling in the detailTable according to which
   * order has been selected in the orderTable
   * @param orderItemArrayList The order that has been selected
   */
  private void selectedOrderChange(ArrayList<OrderItem> orderItemArrayList)
  {

    ObservableList<OrderItem> list = FXCollections.observableArrayList(orderItemArrayList);
    detailsTable.setItems(list);
    itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
    unselectedIngredients.setCellValueFactory(unselected -> {
      OrderItem item = unselected.getValue();
      if(item.getUnselectedIngredients().size() ==0)
      {
        return new SimpleObjectProperty<>("");
      }
      else
        return new SimpleObjectProperty<>(item.getUnselected());});


    quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    button.setDisable(false);
  }

  /**
   * Calls the  requestUncollectedOrders() method in the view model to refresh
   * the data in the view
   */
  @Override public void refresh()
  {
    viewModel.requestUncollectedOrders();
  }

  /**
   * When an order is selected its code is sent to the view model so the
   * order can be canceled
   */
  @FXML void onCollect()
  {
    ArrayList<OrderItem> orderItems = ordersTable.getSelectionModel().selectedItemProperty().get();
    viewModel.cancelOrder(orderItems.get(0).getCode());

  }
}
