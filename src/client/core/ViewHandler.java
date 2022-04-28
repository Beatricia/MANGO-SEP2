package client.core;

import client.view.Login.LoginViewController;
import client.view.register.RegisterViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A class that handles the opening of the fxml view files
 * @author Uafa
 */
public class ViewHandler
{
  private ViewModelFactory viewModelFactory;
  private Stage stage;

  /**
   * Constructor for the class
   * @param viewModelFactory  a viewModelFactory object
   * @param stage  a Stage object
   */
  public ViewHandler(ViewModelFactory viewModelFactory, Stage stage)
  {
    this.viewModelFactory = viewModelFactory;
    this.stage = stage;
  }

  /**
   * A method that is responsible for opening the login view, which is the first
   * view shown when the program start
   */
  public void start()
  {
    openLoginView();
  }

  /**
   * A method that loads the login view, instantiates the LoginViewController,
   * calls the init() method in the controller, sets a title for the stage and
   * displays the view.
   */

  public void openLoginView()
  {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("../view/Login/LoginView.fxml"));

    Parent root = null;

    try
    {
      root = loader.load();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    LoginViewController controller = loader.getController();
    controller.init(this, viewModelFactory);
    stage.setTitle("Log in");

    Scene scene =new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  /**
   * A method that loads the register view, instantiates the
   * RegisterViewController, calls the init() method in the controller, sets a
   * title for the stage and displays the view.
   */

  public void openRegisterView()
  {
    FXMLLoader loader = new FXMLLoader();

    loader.setLocation(getClass().getResource("../view/register/RegisterViewNew.fxml"));

    Parent root = null;

    try
    {
      root = loader.load();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    RegisterViewController controller = loader.getController();
    controller.init(this, viewModelFactory);
    stage.setTitle("Register");


    Scene scene =new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
