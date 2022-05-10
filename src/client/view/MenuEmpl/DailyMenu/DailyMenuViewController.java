package client.view.MenuEmpl.DailyMenu;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.model.MenuModel;
import client.view.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.CheckBoxListCell;
import transferobjects.MenuItem;

import java.awt.event.ActionEvent;

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

  private ViewHandler viewHandler;
  private DailyMenuViewModel viewModel;


  /**
   * Override interface's method.
   * Initial data that has to be loaded.
   * @param viewHandler get instance of the ViewHandler class.
   * @param viewModelFactory class needed to get access to DisplayMenuViewModel class.
   */
  @Override public void init(ViewHandler viewHandler,
      ViewModelFactory viewModelFactory)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModelFactory.getDailyMenuViewModel();

    list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

  }

  @Override public void refresh() {
    throw new RuntimeException("not implemented the refresh method");
  }

  /**
   * Sends an Observable list with the selected menu items and date to the
   * view model.
   */

  public void onAdd(javafx.event.ActionEvent actionEvent)
  {
    viewModel.addToDailyMenu(list.getSelectionModel().getSelectedItems(), datePicker.getValue());

  }
}
