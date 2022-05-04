package client.core;

import client.model.MenuModel;
import client.model.MenuModelImp;
import client.model.UserModel;
import client.model.UserModelImp;
import client.view.MenuEmpl.MenuEmplViewModel;

/**
 * A class that creates a model object and returns it#
 * @author Uafa
 */
public class ModelFactory
{
  private ClientFactory clientFactory;
  private UserModel userModel;
  private MenuModel menuModel;

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
      userModel = new UserModelImp(clientFactory.getClient());
    }
    return userModel;
  }

  public MenuModel getMenuModel()
  {
    if(menuModel == null)
    {
      menuModel = new MenuModelImp(clientFactory.getClient());
    }
    return menuModel;
  }
}
