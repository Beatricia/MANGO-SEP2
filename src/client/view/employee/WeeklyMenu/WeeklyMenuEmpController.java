package client.view.employee.WeeklyMenu;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import shared.Log;
import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeeklyMenuEmpController implements ViewController
{
  public ListView<MenuItemWithQuantity> mondayList;
  public Label mondayDate;
  public ListView<MenuItemWithQuantity> tuesdayList;
  public Label tuesdayDate;
  public ListView<MenuItemWithQuantity> wednesdayList;
  public Label wednesdayDate;
  public ListView<MenuItemWithQuantity> thursdayList;
  public Label thursdayDate;
  public ListView<MenuItemWithQuantity> fridayList;
  public Label fridayDate;
  public Button deleteButton;

  private WeeklyMenuEmpViewModel viewModel;


  @Override public void init(ViewHandler viewHandler,
      ViewModelFactory viewModelFactory)
  {
    viewModel = viewModelFactory.getWeeklyMenuEmpViewModel();

    mondayList.setItems(viewModel.getMondayList());
    tuesdayList.setItems(viewModel.getTuesdayList());
    wednesdayList.setItems(viewModel.getWednesdayList());
    thursdayList.setItems(viewModel.getThursdayList());
    fridayList.setItems(viewModel.getFridayList());

    enableSelectItems();

    mondayDate.textProperty().bind(viewModel.mondayDateProperty());
    tuesdayDate.textProperty().bind(viewModel.tuesdayDateProperty());
    wednesdayDate.textProperty().bind(viewModel.wednesdayDateProperty());
    thursdayDate.textProperty().bind(viewModel.thursdayDateProperty());
    fridayDate.textProperty().bind(viewModel.fridayDateProperty());

    deleteButton.setDisable(true);
  }

  private void enableButton()
  {
    boolean checkMondayList = mondayList.getSelectionModel().getSelectedItems().size() > 0;
    boolean checkTuesdayList = tuesdayList.getSelectionModel().getSelectedItems().size() > 0;
    boolean checkWednesdayList = wednesdayList.getSelectionModel().getSelectedItems().size() > 0;
    boolean checkThursdayList = thursdayList.getSelectionModel().getSelectedItems().size() > 0;
    boolean checkFridayList = fridayList.getSelectionModel().getSelectedItems().size() > 0;

    deleteButton.setDisable(!checkMondayList || !checkTuesdayList || !checkWednesdayList ||
                            !checkThursdayList || !checkFridayList);

    Log.log("Button to delete items from daily menu is enabled");
  }

  private void listenList(ListChangeListener.Change<? extends MenuItem> change)
  {
    change.next();
    enableButton();
  }

  @Override public void refresh()
  {
    viewModel.refresh();
  }

  public void onDelete(ActionEvent actionEvent)
  {
    Log.log("Delete button has been clicked to delete items from weeklyMenu");
  }

  private void multipleSelection(MouseEvent evt)
  {
    //idk
  }

  private void enableSelectItems()
  {
    LocalDate date = LocalDate.now();

    if (date.equals(viewModel.mondayDateProperty()))
    {
      tuesdayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      tuesdayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      tuesdayList.getSelectionModel().getSelectedItems().addListener(this::listenList);

      wednesdayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      wednesdayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      wednesdayList.getSelectionModel().getSelectedItems().addListener(this::listenList);

      thursdayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      thursdayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      thursdayList.getSelectionModel().getSelectedItems().addListener(this::listenList);

      fridayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      fridayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      fridayList.getSelectionModel().getSelectedItems().addListener(this::listenList);
    }
    else if (date.equals(viewModel.tuesdayDateProperty()))
    {
      wednesdayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      wednesdayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      wednesdayList.getSelectionModel().getSelectedItems().addListener(this::listenList);

      thursdayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      thursdayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      thursdayList.getSelectionModel().getSelectedItems().addListener(this::listenList);

      fridayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      fridayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      fridayList.getSelectionModel().getSelectedItems().addListener(this::listenList);
    }
    else if(date.equals(viewModel.wednesdayDateProperty()))
    {
      thursdayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      thursdayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      thursdayList.getSelectionModel().getSelectedItems().addListener(this::listenList);

      fridayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      fridayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      fridayList.getSelectionModel().getSelectedItems().addListener(this::listenList);
    }
    else if(date.equals(viewModel.thursdayDateProperty()))
    {
      fridayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      fridayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      fridayList.getSelectionModel().getSelectedItems().addListener(this::listenList);
    }
    else if(date.equals(viewModel.fridayDateProperty()))
    {
    }
    else
    {
      mondayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      mondayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      mondayList.getSelectionModel().getSelectedItems().addListener(this::listenList);

      tuesdayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      tuesdayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      tuesdayList.getSelectionModel().getSelectedItems().addListener(this::listenList);

      wednesdayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      wednesdayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      wednesdayList.getSelectionModel().getSelectedItems().addListener(this::listenList);

      thursdayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      thursdayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      thursdayList.getSelectionModel().getSelectedItems().addListener(this::listenList);

      fridayList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      fridayList.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
      fridayList.getSelectionModel().getSelectedItems().addListener(this::listenList);
    }
  }
}
