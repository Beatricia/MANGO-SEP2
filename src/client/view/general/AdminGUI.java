package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import javafx.scene.control.TabPane;
import shared.Log;

/**
 * AdminGUI is part of the strategy pattern, specifically this is the strategy when the user
 * is an Administrator
 * @author Greg
 * @version 1
 */
public class AdminGUI extends UserStrategy
{
  private final static String BASE_PATH = "src/client/view/admin/";

  /**
   * All the tabs to load when the logged in user is the admin
   */
  private final static String[] tabs = {
      "acceptEmployee/AcceptEmployeeView.fxml", "Handle Employees"
  };

  public AdminGUI(TabPane tabPane, ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    super(tabPane, viewHandler, viewModelFactory);
  }

  @Override public void loadTabs() {
    Log.log("AdminGUI admin tabs are loading");
    loadTabs(BASE_PATH, tabs);
  }
}
