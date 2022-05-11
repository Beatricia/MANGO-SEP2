package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import shared.Log;
import shared.UserType;
import transferobjects.User;

public class GeneralViewController implements ViewController
{
  @FXML private TabPane tabPane;
  @FXML private Label nameLabel; // to display name of the user

  private UserStrategy userStrategy; // user strategy depending on the user type

  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    tabPane.getTabs().clear();
    tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> tabChanged(newTab));
  }

  /**
   * Initializes the GeneralViewController, sets the user strategy depending on the user type.
   * @param user logged in user
   * @param viewHandler view handler
   * @param viewModelFactory view model factory
   */
  public void init(User user, ViewHandler viewHandler, ViewModelFactory viewModelFactory){
    if(userStrategy != null)
      return;

    Log.log("GeneralViewController initializing Controller");
    init(viewHandler, viewModelFactory);


    String name = user.getFirstName() + " " + user.getLastName();
    nameLabel.setText(name);

    UserType userType = user.getUserType();

    if(userType == UserType.ADMINISTRATOR) {
      userStrategy = new AdminGUI(tabPane, viewHandler, viewModelFactory);
    }
    else if(userType == UserType.EMPLOYEE) {
      userStrategy = new EmployeeGUI(tabPane, viewHandler, viewModelFactory);
    }
    else if(userType == UserType.CUSTOMER) {
      userStrategy = new CustomerGUI(tabPane, viewHandler, viewModelFactory);
    }
    else {
      throw new RuntimeException("Unexpected User type");
    }

    Log.log("GeneralViewController user strategy created: " + userStrategy);

    userStrategy.loadTabs();
  }

  private void tabChanged(Tab newTab) {
    Log.log("Changed user tab");
    userStrategy.refreshTab(newTab);
  }

  @Override public void refresh() {

  }
}
