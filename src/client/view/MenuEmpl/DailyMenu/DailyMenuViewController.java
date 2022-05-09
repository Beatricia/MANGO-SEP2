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

public class DailyMenuViewController implements ViewController
{
  private ListView<String> list;
  private DatePicker datePicker;



  private ViewHandler viewHandler;
  private DailyMenuViewModel viewModel;

  @Override public void init(ViewHandler viewHandler,
      ViewModelFactory viewModelFactory)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModelFactory.getDailyMenuViewModel();

    list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

  }

  public void onAdd()
  {
      viewModel.addToDailyMenu(list.getSelectionModel().getSelectedItems(), datePicker.getValue());
  }
}
