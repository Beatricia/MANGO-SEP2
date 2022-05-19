package client.view.customer.myOrder;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import client.view.customer.displayWeeklyMenu.WeeklyMenuController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import shared.Log;
import transferobjects.MenuItemWithQuantity;
import transferobjects.OrderItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Controller for MyOrderView.fxml
 * @author Agata
 */

public class MyOrderController implements ViewController
{

  public Label dateLabel;
  public Label codeLabel;
  public TableView<OrderItem> table;
  public Label priceLabel;
  public TableColumn<OrderItem, String> nameColumn;
  public TableColumn<OrderItem, ArrayList<String>> ingredientsColumn;
  public TableColumn<OrderItem, Integer> quantityColumn;

  private MyOrderViewModel viewModel;

  /**
   * Initializes the controller
   * @param viewHandler instance of ViewHandler class, which is responsible for managing the GUI views
   * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
   */

  @Override public void init(ViewHandler viewHandler,
      ViewModelFactory viewModelFactory)
  {
    viewModel = viewModelFactory.getCustomerMyOrderViewModel();

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    ingredientsColumn.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    quantityColumn.setStyle("-fx-alignment: CENTER;");
    listView();


    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    dateLabel.setText(today);

    codeLabel.setText(String.format("%06d", viewModel.getOrderCode()));

    priceLabel.setText(viewModel.getTotalPrice() + " dkk");
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
    viewModel.cancelOrder();
  }

  /**
   * loading the orderItems into the table
   */

  private void listView()
  {
    ObservableList<OrderItem> orderItemsList = viewModel.getAllOrderItems();
    /*ArrayList<String> ingredient = new ArrayList<>();
    ingredient.add("banana");
    ingredient.add("onion");
    ingredient.add("tomato");
    OrderItem item1 = new OrderItem("Agata",ingredient,13,null,null,3,null,13);
    OrderItem item2 = new OrderItem("Bartel",ingredient,15,null,null,2,null,13);
    OrderItem item3 = new OrderItem("Maciek",ingredient,17,null,null,3,null,13);*/

    for (int i = 0; i <orderItemsList.size() ; i++)
    {
      table.getItems().add(orderItemsList.get(i));
    }
  }
}
