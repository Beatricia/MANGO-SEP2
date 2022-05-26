package client.view.employee.AddDish;

import client.core.ViewModelFactory;
import client.view.TabController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shared.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class responsible for connecting MenuEmpl.fxml with the MenuEmplViewModel and therefore providing functionality to the GUI.
 * A controller for the employer's menu where he can add new items.
 * @author Beatricia
 * @version 1
 */

public class MenuEmplViewController extends JFrame implements TabController {
    public TextField nameTextField;
    public TextField priceTextField;
    public ImageView imageView;
    public Label errorLabel;
    public TextArea ingredientsAreaField;

    public MenuEmplViewModel viewModel;

    private File filePath;
    private Image image;

    /**
     * Binding all necessary fields with the MenuEmplViewModel and initializing the view Handler
     * Also setting the place where the image menu should be to a default one
     * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
     */
    @Override
    public void init(ViewModelFactory viewModelFactory) {
        viewModel = viewModelFactory.getMenuEmplViewModel();

        errorLabel.textProperty().bindBidirectional(viewModel.getErrorMessage());
        refresh();
    }


    @Override public void refresh()
    {
        //setting everything to default
        nameTextField.clear();
        priceTextField.clear();
        errorLabel.setText("");
        ingredientsAreaField.clear();
        image = new Image(String.valueOf(getClass().getResource("defaultImage/default.png")));
        imageView.setImage(image);
    }

    /**
     * Adding a new item to the database with its name, ingredients and price
     */
    public void addButton() {

        Log.log("Add dish button has been clicked in the MenuEmpl view");
        try{
            viewModel.addItem(nameTextField.getText(), ingredientsAreaField.getText(),priceTextField.getText(), filePath.toString()); //added the picture file here
            refresh();
            // + get the image away
        } catch (NullPointerException e){
            errorLabel.setText("Please select an image!");
        }
    }

    /**
     * Importing a picture from the PC
     */
    public void selectButton() {
        //create new file chooser
        FileChooser fileChooser = new FileChooser();
        //add extension filter, the user can only select png, jpeg

        fileChooser.setTitle("Select a picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("img","*.PNG","*.JPEG", "*.JPG"));
        this.filePath = fileChooser.showOpenDialog(new Stage());

        //Set to user's directory or go to the default C drive if you cannot access
        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if(!userDirectory.canRead()){
            userDirectory = new File("c/");
            fileChooser.setInitialDirectory(userDirectory);
        }

        //update the image by loading the new image
        try{
            BufferedImage bufferedImage = ImageIO.read(filePath);
            image = SwingFXUtils.toFXImage(bufferedImage,null);
            imageView.setImage(image);
        } catch (IOException | RuntimeException e){
            System.out.println(e.getMessage());
        }

        Log.log("An image has been selected when adding a dish");
    }
}
