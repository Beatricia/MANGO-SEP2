package client.core;

import client.view.admin.AcceptEmployee.AcceptEmployeeViewModel;
import client.view.admin.manageCanteen.ManageCanteenViewModel;
import client.view.customer.displayMenu.DisplayMenuViewModel;
import client.view.customer.displayWeeklyMenu.CustomerWeeklyMenuViewModel;
import client.view.customer.myOrder.MyOrderViewModel;
import client.view.customer.shoppingCart.ShoppingCartViewModel;
import client.view.employee.AddQuantity.AddQuantityViewModel;
import client.view.employee.MenuItems.MenuItemsViewModel;
import client.view.employee.CollectOrder.CollectOrderViewModel;
import client.view.employee.WeeklyMenu.WeeklyMenuEmpViewModel;
import client.view.login.LoginViewModel;
import client.view.employee.AddDish.MenuEmplViewModel;
import client.view.employee.DailyMenu.DailyMenuViewModel;
import client.view.register.RegisterViewModel;
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
  private MenuItemsViewModel menuItemsViewModel;
  private CollectOrderViewModel collectOrderViewModel;

  // Admin
  private AcceptEmployeeViewModel acceptEmployeeViewModel;
  private ManageCanteenViewModel manageCanteenViewModel;



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

  /**
   * Using lazy instantiation creates a MenuEmplViewModel object and returns it
   * @return a MenuEmplViewModel object with the UserModel as its parameter
   */
  public MenuEmplViewModel getMenuEmplViewModel(){
    if(menuEmplViewModel == null)
    {
      Log.log("ViewModelFactory: MenuEmpl ViewModel created lol");
      menuEmplViewModel = new MenuEmplViewModel(modelFactory.getMenuModel());
    }
    return menuEmplViewModel;
  }

  /**
   * Using lazy instantiation creates a DailyMenuViewModel object and returns it
   * @return a DailyMenuViewModel object with the UserModel as its parameter
   */
  public DailyMenuViewModel getDailyMenuViewModel()
  {
    if(dailyMenuViewModel == null)
    {
      Log.log("ViewModelFactory: DailyMenu ViewModel created");

      dailyMenuViewModel = new DailyMenuViewModel(modelFactory.getMenuModel());
    }
    return dailyMenuViewModel;
  }

  /**
   * Using lazy instantiation creates a AcceptEmployeeViewModel object and returns it
   * @return a AcceptEmployeeViewModel object with the UserModel as its parameter
   */
  public AcceptEmployeeViewModel getAcceptEmployeeViewModel() {
    if(acceptEmployeeViewModel == null)
    {
      Log.log("ViewModelFactory: AcceptEmployee ViewModel created");

      acceptEmployeeViewModel = new AcceptEmployeeViewModel(modelFactory.getAdminModel());
    }
    return acceptEmployeeViewModel;
  }

  /**
   * Using lazy instantiation creates a DisplayMenuViewModel object and returns it
   * @return a DisplayMenuViewModel object with the UserModel as its parameter
   */
  public DisplayMenuViewModel getDisplayMenuViewModel() {
    if(displayMenuViewModel == null){
      Log.log("ViewModelFactory: DisplayMenu ViewModel created");

      displayMenuViewModel = new DisplayMenuViewModel(modelFactory.getMenuModel(), modelFactory.getCartModel());
    }
    return displayMenuViewModel;
  }

  /**
   * Using lazy instantiation creates a AddQuantityViewModel object and returns it
   * @return a AddQuantityViewModel object with the UserModel as its parameter
   */
  public AddQuantityViewModel getAddQuantityViewModel()
  {
    if(addQuantityViewModel == null){
      Log.log("ViewModelFactory: DisplayMenu ViewModel created");

      addQuantityViewModel = new AddQuantityViewModel(modelFactory.getMenuModel());
    }
    return addQuantityViewModel;
  }

  /**
   * Using lazy instantiation creates a CustomerWeeklyMenuViewModel object and returns it
   * @return a CustomerWeeklyMenuViewModel object with the UserModel as its parameter
   */
  public CustomerWeeklyMenuViewModel getCustomerWeeklyMenuViewModel() {
    if(customerWeeklyMenuViewModel == null){
      Log.log("ViewModelFactory: CustomerWeeklyMenu ViewModel created");

      customerWeeklyMenuViewModel = new CustomerWeeklyMenuViewModel(modelFactory.getMenuModel());
    }
    return customerWeeklyMenuViewModel;
  }

  /**
   * Using lazy instantiation creates a WeeklyMenuEmpViewModel object and returns it
   * @return a WeeklyMenuEmpViewModel object with the UserModel as its parameter
   */
  public WeeklyMenuEmpViewModel getWeeklyMenuEmpViewModel()
  {
    if (weeklyMenuEmpViewModel == null)
    {
      Log.log("ViewModelFactory: EmployeeWeeklyMenu ViewModel created");

      weeklyMenuEmpViewModel = new WeeklyMenuEmpViewModel(modelFactory.getMenuModel());
    }
    return weeklyMenuEmpViewModel;
  }

  /**
   * Using lazy instantiation creates a ShoppingCartViewModel object and returns it
   * @return a ShoppingCartViewModel object with the UserModel as its parameter
   */
    public ShoppingCartViewModel getCustomerShoppingCartViewModel() {
    if(shoppingCartViewModel == null)
    {
      Log.log("ViewModelFactory: CustomerShoppingCart ViewModel created");

      shoppingCartViewModel = new ShoppingCartViewModel(modelFactory.getCartModel());
    }
    return shoppingCartViewModel;
    }

  /**
   * Using lazy instantiation creates a MyOrderViewModel object and returns it
   * @return a MyOrderViewModel object with the UserModel as its parameter
   */
  public MyOrderViewModel getCustomerMyOrderViewModel() {
    if(myOrderViewModel == null)
    {
      Log.log("ViewModelFactory: CustomerMyOrder ViewModel created");

      myOrderViewModel = new MyOrderViewModel(modelFactory.getOrderModelCustomer());
    }
    return myOrderViewModel;
  }

  /**
   * Using lazy instantiation creates a CollectOrderViewModel object and returns it
   * @return a CollectOrderViewModel object with the UserModel as its parameter
   */
  public CollectOrderViewModel getCollectOrderViewModel()
  {
    if(collectOrderViewModel == null)
    {
      Log.log("ViewModelFactory: CollectOrder ViewModel created");

      collectOrderViewModel = new CollectOrderViewModel(modelFactory.getOrderModelCustomer());
    }
    return collectOrderViewModel;
  }

  /**
   * Using lazy instantiation creates a MenuItemsViewModel object and returns it
   * @return a MenuItemsViewModel object with the UserModel as its parameter
   */
  public MenuItemsViewModel getMenuItemsViewModel()
  {
    if (menuItemsViewModel == null)
    {
      menuItemsViewModel = new MenuItemsViewModel(modelFactory.getMenuModel());
    }
    return menuItemsViewModel;
  }

  /**
   * Using lazy instantiation creates a ManageCanteenViewModel object and returns it
   * @return a ManageCanteenViewModel object with the UserModel as its parameter
   */
    public ManageCanteenViewModel getManageCanteenViewModel() {
    if(manageCanteenViewModel == null)
    {
      manageCanteenViewModel = new ManageCanteenViewModel(modelFactory.getAdminModel());
    }
    return manageCanteenViewModel;
    }
}
