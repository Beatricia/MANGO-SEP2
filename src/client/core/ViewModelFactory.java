package client.core;

import client.view.admin.AcceptEmployee.AcceptEmployeeViewModel;
import client.view.customer.displayMenu.DisplayMenuViewModel;
import client.view.customer.displayWeeklyMenu.CustomerWeeklyMenuViewModel;
import client.view.customer.myOrder.MyOrderViewModel;
import client.view.customer.shoppingCart.ShoppingCartViewModel;
import client.view.employee.AddQuantity.AddQuantityViewModel;
import client.view.employee.WeeklyMenu.WeeklyMenuEmpViewModel;
import client.view.login.LoginViewModel;
import client.view.employee.AddDish.MenuEmplViewModel;
import client.view.employee.DailyMenu.DailyMenuViewModel;
import client.view.Register.RegisterViewModel;
import shared.Log;

//TODO javadocs

/**
 * A class that returns the different types if view models the system uses
 * @author Uafa
 */
public class ViewModelFactory
{
  private final ModelFactory modelFactory;

  // User
  private LoginViewModel loginViewModel;
  private RegisterViewModel registerViewModel;

  // Customer
  private CustomerWeeklyMenuViewModel customerWeeklyMenuViewModel;
  private DisplayMenuViewModel displayMenuViewModel;
  private ShoppingCartViewModel shoppingCartViewModel;
  private MyOrderViewModel myOrderViewModel;

  // Employee
  private AddQuantityViewModel addQuantityViewModel;
  private DailyMenuViewModel dailyMenuViewModel;
  private MenuEmplViewModel menuEmplViewModel;
  private WeeklyMenuEmpViewModel weeklyMenuEmpViewModel;

  // Admin
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

  public DisplayMenuViewModel getDisplayMenuViewModel() {
    if(displayMenuViewModel == null){
      Log.log("ViewModelFactory: DisplayMenu ViewModel created");

      displayMenuViewModel = new DisplayMenuViewModel(modelFactory.getMenuModel(), modelFactory.getCartModel());
    }
    return displayMenuViewModel;
  }

  public AddQuantityViewModel getAddQuantityViewModel()
  {
    if(addQuantityViewModel == null){
      Log.log("ViewModelFactory: DisplayMenu ViewModel created");

      addQuantityViewModel = new AddQuantityViewModel(modelFactory.getMenuModel());
    }
    return addQuantityViewModel;
  }

  public CustomerWeeklyMenuViewModel getCustomerWeeklyMenuViewModel() {
    if(customerWeeklyMenuViewModel == null){
      Log.log("ViewModelFactory: CustomerWeeklyMenu ViewModel created");

      customerWeeklyMenuViewModel = new CustomerWeeklyMenuViewModel(modelFactory.getMenuModel());
    }
    return customerWeeklyMenuViewModel;
  }

  public WeeklyMenuEmpViewModel getWeeklyMenuEmpViewModel()
  {
    if (weeklyMenuEmpViewModel == null)
    {
      Log.log("ViewModelFactory: EmployeeWeeklyMenu ViewModel created");

      weeklyMenuEmpViewModel = new WeeklyMenuEmpViewModel(modelFactory.getMenuModel());
    }
    return weeklyMenuEmpViewModel;
  }

    public ShoppingCartViewModel getCustomerShoppingCartViewModel() {
    if(shoppingCartViewModel == null)
    {
      Log.log("ViewModelFactory: CustomerShoppingCart ViewModel created");

      shoppingCartViewModel = new ShoppingCartViewModel(modelFactory.getCartModel());
    }
    return shoppingCartViewModel;
    }

  public MyOrderViewModel getCustomerMyOrderViewModel() {
    if(myOrderViewModel == null)
    {
      Log.log("ViewModelFactory: CustomerMyOrder ViewModel created");

      myOrderViewModel = new MyOrderViewModel(modelFactory.getOrderModelCustomer());
    }
    return myOrderViewModel;
  }
}
