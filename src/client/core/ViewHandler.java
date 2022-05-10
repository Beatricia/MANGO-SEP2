package client.core;

import client.networking.Client;
import client.networking.SocketClient;
import client.view.Login.LoginViewController;
import client.view.Register.RegisterViewController;
import client.view.ViewController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import shared.UserType;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

/**
 * A class that handles the opening of the fxml view files
 *
 * @author Uafa
 */
public class ViewHandler implements PropertyChangeListener
{
  private ViewModelFactory viewModelFactory;
  private Stage stage;
  private ClientFactory clientFactory;

  public Stage getStage() {
    return stage;
  }

  /**
   * Constructor for the class
   *
   * @param viewModelFactory a viewModelFactory object
   * @param stage            a Stage object
   */
  public ViewHandler(ViewModelFactory viewModelFactory, Stage stage, ClientFactory clientFactory) throws IOException {
    this.viewModelFactory = viewModelFactory;
    this.stage = stage;

    this.clientFactory=clientFactory;

    clientFactory.getClient().addListener(this);
  }

  /**
   * A method that is responsible for opening the login view, which is the first
   * view shown when the program start
   */
  public void start() {
   openAdminAcceptUserView();
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
    String path = "../view/MenuEmpl/AddDish/MenuEmpl.fxml";
    Pane p = openView(path);

    //We used platform.runLater because right now it will run on a javafx thread
    Platform.runLater(()->{
      stage.setTitle("Add items to the menu");
      Scene scene = new Scene(p);
      stage.setScene(scene);
      stage.show();
    });
  }

  public void openAdminAcceptUserView() {
    String path = "../view/Admin/acceptEmployee/AcceptEmployeeView.fxml";
    Pane p = openView(path);

    //We used platform.runLater because right now it will run on a javafx thread
    Platform.runLater(()->{
      stage.setTitle("Handle Employees");
      Scene scene = new Scene(p);
      stage.setScene(scene);
      stage.show();
    });
  }

  public void openDailyMenuView()

  {
    String path = "../view/MenuEmpl/DailyMenu/DailyMenuView.fxml";
    Pane p = openView(path);

    Platform.runLater(()->{
      stage.setTitle("Add to daily menu");
      Scene scene = new Scene(p);
      stage.setScene(scene);
      stage.show();
    });

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

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if(evt.getPropertyName().equals(Client.LOGGED_IN_RECEIVED) ){
      User user = (User)evt.getNewValue();
      if(user.getUserType() == (UserType.EMPLOYEE)){
        //openAddDishesView();

        openDailyMenuView();
      }
    }
  }
}
