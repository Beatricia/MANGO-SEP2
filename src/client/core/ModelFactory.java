package client.core;

import client.model.*;
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
      Log.log("UserModel was created");
      userModel = new UserModelImp(clientFactory.getClient());
    }
    return userModel;
  }

  public MenuModel getMenuModel()
  {
    if(menuModel == null)
    {
      Log.log("MenuModel was created");
      menuModel = new MenuModelImp(clientFactory.getClient());
    }
    return menuModel;
  }

  public AdminModel getAdminModel() {
    if(adminModel == null)
    {
      Log.log("AdminModel was created");
      adminModel = new AdminModelImp(clientFactory.getClient());
    }
    return adminModel;
  }
}
