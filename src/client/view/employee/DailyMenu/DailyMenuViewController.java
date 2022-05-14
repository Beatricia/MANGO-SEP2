package client.view.employee.DailyMenu;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import shared.Log;
import transferobjects.MenuItem;

import java.time.LocalDate;

/**
 * The class is responsible for the functionality of the graphical user
 * interface that displays the view in which an Employee can add menu items to
 * the daily menu.
 * The class implements the ViewController interface
 * @author Uafa
 * @version 1
 */
public class DailyMenuViewController implements ViewController
{
  public ListView<MenuItem> list;
  public DatePicker datePicker;
  @FXML Button addButton;

  private ViewHandler viewHandler;
  private DailyMenuViewModel viewModel;

  /**
   * Override interface's method.
   * Initial data that has to be loaded.
   *
   * @param viewHandler      get instance of the ViewHandler class.
   * @param viewModelFactory class needed to get access to DisplayMenuViewModel
   *                         class.
   */
  @Override public void init(ViewHandler viewHandler,
      ViewModelFactory viewModelFactory)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModelFactory.getDailyMenuViewModel();

    addButton.setDisable(true);

    list.setItems(viewModel.getMenuItems());
    list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    //THIS IS HERE SO WE CAN SELECT MULTIPLE MENU ITEMS

    list.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
    list.getSelectionModel().getSelectedItems().addListener(this::listenList);
    datePicker.valueProperty().addListener(this::datePickerListener);

  }

  private void datePickerListener(Observable observable) {
    enableButton();
  }

  /**
   * Method checks if a date is selected and if it is a valid value. Also checks
   * if any menu items are selected. If all the conditions are true the add
   * button is enabled.
   */
  private void enableButton()
  {
    boolean checkListSize = list.getSelectionModel().getSelectedItems().size()> 0;
    boolean checkDateSelected = datePicker.getValue() != null && !datePicker.getValue().isBefore(LocalDate.now());


    addButton.setDisable(!checkListSize || !checkDateSelected);

    Log.log("Button to add A DailyMenu is enabled");
  }

  /**
   * Method listens to the Observable list containing Menu Items and calls the
   * private enableButton method
   * @param change any change that might occur in the Observable list
   */
  private void listenList(ListChangeListener.Change<? extends MenuItem> change)
  {
    change.next();
    enableButton();
  }

  @Override public void refresh()
  {
    viewModel.requestList();
  }

  /**
   * Sends an Observable list with the selected menu items and date to the
   * view model.
   */

  public void onAdd()
  {
    Log.log("Add button has been clicked to add a DailyMenu");
    viewModel.addToDailyMenu(list.getSelectionModel().getSelectedItems(), datePicker.getValue());
  }

  /**
   * Handle a user click on the list view. When the user clicks on the list view object,
   * this method checks if the click landed on a list item, if yes, then toggles the
   * selection on that list item.
   * @param evt mouse event of the user click
   */
  private void multipleSelection(MouseEvent evt)
  {
    Node node = evt.getPickResult().getIntersectedNode();

    // go up from the target node until a list cell is found or it's clear
    // it was not a cell that was clicked
    while (node != null && node != list && !(node instanceof ListCell)) {
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

}