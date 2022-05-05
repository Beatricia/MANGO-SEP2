package client.view.MenuEmpl;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Class responsible for connecting MenuEmpl.fxml with the MenuEmplViewModel and therefore providing functionality to the GUI.
 * A controller for the employer's menu where he can add new items.
 * @author beatricia
 * @version 1
 */

public class MenuEmplController implements ViewController {
    public TextField nameTextField;
    public TextField priceTextField;
    public ImageView imageView;
    public Label errorLabel;
    public TextArea ingredientsAreaField;

    public ViewHandler viewHandler;
    public MenuEmplViewModel viewModel;
    private File lastSelected;
    private String pictureFile;

    /**
     * Binding all necessary fields with the MenuEmplViewModel and initializing the view Handler
     * @param viewHandler instance of ViewHandler class, which is responsible for managing the GUI views
     * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
     */
    @Override
    public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
        this.viewHandler = viewHandler;
        viewHandler.openAddDishesView();

        errorLabel.textProperty().bindBidirectional(viewModel.getErrorMessage());
    }

    /**
     * Adding a new item to the database with it's name, ingredients and price
     * @param actionEvent from GUI(Button click)
     */
    public void addButton(ActionEvent actionEvent) {
        viewModel.addItem(nameTextField.getText(), ingredientsAreaField.getText(),priceTextField.getText(), pictureFile); //added the picture file here
    }

    /**
     * Importing a picture from the PC
     * @param actionEvent from GUI(Button click)
     */
    public void selectButton(ActionEvent actionEvent) {
        //create new file chooser
        FileChooser fileChooser = new FileChooser();
        //add extension filter, the user can only select png, jpeg
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".PNG",".JPEG"));
        fileChooser.setTitle("Selecting a picture");

        if(lastSelected!=null){
            //Just starting the chooser in the last selected file s directory
            fileChooser.setInitialDirectory(lastSelected);
        }
        //Run the chooser
        File file = fileChooser.showOpenDialog(new Stage());
        //
        if(file==null){
            return;
        }
        //Saving the directory for future use
        lastSelected = file.getParentFile();
        pictureFile = file.getAbsolutePath();
    }


}
