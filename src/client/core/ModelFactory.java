package client.core;

import client.model.*;
import server.model.ClosingRoutineModel;
import server.model.ClosingRoutineModelImpl;
import shared.Log;

/**
 * A class that creates a model object and returns it#
 * @author Uafa
 */
public class ModelFactory
{
  private ClientFactory clientFactory;
  private UserModel userModel;
  private MenuModel menuModel;
  private AdminModel adminModel;
  private CartModel cartModel;
  private OrderModelCustomer customerOrderModel;

  /**
   * Constructor for the class
   * @param clientFactory takes a ClientFactory as an argument
   */
  public ModelFactory(ClientFactory clientFactory)
  {
    this.clientFactory = clientFactory;
  }

  /**
   * A method that creates a new UserModelImp object and returns it
   * @return returns a UserModelImp object
   */
  public UserModel getUserModel()
  {
    if(userModel == null)
    {
      Log.log("ModelFactory: UserModel was created");
      userModel = new UserModelImp(clientFactory.getClient());
    }
    return userModel;
  }

  /**
   * A method that creates a new MenuModelImp object and returns it
   * @return returns a MenuModelImp object
   */
  public MenuModel getMenuModel()
  {
    if(menuModel == null)
    {
      Log.log("ModelFactory: MenuModel was created");
      menuModel = new MenuModelImp(clientFactory.getClient());
    }
    return menuModel;
  }

  /**
   * A method that creates a new AdminModelImp object and returns it
   * @return returns a AdminModelImp object
   */
  public AdminModel getAdminModel() {
    if(adminModel == null)
    {
      Log.log("ModelFactory: AdminModel was created");
      adminModel = new AdminModelImp(clientFactory.getClient());
    }
    return adminModel;
  }

  /**
   * A method that creates a new CartModelImpl object and returns it
   * @return returns a CartModelImpl object
   */
    public CartModel getCartModel() {
    if(cartModel == null)
    {
      Log.log("ModelFactory: CartModel was created");
      cartModel = new CartModelImpl(clientFactory.getClient());
    }
      return cartModel;
    }

  /**
   * A method that creates a new OrderModelCustomerImp object and returns it
   * @return returns a OrderModelCustomerImp object
   */
  public OrderModelCustomer getOrderModelCustomer()
  {
    if (customerOrderModel == null)
    {
      Log.log("ModelFactory: CustomerOrderModel was created");
      customerOrderModel = new OrderModelCustomerImp(clientFactory.getClient());
    }
    return customerOrderModel;
  }


}
