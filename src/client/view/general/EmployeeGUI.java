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
  /**
   * All the tabs to load when the logged in user is an employee
   */
  private final static String[] tabs = {
      "src/client/view/MenuEmpl/AddDish/MenuEmpl.fxml",         "Add items to the menu",
      "src/client/view/MenuEmpl/DailyMenu/DailyMenuView.fxml",  "Add to daily menu"
  };

  public EmployeeGUI(TabPane tabPane, ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    super(tabPane, viewHandler, viewModelFactory);
  }

  @Override public void loadTabs() {
    Log.log("EmployeeGUI employee tabs are loading");
    loadTabs(tabs);
  }
}
