package client.view.admin.acceptEmployee;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import shared.Log;
import transferobjects.User;

import java.util.*;

/**
 * A class that connects the GUI with ViewModel
 * In this class the information about events that happen in the GUI is passed onto the ViewModel
 *
 * @author Simon
 */
public class AcceptEmployeeController implements ViewController
{
  @FXML private VBox employeeVBox; //javafx storage to show the pending employees

  private AcceptEmployeeViewModel viewModel;

  private final Map<String, Pane> usersInView = new HashMap<>(); //storing the users and the associated panes
                                                              // so its gonna be easier to remove one user's pane from the view

  /**
   * This method is responsible for main initializations
   *
   * @param viewHandler instance of the class viewHandler responsible for view management
   * @param viewModelFactory instance responsible for creating Models
   */
  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {

    viewModel = viewModelFactory.getAcceptEmployeeViewModel();
    viewModel.getEmployeeList().addListener(this::onListChange);

    onRefreshButtonPressed();
  }

  /**
   * This method is responsible for passing the refresh request to the ViewModel
   */
  @Override public void refresh() {
    viewModel.refresh();
  }

  /**
   * This method is responsible for keeping the list of employees updated and handling accept and decline requests
   *
   * @param change The change in the subject registered by the listener
   */
  private void onListChange(ListChangeListener.Change<? extends User> change) {
    Platform.runLater(() -> {
      change.next();
      if(change.wasRemoved()){
        removeUsers(change.getRemoved());
        Log.log("AcceptEmployeeController the employee was removed from the ViewModel");
      }
      else if(change.wasAdded()){
        addUsers(change.getAddedSubList());
        Log.log("AcceptEmployeeController the employee was added to the ViewModel");
      }
    });
  }

  /**
   * This method is responsible for removing users from the list (VBox)
   *
   * @param users List of User objects
   */
  private void removeUsers(List<? extends User> users){
    Log.log("AcceptEmployeeController removed user: " + users.size());
    for (User user : users){
      try{
        Pane p = usersInView.remove(user.getUsername());
        employeeVBox.getChildren().remove(p);
        System.out.println(user + " user removed with pane " + p);
      } catch (Exception e){
        e.printStackTrace();
      }
    }
  }

  /**
   * This method is responsible for adding users to the list (VBox)
   *
   * @param users List of User objects
   */
  private void addUsers(List<? extends User> users){
    Log.log("AcceptEmployeeController added users " + users.size());
    for (User user : users){
      try{
        Pane p = createUserBox(user);
        System.out.println(user + " user added in pane " + p);
        usersInView.put(user.getUsername(), p);
      } catch (Exception e){
        e.printStackTrace();
      }
    }
  }

  /**
   * This method is responsible for creating a user box (HBox in a VBox)
   *
   * @param user User type object
   */
  private Pane createUserBox(User user){
    Label label = new Label(user.toString());

    Button buttonAccept = new Button("Accept");
    buttonAccept.setOnAction(evt -> viewModel.handleUser(user, true));
    Button buttonDecline = new Button("Decline");
    buttonDecline.setOnAction(evt -> viewModel.handleUser(user, false));

    HBox buttonBox = new HBox();
    buttonBox.setPadding(new Insets(2));
    buttonBox.getChildren().addAll(buttonAccept, buttonDecline);

    VBox centerVBox = new VBox();
    centerVBox.setAlignment(Pos.CENTER_LEFT);
    centerVBox.getChildren().add(label);


    BorderPane borderPane = new BorderPane();
    borderPane.setCenter(centerVBox);
    borderPane.setRight(buttonBox);

    employeeVBox.getChildren().add(borderPane);

    Log.log("AcceptEmployeeController a new userBox is created");
    return borderPane;
  }

  /**
   * This method is responsible for calling the <code>refresh()<code/> method whenever the <b>Refresh<b/> button is pressed
   */
  public void onRefreshButtonPressed() {
    refresh();
  }
}


/*
   Scroll pane
┌───────────────────────────────────────────────────────────────────────┐
│                                                                       │
│    Vbox                                                               │
│   ┌────────────────────────────────────────────────────────────┐      │
│   │    Border pane                                             │      │
│   │ ┌────────────────────────────────────────────────────────┐ │      │
│   │ │        CENTER                     RIGHT                │ │      │
│   │ │                                                        │ │      │
│   │ │                          Hbox                          │ │      │
│   │ │                         ┌────────────────────────────┐ │ │      │
│   │ │  Vbox (align left)      │                            │ │ │      │
│   │ │    ┌────────────┐       │ ┌─────────┐   ┌─────────┐  │ │ │      │
│   │ │    │ Name       │       │ │  Accept │   │ Decline │  │ │ │      │
│   │ │    └────────────┘       │ └─────────┘   └─────────┘  │ │ │      │
│   │ │                         │                            │ │ │      │
│   │ │                         └────────────────────────────┘ │ │      │
│   │ │                                                        │ │      │
│   │ │                                                        │ │      │
│   │ └────────────────────────────────────────────────────────┘ │      │
│   │                                                            │      │
│   │    Border pane                                             │      │
│   │ ┌────────────────────────────────────────────────────────┐ │      │
│   │ │        CENTER                     RIGHT                │ │      │
│   │ │                                                        │ │      │
│   │ │                          Hbox                          │ │      │
│   │ │                         ┌────────────────────────────┐ │ │      │
│   │ │  Vbox (align left)      │                            │ │ │      │
│   │ │    ┌────────────┐       │ ┌─────────┐   ┌─────────┐  │ │ │      │
│   │ │    │ Name       │       │ │  Accept │   │ Decline │  │ │ │      │
│   │ │    └────────────┘       │ └─────────┘   └─────────┘  │ │ │      │
│   │ │                         │                            │ │ │      │
│   │ │                         └────────────────────────────┘ │ │      │
│   │ │                                                        │ │      │
│   │ │                                                        │ │      │
│   │ └────────────────────────────────────────────────────────┘ │      │
│   │                                                            │      │
│   └────────────────────────────────────────────────────────────┘      │
│                                                                       │
└───────────────────────────────────────────────────────────────────────┘
 */

