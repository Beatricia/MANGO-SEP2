package client.view.admin.manageCanteen;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.TabController;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import transferobjects.User;

import javax.swing.*;
import java.time.LocalTime;

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

    @Override
    public void init(ViewModelFactory viewModelFactory)
    {
        viewModel = viewModelFactory.getManageCanteenController();

        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        employeesTableView.setItems(viewModel.getAcceptedEmployee());

        currentOpeningHoursLabel.textProperty().bind(viewModel.getOpeningHours());

        fromHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,24));
        fromMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
        toHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,24));
        toMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,59));
    }

    @Override
    public void refresh() {

    }


    public void onRemoveEmplButton(ActionEvent actionEvent) {
        User selectedUser = employeesTableView.getSelectionModel().getSelectedItem();
        if(selectedUser!=null){
            viewModel.removeEmployee(selectedUser.getUsername());
        }
    }

    public void onSetHoursButton(ActionEvent actionEvent) {
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
