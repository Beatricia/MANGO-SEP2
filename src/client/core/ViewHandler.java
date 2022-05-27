package client.core;

import client.networking.Client;
import client.view.ViewController;
import client.view.general.GeneralViewController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
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

    stage.getIcons().add(new Image("file:src/client/view/logo/logo.png"));
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

  /**
   * Loads and opens the general tab view for the user, and initializing its controller.
   * @param user Logged in user's object
   */
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

  /**
   * Get the main stage
   * @return the main stage
   */
  public Stage getStage() {
    return stage;
  }

  /**
   * Loads the fxml view from file, initializes its ViewController, and returns the fxml Pane
   * @param path path to the fxml file (relative path starting from ViewHandler)
   * @return the loaded pane
   */
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

  /**
   * On logged in event received from the server
   * @param evt event value
   */
  private void onLoggedInReceived(PropertyChangeEvent evt) {
    User user = (User) evt.getNewValue();
    Platform.runLater(() -> openGeneralView(user));
  }

  /**
   * Error event handle. It shows an alert to the user with the error message.
   * @param evt event value
   */
  private void onErrorReceived(PropertyChangeEvent evt) {
    Platform.runLater(
        () -> {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error Dialog");
          alert.setHeaderText(null);
          alert.setContentText(evt.getNewValue().toString());

          alert.showAndWait();
        }
    );

    //Platform.runLater(alert::showAndWait);
  }
}
