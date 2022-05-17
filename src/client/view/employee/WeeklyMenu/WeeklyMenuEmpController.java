package client.view.employee.WeeklyMenu;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import shared.Log;
import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

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

  private ArrayList<ListView<MenuItemWithQuantity>> list;


  @Override public void init(ViewHandler viewHandler,
      ViewModelFactory viewModelFactory)
  {
    viewModel = viewModelFactory.getWeeklyMenuEmpViewModel();

    mondayList.setItems(viewModel.getMondayList());
    tuesdayList.setItems(viewModel.getTuesdayList());
    wednesdayList.setItems(viewModel.getWednesdayList());
    thursdayList.setItems(viewModel.getThursdayList());
    fridayList.setItems(viewModel.getFridayList());


    list.add(mondayList);
    list.add(tuesdayList);
    list.add(wednesdayList);
    list.add(thursdayList);
    list.add(fridayList);

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
    ArrayList<MenuItemWithQuantity> listToDelete = new ArrayList<>();


    for (int i = 0; i < list.size(); i++)
    {
      ObservableList<MenuItemWithQuantity> items = list.get(i).getSelectionModel().getSelectedItems();
      listToDelete.addAll(items);
    }

    viewModel.deleteItems(listToDelete);

    Log.log("Delete button has been clicked to delete items from weeklyMenu");
  }

  private void multipleSelection(MouseEvent evt)
  {
    Node node = evt.getPickResult().getIntersectedNode();

    // go up from the target node until a list cell is found or it's clear
    // it was not a cell that was clicked
    while (node != null && node != mondayList && node != tuesdayList && node != wednesdayList && node != thursdayList && node != fridayList && !(node instanceof ListCell)) {
      node = node.getParent();
    }

    // if is part of a cell or the cell,
    // handle event instead of using standard handling
    if (node instanceof ListCell) {
      // prevent further handling
      evt.consume();

      ListCell cell = (ListCell) node;
      ListView lv = cell.getListView();

      // focus the listview
      lv.requestFocus();

      if (!cell.isEmpty()) {
        // handle selection for non-empty cells
        int index = cell.getIndex();
        if (cell.isSelected()) {
          lv.getSelectionModel().clearSelection(index);
        } else {
          lv.getSelectionModel().select(index);
        }
      }
    }
  }

  private void enableSelectItems()
  {
    ArrayList<ListView<MenuItemWithQuantity>> lists =  new ArrayList<>();
    lists.add(mondayList);
    lists.add(tuesdayList);
    lists.add(wednesdayList);
    lists.add(thursdayList);
    lists.add(fridayList);

    LocalDate date = LocalDate.now();
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    int day = dayOfWeek.getValue();

    for (int i = 0; i < lists.size(); i++)
    {
      ListView list = lists.get(i);
      if(i < day && day >= 6)
      {
        list.setDisable(true);
      }
      else
      {
        list.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
        list.getSelectionModel().getSelectedItems().addListener(this::listenList);
      }
    }


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
