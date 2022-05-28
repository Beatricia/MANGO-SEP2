package client.view.admin.purchaseHistory;

import client.model.AdminModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.Log;
import transferobjects.OrderItem;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 * A view model class responsible for connecting the PurchaseHistoryController
 * and the AdminModel
 */
public class PurchaseHistoryViewModel
{
  private AdminModel adminModel;
  private ObservableList<ArrayList<OrderItem>> orders;


  /**
   * A constructor for the class
   * @param adminModel the model that the view should be connected to
   */
  public PurchaseHistoryViewModel(AdminModel adminModel)
  {
    this.adminModel= adminModel;
    orders = FXCollections.observableArrayList();

    adminModel.addListener(AdminModel.PURCHASE_HISTORY_RECEIVED, this:: ordersReceived);
  }

  /**
   * The method listens to the AdminModel and whenever it fires a
   * PURCHASE_HISTORY_RECEIVED property, this method fills in the private
   * ObservableList<ArrayList<OrderItem>> attribute with the received data.
   * @param event the event that has been sent by the OrderModel
   */
  private void ordersReceived(PropertyChangeEvent event)
  {
    Log.log("PurchaseHistoryViewModel: All orders received");

    ArrayList<ArrayList<OrderItem>> ordersReceived = (ArrayList<ArrayList<OrderItem>>) event.getNewValue();

    Platform.runLater( () -> {

      this.orders.clear();
      this.orders.addAll(ordersReceived);
    });
  }

  /**
   * The method returns all orders
   * @return all orders
   */
  public ObservableList<ArrayList<OrderItem>> getAllOrders()
  {
    return orders;
  }

  /**
   * The method request the purchase history from the AdminModel class
   */
  public void requestPurchaseHistory()
  {
    adminModel.requestPurchaseHistory();
  }
}
