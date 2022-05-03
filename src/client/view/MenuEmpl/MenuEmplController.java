package client.view.MenuEmpl;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class MenuEmplController implements ViewController {
    public TextField nameTextField;
    public TextField ingredientsTextField;
    public TextField priceTextField;
    public ImageView imageView;
    public Label errorLabel;

    public ViewHandler viewHandler;
    public MenuEmplViewModel viewModel;
    private File lastSelected;
    private String pictureFile;

    @Override
    public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
        this.viewHandler = viewHandler;
        viewHandler.openAddDishesView();

        errorLabel.textProperty().bindBidirectional(viewModel.getErrorMessage());
    }

    public void addButton(ActionEvent actionEvent) {
        viewModel.addItem(nameTextField.getText(), ingredientsTextField.getText(),priceTextField.getText());
    }

    public void selectButton(ActionEvent actionEvent) {
        //create new file chooser
        FileChooser fileChooser = new FileChooser();
        //add extension filter, the user can only select png, jpeg
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".PFG",".JPEG"));
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
