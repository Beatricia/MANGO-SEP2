package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import javafx.scene.control.TabPane;
import shared.Log;

public class EmployeeGUI extends UserStrategy
{
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
