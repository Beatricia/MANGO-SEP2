package client.core;

import client.view.Login.LoginViewController;
import client.view.Register.RegisterViewController;
import client.view.ViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A class that handles the opening of the fxml view files
 *
 * @author Uafa
 */
public class ViewHandler
{
  private ViewModelFactory viewModelFactory;
  private Stage stage;

  public Stage getStage() {
    return stage;
  }

  /**
   * Constructor for the class
   *
   * @param viewModelFactory a viewModelFactory object
   * @param stage            a Stage object
   */
  public ViewHandler(ViewModelFactory viewModelFactory, Stage stage) {
    this.viewModelFactory = viewModelFactory;
    this.stage = stage;
  }

  /**
   * A method that is responsible for opening the login view, which is the first
   * view shown when the program start
   */
  public void start() {
    //openLoginView();

    openAddDishesView();
  }

  /**
   * A method that loads the login view, instantiates the LoginViewController,
   * calls the init() method in the controller, sets a title for the stage and
   * displays the view.
   */
  public void openLoginView() {

    String path = "../view/Login/LoginView.fxml";
    Pane p = openView(path);

    stage.setTitle("Log in");
    Scene scene = new Scene(p);
    stage.setScene(scene);
    stage.show();
  }

  /**
   * A method that loads the register view, instantiates the
   * RegisterViewController, calls the init() method in the controller, sets a
   * title for the stage and displays the view.
   */

  public void openRegisterView() {
    String path = "../view/Register/RegisterViewNew.fxml";
    Pane p = openView(path);

    stage.setTitle("Register");
    Scene scene = new Scene(p);
    stage.setScene(scene);
    stage.show();
  }

  public void openAddDishesView() {
    String path = "../view/MenuEmpl/MenuEmpl.fxml";
    Pane p = openView(path);

    stage.setTitle("Add items to the menu");
    Scene scene = new Scene(p);
    stage.setScene(scene);
    stage.show();
  }

  private Pane openView(String path) {
    FXMLLoader loader = new FXMLLoader();

    loader.setLocation(getClass().getResource(path));

    Pane root = null;

    try {
      root = loader.load();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    ViewController controller = loader.getController();
    controller.init(this, viewModelFactory);

    return root;
  }
}
