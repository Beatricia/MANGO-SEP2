package client.view.customer.displayWeeklyMenu;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

import transferobjects.MenuItemWithQuantity;

import java.util.Arrays;
import java.util.List;

/**
 * Controller for the WeeklyMenuView.fxml
 *
 * @author Greg
 * @version 1
 */
public class WeeklyMenuController implements ViewController
{
  @FXML private ListView<MenuItemWithQuantity> mondayList;
  @FXML private ListView<MenuItemWithQuantity> tuesdayList;
  @FXML private ListView<MenuItemWithQuantity> wednesdayList;
  @FXML private ListView<MenuItemWithQuantity> thursdayList;
  @FXML private ListView<MenuItemWithQuantity> fridayList;

  @FXML private Label mondayDate;
  @FXML private Label tuesdayDate;
  @FXML private Label wednesdayDate;
  @FXML private Label thursdayDate;
  @FXML private Label fridayDate;

  CustomerWeeklyMenuViewModel viewModel;



  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    viewModel = viewModelFactory.getCustomerWeeklyMenuViewModel();

    mondayList.setItems(viewModel.getMondayList());
    tuesdayList.setItems(viewModel.getTuesdayList());
    wednesdayList.setItems(viewModel.getWednesdayList());
    thursdayList.setItems(viewModel.getThursdayList());
    fridayList.setItems(viewModel.getFridayList());

    List<ListView<MenuItemWithQuantity>> menuLists =
        Arrays.asList(mondayList, tuesdayList, wednesdayList, thursdayList, fridayList);

    NoSelectionModel<MenuItemWithQuantity> selectionModel =
        new NoSelectionModel<>();

    for (ListView<MenuItemWithQuantity> menuList : menuLists){
      menuList.setSelectionModel(selectionModel);
    }

    mondayDate.textProperty().bind(viewModel.mondayDateProperty());
    tuesdayDate.textProperty().bind(viewModel.tuesdayDateProperty());
    wednesdayDate.textProperty().bind(viewModel.wednesdayDateProperty());
    thursdayDate.textProperty().bind(viewModel.thursdayDateProperty());
    fridayDate.textProperty().bind(viewModel.fridayDateProperty());
  }

  @Override public void refresh() {
    viewModel.refresh();
  }

  /**
   * SelectionModel without selection
   * @param <T> type of the selectable item
   */
  static class NoSelectionModel<T> extends MultipleSelectionModel<T>{
    @Override public ObservableList<Integer> getSelectedIndices() {return FXCollections.observableArrayList();}
    @Override public ObservableList<T> getSelectedItems() {return FXCollections.observableArrayList();}
    @Override public void selectIndices(int i, int... ints) {}
    @Override public void selectAll() {}
    @Override public void selectFirst() {}
    @Override public void selectLast() {}
    @Override public void clearAndSelect(int i) {}
    @Override public void select(int i) {}
    @Override public void select(Object o) {}
    @Override public void clearSelection(int i) {}
    @Override public void clearSelection() {}
    @Override public boolean isSelected(int i) {return false;}
    @Override public boolean isEmpty() {return true;}
    @Override public void selectPrevious() {}
    @Override public void selectNext() {}
  }
}
