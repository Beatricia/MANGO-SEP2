package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import shared.UserType;
import transferobjects.User;

public class GeneralViewController implements ViewController
{
  @FXML private TabPane tabPane;
  @FXML private Label nameLabel;

  private UserStrategy userStrategy;

  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    tabPane.getTabs().clear();
    tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> tabChanged(newTab));
  }

  public void init(User user, ViewHandler viewHandler, ViewModelFactory viewModelFactory){
    if(userStrategy != null)
      return;


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

    userStrategy.loadTabs();
  }

  private void tabChanged(Tab newTab) {
    userStrategy.refreshTab(newTab);
  }

  @Override public void refresh() {

  }
}
