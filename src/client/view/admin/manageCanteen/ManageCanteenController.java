package client.view.admin.manageCanteen;

import client.core.ViewModelFactory;
import client.view.TabController;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import transferobjects.User;

import java.time.LocalTime;

/**
 * Controller for ManageCanteenView.fxml
 *
 * @author Beatricia
 */

public class ManageCanteenController implements TabController {
    public TableView<User> employeesTableView;
    public TableColumn<User, String> employeeNameColumn;
    public TableColumn<User, String> employeeUsernameColumn;
    public Spinner<Integer> fromHourSpinner;
    public Spinner<Integer> fromMinSpinner;
    public Spinner<Integer> toHourSpinner;
    public Spinner<Integer> toMinSpinner;
    public Label currentOpeningHoursLabel;

    private ManageCanteenViewModel viewModel;

    /**
     * Method that initializes the controller
     * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
     */
    @Override
    public void init(ViewModelFactory viewModelFactory)
    {
        viewModel = viewModelFactory.getManageCanteenViewModel();

        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        employeeUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        employeesTableView.setItems(viewModel.getAcceptedEmployee());

        currentOpeningHoursLabel.textProperty().bind(viewModel.getOpeningHours());

        fromHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,24));
        fromMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        toHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,24));
        toMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
    }

    /**
     * Refreshing the accepted employee list
     */
    @Override
    public void refresh() {
        viewModel.requestAcceptedEmployees();
    }


    /**
     * If the remove button is clicked and the selected employee is not null,
     * the employee is deleted
     */
    public void onRemoveEmplButton() {
        User selectedUser = employeesTableView.getSelectionModel().getSelectedItem();
        if(selectedUser!=null){
            viewModel.removeEmployee(selectedUser.getUsername());
        }
    }

    /**
     * If the set button is clicked, the hours are set
     */
    public void onSetHoursButton() {
        LocalTime from = LocalTime.of(fromHourSpinner.getValue(), fromMinSpinner.getValue());
        LocalTime to = LocalTime.of(toHourSpinner.getValue(), toMinSpinner.getValue());
        try {
            viewModel.setOpeningHours(from, to);
        } catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }
}
