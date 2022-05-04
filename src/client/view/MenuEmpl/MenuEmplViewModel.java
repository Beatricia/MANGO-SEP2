package client.view.MenuEmpl;

import client.model.MenuModel;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuEmplViewModel {
    private StringProperty errorMessage;
    private MenuModel menuModel;

    public MenuEmplViewModel(MenuModel menuModel)
    {
        this.menuModel = menuModel;
        errorMessage = new SimpleStringProperty("error");
    }
    
    public Property<String> getErrorMessage() {
        return errorMessage;
    }

    public void addItem(String text, String text1, String text2)
    {
        String line = "This order was placed for QT3000! OK?";
        String pattern = "(((\\w|\\s)+),?)+";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);

        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
        } else {
            System.out.println("NO MATCH");
        }
    }


}
