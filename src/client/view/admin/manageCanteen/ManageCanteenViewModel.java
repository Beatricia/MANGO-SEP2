package client.view.admin.manageCanteen;

import client.model.AdminModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.time.LocalTime;
import java.util.List;

public class ManageCanteenViewModel {
    private AdminModel adminModel;
    private StringProperty openingHours;
    private ObservableList<User> acceptedEmployees;

    public ManageCanteenViewModel(AdminModel adminModel){
        this.adminModel = adminModel;

        openingHours = new SimpleStringProperty();
        acceptedEmployees = FXCollections.observableArrayList();

        this.adminModel.addListener(AdminModel.OPENING_HOURS_RECEIVED,this::updateOpeningHours);
        this.adminModel.addListener(AdminModel.ACCEPTED_EMPLOYEES_RECEIVED,this::employeesReceived);
    }

    private void employeesReceived(PropertyChangeEvent propertyChangeEvent) {
        List<User> employees = (List<User>)propertyChangeEvent.getNewValue();

        Platform.runLater(() ->{
            acceptedEmployees.clear();
            acceptedEmployees.addAll(employees);
        });
    }

    private void updateOpeningHours(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> {
            if (propertyChangeEvent.getPropertyName().equals(AdminModel.OPENING_HOURS_RECEIVED)) {
                openingHours.set(propertyChangeEvent.getNewValue() + "");
            }
        });
    }

    public StringProperty getOpeningHours() {
        return openingHours;
    }

    public void removeEmployee(String username){
        adminModel.removeEmployee(username);
    }

    public ObservableList<User> getAcceptedEmployee(){
        return acceptedEmployees;
    }

    public void setOpeningHours(LocalTime from,LocalTime to){
        if(from.isBefore(to)) {
            adminModel.setOpeningHours(from, to);
        } else throw new IllegalArgumentException("The opening hours are after closing hours");
    }
}
