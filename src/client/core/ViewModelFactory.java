package client.core;

import client.view.Admin.acceptEmployee.AcceptEmployeeViewModel;
import client.view.Login.LoginViewModel;
import client.view.MenuEmpl.AddDish.MenuEmplViewModel;
import client.view.MenuEmpl.DailyMenu.DailyMenuViewModel;
import client.view.Register.RegisterViewModel;
import shared.Log;

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
  private DailyMenuViewModel dailyMenuViewModel;
  private AcceptEmployeeViewModel acceptEmployeeViewModel;

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
      Log.log("ViewModelFactory: Log-In ViewModel created");
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
      Log.log("ViewModelFactory: Register ViewModel created");

      registerViewModel = new RegisterViewModel(modelFactory.getUserModel());
    }
    return registerViewModel;
  }

  //bety
  public MenuEmplViewModel getMenuEmplViewModel(){
    if(menuEmplViewModel == null)
    {
      Log.log("ViewModelFactory: MenuEmpl ViewModel created lol");
      menuEmplViewModel = new MenuEmplViewModel(modelFactory.getMenuModel());
    }
    return menuEmplViewModel;
  }

  public DailyMenuViewModel getDailyMenuViewModel()
  {
    if(dailyMenuViewModel == null)
    {
      Log.log("ViewModelFactory: DailyMenu ViewModel created");

      dailyMenuViewModel = new DailyMenuViewModel(modelFactory.getMenuModel());
    }
    return dailyMenuViewModel;
  }

  public AcceptEmployeeViewModel getAcceptEmployeeViewModel() {
    if(acceptEmployeeViewModel == null)
    {
      Log.log("ViewModelFactory: AcceptEmployee ViewModel created");

      acceptEmployeeViewModel = new AcceptEmployeeViewModel(modelFactory.getAdminModel());
    }
    return acceptEmployeeViewModel;
  }
}
