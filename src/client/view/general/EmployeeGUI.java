package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import javafx.scene.control.TabPane;
import shared.Log;

/**
 * EmployeeGUI is part of the strategy pattern, specifically this is the strategy when the user
 * is an employee
 * @author Greg
 * @version 1
 */
public class EmployeeGUI extends UserStrategy
{
  private final static String BASE_PATH = "src/client/view/employee/";
  /**
   * All the tabs to load when the logged in user is an employee
   */
  private final static String[] tabs = {
      "AddDish/MenuEmpl.fxml",         "Add items to the menu",
      "DailyMenu/DailyMenuView.fxml",  "Add to daily menu",
      "AddQuantity/AddQuantityView.fxml", "Add quantity",
      "WeeklyMenu/WeeklyMenuEmp.fxml", "Weekly Menu",
      "CollectOrder/CollectOrderView.fxml", "Collect order",
      "MenuItems/MenuItems.fxml", "Menu Items",
  };
  private final static int TAB_VIEW_WIDTH = 766;
  private final static int TAB_VIEW_HEIGHT = 525;

  public EmployeeGUI(TabPane tabPane, ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    super(tabPane, viewHandler, viewModelFactory);
  }

  /**
   * Loads the tabs to the provided tab view.
   */
  @Override public void loadTabs() {
    Log.log("EmployeeGUI employee tabs are loading");
    setWindowSize(TAB_VIEW_WIDTH, TAB_VIEW_HEIGHT);
    loadTabs(BASE_PATH, tabs);
  }

  /**
   * Gets the main window's width
   * @return the main window's width
   */
  @Override public int getWindowWidth() {
    return TAB_VIEW_WIDTH;
  }
}
