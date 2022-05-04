package client.core;

import client.view.Login.LoginViewModel;
import client.view.MenuEmpl.MenuEmplViewModel;
import client.view.Register.RegisterViewModel;

/**
 * A class that returns the different types if view models the system uses
 * @author Uafa
 */
public class ViewModelFactory
{
  private ModelFactory modelFactory;
  private LoginViewModel loginViewModel;
  private RegisterViewModel registerViewModel;
  private MenuEmplViewModel menuEmplViewModel;

  /**
   * Constructor for the class
   * @param modelFactory a ModelFactory object
   */
  public ViewModelFactory(ModelFactory modelFactory)
  {
    this.modelFactory = modelFactory;
  }

  /**
   * Using lazy instantiation creates a LoginViewModel object and returns it
   * @return a LoginViewModel object with the UserModel as its parameter
   */
  public LoginViewModel getLoginViewModel()
  {
    if(loginViewModel == null)
    {
      loginViewModel = new LoginViewModel(modelFactory.getUserModel());
    }
    return loginViewModel;
  }


  /**
   * Using lazy instantiation creates a RegisterViewModel object and returns it
   * @return a RegisterViewModel object with the UserModel as its parameter
   */
  public RegisterViewModel getRegisterViewModel()
  {
    if(registerViewModel == null)
    {
      registerViewModel = new RegisterViewModel(modelFactory.getUserModel());
    }
    return registerViewModel;
  }

  //bety
  public MenuEmplViewModel getMenuEmplViewModel(){
    if(menuEmplViewModel == null)
    {
      menuEmplViewModel = new MenuEmplViewModel(modelFactory.getMenuModel());
    }
    return menuEmplViewModel;
  }
}
