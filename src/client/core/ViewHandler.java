package client.core;

import client.networking.Client;
import client.view.ViewController;
import client.view.general.GeneralViewController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import shared.Log;

import transferobjects.User;

import java.beans.PropertyChangeEvent;
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

  private Scene loginScene;
  private Scene registerScene;
  private Scene generalScene;

  /**
   * Constructor for the class
   *
   * @param viewModelFactory a viewModelFactory object
   * @param stage            a Stage object
   */
  public ViewHandler(ViewModelFactory viewModelFactory, Stage stage, ClientFactory clientFactory) {
    this.viewModelFactory = viewModelFactory;
    this.stage = stage;

    Client client = clientFactory.getClient();
    client.addListener(Client.ERROR_RECEIVED, this::onErrorReceived);
    client.addListener(Client.LOGGED_IN_RECEIVED, this::onLoggedInReceived);
  }

  /**
   * A method that is responsible for opening the login view, which is the first
   * view shown when the program start
   */
  public void start() {
    openLoginView();
  }

  /**
   * A method that loads the login view, instantiates the LoginViewController,
   * calls the init() method in the controller, sets a title for the stage and
   * displays the view.
   */
  public void openLoginView() {

    if(loginScene == null){
      String path = "../view/Login/LoginView.fxml";
      Pane p = openView(path);
      loginScene = new Scene(p);
    }

    Log.log("ViewHandler: Log-In view opened");
    stage.setScene(loginScene);
    stage.setTitle("Log in");
    stage.show();
  }

  /**
   * A method that loads the register view, instantiates the
   * RegisterViewController, calls the init() method in the controller, sets a
   * title for the stage and displays the view.
   */

  public void openRegisterView() {
    if(registerScene == null){
      String path = "../view/Register/RegisterViewNew.fxml";
      Pane p = openView(path);
      registerScene = new Scene(p);
    }


    Log.log("ViewHandler: Register view opened");
    stage.setTitle("Register");
    stage.setScene(registerScene);
    stage.show();
  }

  private void openGeneralView(User user){
    try{
      if(generalScene == null){
        String path = "../view/general/GeneralView.fxml";

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Pane pane = loader.load();

        GeneralViewController controller = loader.getController();
        controller.init(user, this, viewModelFactory);

        generalScene = new Scene(pane);
      }


      Log.log("ViewHandler: General view opened");
      stage.setScene(generalScene);
      stage.show();
    } catch (IOException e){
      e.printStackTrace();
    }
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

  private void onLoggedInReceived(PropertyChangeEvent evt) {
    User user = (User) evt.getNewValue();
    Platform.runLater(() -> openGeneralView(user));
  }

  private void onErrorReceived(PropertyChangeEvent evt) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error Dialog");
    alert.setHeaderText(null);
    alert.setContentText(evt.getNewValue().toString());

    Platform.runLater(alert::showAndWait);
  }
}
