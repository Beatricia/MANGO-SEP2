package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import shared.Log;
import shared.UserType;
import transferobjects.User;


/**
 * Controller for the GeneralView. The main responsibility for this class is to refresh the
 * tab specific tab the user switched to.
 *
 * @author Greg
 * @version 1
 */
public class GeneralViewController implements ViewController
{
  @FXML private Button refreshButton;
  @FXML private TabPane tabPane;
  @FXML private Label nameLabel; // to display name of the user

  private UserStrategy userStrategy; // user strategy depending on the user type

  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    tabPane.getTabs().clear();
    tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> tabChanged(newTab));

    String reloadImage = getClass().getResource("reload.png").toString();
    Image image = new Image(reloadImage);

    ImageView imageView = new ImageView(image);

    imageView.setFitWidth(40);
    imageView.setFitHeight(40);

    refreshButton.setGraphic(imageView);
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

  /**
   * Calls the tab's controller's refresh method
   * @param newTab the controller to refresh
   */
  private void tabChanged(Tab newTab) {
    Log.log("GeneralViewController Changed user tab");
    userStrategy.refreshTab(newTab);
  }

  @Override public void refresh() {

  }

  public void refreshButtonPressed() {
    Log.log("GeneralViewController refreshing tab");
    Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
    userStrategy.refreshTab(currentTab);
  }
}
