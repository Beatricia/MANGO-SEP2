package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import javafx.scene.control.TabPane;
import shared.Log;

public class CustomerGUI extends UserStrategy
{
  private final static String[] tabs = {

  };

  public CustomerGUI(TabPane tabPane, ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    super(tabPane, viewHandler, viewModelFactory);
  }

  @Override public void loadTabs() {
    Log.log("CustomerGUI customer tabs are loading");
    loadTabs(tabs);
  }
}
