package client.view.MenuEmpl;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class responsible for connecting MenuEmpl.fxml with the MenuEmplViewModel and therefore providing functionality to the GUI.
 * A controller for the employer's menu where he can add new items.
 * @author beatricia
 * @version 1
 */

public class MenuEmplViewController extends JFrame implements ViewController  {
    public TextField nameTextField;
    public TextField priceTextField;
    public ImageView imageView;
    public Label errorLabel;
    public TextArea ingredientsAreaField;

    public ViewHandler viewHandler;
    public MenuEmplViewModel viewModel;

    private FileChooser fileChooser;
    private File filePath;
    private Image image;

    /**
     * Binding all necessary fields with the MenuEmplViewModel and initializing the view Handler
     * Also setting the place where the image menu should be to a default one
     * @param viewHandler instance of ViewHandler class, which is responsible for managing the GUI views
     * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
     */
    @Override
    public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
        this.viewHandler = viewHandler;
        viewModel = viewModelFactory.getMenuEmplViewModel();

        errorLabel.textProperty().bindBidirectional(viewModel.getErrorMessage());

        //setting the default image for the menu item
        image = new Image(String.valueOf(getClass().getResource("defaultImage/default.png")));
        imageView.setImage(image);
    }

    /**
     * Adding a new item to the database with it's name, ingredients and price
     * @param actionEvent from GUI(Button click)
     */
    public void addButton(ActionEvent actionEvent) {
        viewModel.addItem(nameTextField.getText(), ingredientsAreaField.getText(),priceTextField.getText(), filePath.toString()); //added the picture file here
    }

    /**
     * Importing a picture from the PC
     * @param actionEvent from GUI(Button click)
     */
    public void selectButton(ActionEvent actionEvent) throws FileNotFoundException {
        //create new file chooser
        fileChooser = new FileChooser();
        //add extension filter, the user can only select png, jpeg

        fileChooser.setTitle("Select a picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("img","*.PNG","*.JPEG", "*.JPG"));
        this.filePath = fileChooser.showOpenDialog(new Stage());

        //Set to user's directory or go to the default C drive if cannot access
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
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
