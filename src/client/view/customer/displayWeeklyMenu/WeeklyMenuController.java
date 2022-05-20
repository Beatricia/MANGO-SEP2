package client.view.customer.displayWeeklyMenu;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;

import client.view.ViewHelpers.NoSelectionModel;
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
}
