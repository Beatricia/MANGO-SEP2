package client.view.Login;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    username.textProperty().bindBidirectional(viewModel.getUsername());
    password.textProperty().bindBidirectional(viewModel.getPassword());
    errorLabel.textProperty().bind(viewModel.getError());
  }

  /**
   * LogIn method to get particular user object.
   * @param actionEvent event which call login method from LoginViewModel.
   */

  public void onLogIn(ActionEvent actionEvent)
  {
    if (username.getText().equals("") || password.getText().equals(""))
    {
      errorLabel.setText("Empty field");
    }
     viewModel.login(username.getText(), password.getText());
  }

  /**
   * Opens a RegisterView
   */

  public void onRegister(ActionEvent actionEvent)
  {
    viewHandler.openRegisterView();
  }
}
