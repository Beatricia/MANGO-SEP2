package client.view.employee.MenuItems;

import client.core.ViewModelFactory;
import client.view.TabController;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import transferobjects.MenuItem;

import java.time.LocalDate;

/**
 * The class is responsible for the functionality of the graphical user
 * interface that displays the view in which an Employee can remove menu items from the system
 * The class implements the ViewController interface
 * @author Agata
 * @version 1
 */

public class MenuItemsController implements TabController

{
  public Button removeButton;
  public ListView list;

  private MenuItemsViewModel viewModel;

  /**
   * Override interface's method.
   * Initial data that has to be loaded.
   *
   * @param viewModelFactory class needed to get access to DisplayMenuViewModel
   *                         class.
   */

  @Override public void init(ViewModelFactory viewModelFactory)
  {
    this.viewModel = viewModelFactory.getMenuItemsViewModel();

    list.setItems(viewModel.getMenuItems());
    list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    list.addEventFilter(MouseEvent.MOUSE_PRESSED, this::multipleSelection);
    list.getSelectionModel().getSelectedItems().addListener(this::listenList);

    removeButton.setDisable(true);
  }

  /**
   * Method checks if any menu items are selected. If the conditions is true the add
   * button is enabled.
   */

  private void enableButton()
  {
    if(list.getSelectionModel().getSelectedItems().size()>0)
    {
      removeButton.setDisable(false);
    }
    else {
      removeButton.setDisable(true);
    }


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

  /**
   * Sends an Observable list with the selected menu items to the
   * view model.
   */

  public void onRemove(ActionEvent actionEvent)
  {
    viewModel.removeMenuItem(list.getSelectionModel().getSelectedItems());
    refresh();
  }


  @Override public void refresh()
  {
    viewModel.requestList();
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
