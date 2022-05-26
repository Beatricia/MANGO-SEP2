package client.view.admin.manageCanteen;

import client.model.AdminModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.Log;
import transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.time.LocalTime;
import java.util.List;

/**
 * View Model responsible for displaying ready data for the controller
 * @author Beatricia
 */
public class ManageCanteenViewModel {
    private AdminModel adminModel;
    private StringProperty openingHours;
    private ObservableList<User> acceptedEmployees;

    /**
     * Initializing the ManageCanteenViewModel class, creating the StringProperty
     * and the observable list. Also adding the current class as a listener to the Admin Model class
     * @param adminModel Model to get data from.
     */
    public ManageCanteenViewModel(AdminModel adminModel){
        this.adminModel = adminModel;

        openingHours = new SimpleStringProperty();
        acceptedEmployees = FXCollections.observableArrayList();

        this.adminModel.addListener(AdminModel.OPENING_HOURS_RECEIVED,this::updateOpeningHours);
        this.adminModel.addListener(AdminModel.ACCEPTED_EMPLOYEES_RECEIVED,this::employeesReceived);
    }

    /**
     * Responsible for update the list of all the employees from the system
     * @param propertyChangeEvent the new list of employess
     */
    private void employeesReceived(PropertyChangeEvent propertyChangeEvent) {
        Log.log("ManageCanteenViewModel the employee list is updating");
        List<User> employees = (List<User>)propertyChangeEvent.getNewValue();

        Platform.runLater(() ->{
            acceptedEmployees.clear();
            acceptedEmployees.addAll(employees);
        });
    }

    /**
     * Responsible for updating the opening hours
     * @param propertyChangeEvent the new opening hours
     */
    private void updateOpeningHours(PropertyChangeEvent propertyChangeEvent) {
        Log.log("ManageCanteenViewModel the hours are updating");
        Platform.runLater(() -> {
            if (propertyChangeEvent.getPropertyName().equals(AdminModel.OPENING_HOURS_RECEIVED)) {
                openingHours.set(propertyChangeEvent.getNewValue() + "");
            }
        });
    }

    /**
     * Getting the opening hours
     * @return the opening hours
     */
    public StringProperty getOpeningHours() {
        Log.log("ManageCanteenViewModel getting the opening hours");
        return openingHours;
    }

    /**
     * Removing the employee with a given username
     * @param username the employee's username
     */
    public void removeEmployee(String username){
        Log.log("ManageCanteenViewModel sending to the admin model the employee to by removed");
        adminModel.removeEmployee(username);
    }

    /**
     * Requesting the accepted employees from the admin model
     */
    public void requestAcceptedEmployees(){
        Log.log("ManageCanteenViewModel requesting from the admin model the accepted employees");
        adminModel.requestAcceptedEmployees();
    }

    /**
     * Getting the accepted employees
     * @return the observable list with all the employees which are the type of User
     */
    public ObservableList<User> getAcceptedEmployee(){
        Log.log("ManageCanteenViewModel getting the accepted employees");
        return acceptedEmployees;
    }

    /**
     * Setting the opening hours
     * @param from the start time
     * @param to the end time
     */
    public void setOpeningHours(LocalTime from,LocalTime to){
        Log.log("ManageCanteenViewModel giving the admin model the opening hours");
        if(from.isBefore(to)) {
            adminModel.setOpeningHours(from, to);
        } else throw new IllegalArgumentException("The opening hours are after closing hours");
    }
}
