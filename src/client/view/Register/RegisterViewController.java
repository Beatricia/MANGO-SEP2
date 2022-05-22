package client.view.Register;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import shared.Log;
import shared.UserType;

//TODO javadocs

/**
 * Class responsible for connecting RegisterViewNew.fxml with RegisterViewModel and therefore providing the functionality to the GUI.
 * @author Simon
 * @version 1
 */

public class RegisterViewController implements ViewController
{

  @FXML private TextField firstName;
  @FXML private RadioButton radioCustomer;
  @FXML private RadioButton radioEmployee;
  @FXML private TextField lastName;
  @FXML private TextField username;
  @FXML private PasswordField password;
  @FXML private PasswordField passwordRepeat;
  @FXML private Label errorMessage;
  private ViewHandler viewHandler;
  private RegisterViewModel viewModel;

  /**
   * Binding all necessary fields with RegisterViewModel and initializing fo the @parameters.
   * @param viewHandler responsible for managing the GUI views.
   * @param viewModelFactory creating the RegisterViewModel with which the RegisterViewController is connected
   */
  @Override public void init(ViewHandler viewHandler,
      ViewModelFactory viewModelFactory)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModelFactory.getRegisterViewModel();

    errorMessage.textProperty().bindBidirectional(viewModel.getErrorMessage()); // binds with the errorMessage in VM

    refresh();
  }

  @Override public void refresh() {
    firstName.clear();
    lastName.clear();
    username.clear();
    password.clear();
    passwordRepeat.clear();
    errorMessage.setText("");
  }

  /**
   * Changing GUI from RegisterViewNew to LoginView
   * @param actionEvent from GUI (Button click)
   */
  @FXML private void onBack(ActionEvent actionEvent)
  {
    Log.log("Go Back button in the Register view is pressed");
    errorMessage.setText("");
    viewHandler.openLoginView();
    refresh();

  }

  /**
   * Checks for UserType selected in GUI and sends a request to create new User
   * @param actionEvent from GUI (Button click)
   */
  @FXML private void onRegister(ActionEvent actionEvent)
  {

    Log.log("Register button is clicked in the Register view");
    UserType userType = null;
      if (radioCustomer.isSelected()) // checks for the selected radio button
      {
        userType = UserType.CUSTOMER;
      }
      else if (radioEmployee.isSelected())
      {
        userType = UserType.EMPLOYEE;
      }

      viewModel.register(firstName.getText(), lastName.getText(),
          username.getText(), password.getText(), passwordRepeat.getText(), userType);

      refresh();
    }

}
