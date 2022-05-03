package client.view.MenuEmpl;

import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;

public class MenuEmplViewModel {
    public StringProperty errorMessage;
    
    public Property<String> getErrorMessage() {
        return errorMessage;
    }

    public void addItem(String text, String text1, String text2) {
    }
}
