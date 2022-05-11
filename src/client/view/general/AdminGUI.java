package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import javafx.scene.control.TabPane;
import shared.Log;

public class AdminGUI extends UserStrategy
{
  private final static String[] tabs = {
      "src/client/view/Admin/acceptEmployee/AcceptEmployeeView.fxml", "Handle Employees"
  };

  public AdminGUI(TabPane tabPane, ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    super(tabPane, viewHandler, viewModelFactory);
  }

  @Override public void loadTabs() {
    Log.log("AdminGUI admin tabs are loading");
    loadTabs(tabs);
  }
}
