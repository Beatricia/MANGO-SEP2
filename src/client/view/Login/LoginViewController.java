package client.view.Login;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import shared.Log;
import shared.UserType;

/**
 * The class which is responsible for the functionality of
 * the graphical user interface.
 * Implements ViewController interface.
 * @author Agata
 * @version 1
 */

public class LoginViewController implements ViewController
{
  public TextField username;
  public PasswordField password;
  public Label errorLabel;

  private ViewHandler viewHandler;
  private LoginViewModel viewModel;

  /**
   * Override interface's method.
   * Initial base data.
   * @param viewHandler get instance of the ViewHandler class.
   * @param viewModelFactory class needed to get access to LoginViewModel class.
   */

  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModelFactory.getLoginViewModel();

    errorLabel.textProperty().bindBidirectional(viewModel.getError());
  }

  @Override public void refresh() {

  }

  /**
   * LogIn method to get particular user object.(When logg in button pressed)
   * @param actionEvent event which call login method from LoginViewModel.
   */

  public void onLogIn(ActionEvent actionEvent)
  {
    Log.log("LoginViewController logg in button pressed");
    viewModel.login(username.getText(), password.getText());
  }

  /**
   * Opens a RegisterView
   */

  public void onRegister(ActionEvent actionEvent)
  {
    Log.log("LoginViewController register button pressed");
    errorLabel.setText("");
    viewHandler.openRegisterView();
  }
}
