package client.view.admin.purchaseHistory;

import client.core.ViewModelFactory;
import client.view.TabController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import transferobjects.OrderItem;

import java.util.ArrayList;

/**
 * Controller for the PurchaseHistoryView
 */
public class PurchaseHistoryViewController implements TabController
{
  @FXML public TableView<ArrayList<OrderItem>> ordersTable;
  @FXML public TableColumn<ArrayList<OrderItem>, String > dateColumn;
  @FXML public TableColumn<ArrayList<OrderItem>, String > usernameColumn;
  @FXML public TableColumn<ArrayList<OrderItem>, String > itemsColumn;
  @FXML public TableColumn<ArrayList<OrderItem>, String > priceColumn;

  private PurchaseHistoryViewModel viewModel;
  private ViewModelFactory viewModelFactory;

  /**
   * Method that initializes the controller
   * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
   */
  @Override public void init(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
    viewModel = viewModelFactory.getPurchaseHistoryViewModel();

    ordersTable.setItems(viewModel.getAllOrders());

    dateColumn.setCellValueFactory(orders ->{
      ArrayList<OrderItem> orderItems = orders.getValue();
      return new SimpleStringProperty(orderItems.get(0).getDate()+"");
    });

    usernameColumn.setCellValueFactory(orders ->{
      ArrayList<OrderItem> orderItems = orders.getValue();
      return new SimpleStringProperty(orderItems.get(0).getUsername()+"");
    });

    itemsColumn.setCellValueFactory(orders ->{
      ArrayList<OrderItem> orderItems = orders.getValue();
      String items = "";
      for (int i = 0; i < orderItems.size(); i++)
      {
        items += orderItems.get(i).getName() + ", ";
      }

      items = items.substring(0, items.length()-2);

      return new SimpleStringProperty(items);
    });

    priceColumn.setCellValueFactory(orders -> {
      ArrayList<OrderItem> orderItems = orders.getValue();
      double total = 0;
      for (OrderItem orderItem : orderItems) {
        total += orderItem.getPrice() * orderItem.getQuantity();
      }
      return new SimpleStringProperty(total+"");
    });


  }

  @Override public void refresh()
  {
      viewModel.requestPurchaseHistory();
  }



}
