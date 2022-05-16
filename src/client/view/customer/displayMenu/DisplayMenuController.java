package client.view.customer.displayMenu;

import client.core.ClientFactory;
import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.model.MenuModelImp;
import client.view.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import shared.Log;
import transferobjects.MenuItem;
import transferobjects.MenuItemWithQuantity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for DisplayMenuView
 *
 * @author Greg
 * @version 1
 */
public class DisplayMenuController implements ViewController
{
  @FXML private VBox menuItemsVBox;
  @FXML private Label dateLabel;

  private DisplayMenuViewModel viewModel;

  /**
   * Initializes the controller
   * @param viewHandler instance of ViewHandler class, which is responsible for managing the GUI views
   * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
   */
  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    viewModel = viewModelFactory.getDisplayMenuViewModel();
    //viewModel = new TestViewModel();
    viewModel.menuItemWithQuantitiesList().addListener(this::menuItemListChangeListener);

    LocalDate localDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    String dateText = localDate.getDayOfWeek() + " - " + localDate.format(formatter);
    dateLabel.setText(dateText);
  }

  /**
   * Listener for the ObservableList in the ViewModel
   * @param change data of the change event
   */
  private void menuItemListChangeListener(ListChangeListener.Change<? extends MenuItemWithQuantity> change) {
    change.next();

    if(change.wasAdded()){
      addedMenuItemWithQuantity(change.getAddedSubList());
    }
    else if(change.wasRemoved()){
      menuItemsVBox.getChildren().clear();
    }
  }

  /**
   * This method is called when any MenuItemWithQuantity was added to the ObservableList.
   * Creates the GUI boxes for all the added items, and puts them in the right place of the GUI.
   * @param list The list of the added items
   */
  private void addedMenuItemWithQuantity(List<? extends MenuItemWithQuantity> list){
    for (MenuItemWithQuantity menuItem : list){
      try{
        HBox hBox = createDailyMenuItemBox(menuItem);
        putIntoHBox(hBox);

      } catch (IOException e){
        Log.log("Image could not be loaded for menu item " + menuItem.getName());
      }
    }
  }

  /**
   * Puts the menu item pane into the main menu pane.
   * The main menu pane is a vbox, every two menuItemPane is put together into one
   * HBox, so that there is maximum two menuItems in one row.
   * <p>
   *   This method is responsible for putting two menuItemPanes into one HBox, and if the last
   *   HBox is full (there are 2 menuItemPanes), it creates a new HBox and puts the menuItemPane
   *   into the empty box.
   * </p>
   * @param menuItemPane the menu item's pane to put into the HBoxes
   */
  private void putIntoHBox(Pane menuItemPane){
    // count of the HBoxes in the main vbox
    int vboxChildrenSize = menuItemsVBox.getChildren().size();

    if(vboxChildrenSize == 0)
      menuItemsVBox.getChildren().add(new HBox());

    // get last HBox from the main vbox
    HBox lastHBox = (HBox) menuItemsVBox.getChildren().get(vboxChildrenSize - 1);

    // check if there are already 2 items in the last HBox
    // if yes, create a new hbox and add it to the main pane
    if(lastHBox.getChildren().size() >= 2){
      lastHBox = new HBox();
      lastHBox.setPadding(new Insets(50, 0, 0, 0));

      menuItemsVBox.getChildren().add(lastHBox);
    }

    lastHBox.getChildren().add(menuItemPane);
  }

  @Override public void refresh() {
    viewModel.requestDailyMenuItems();
  }


  /*
  createDailyMenuItemBox returns this structure for one MenuItemWithQuantity


  -Wrapper HBox- (11)
┌────────────────────────────────────────────────────────────────┐
│   -Left VBox- (10)                   -Right VBox- (5)          │
│ ┌────────────────────────────────┐  ┌────────────────────────┐ │
│ │    -ImageView- (9)             │  │ Name Label (1)         │ │
│ │   ┌───────────────────────┐    │  │                        │ │
│ │   │                       │    │  │ Ingredients Label (2)  │ │
│ │   │                       │    │  │                        │ │
│ │   │                       │    │  │                        │ │
│ │   │                       │    │  │  -Price VBox- (4)      │ │
│ │   └───────────────────────┘    │  │ ┌────────────────────┐ │ │
│ │  -Quantity Box (HBox)- (8)     │  │ │     (3) Price Label│ │ │
│ │ ┌────────────────────────────┐ │  │ └────────────────────┘ │ │
│ │ │QuantityLabel: numberLabel  │ │  │                        │ │
│ │ └───(6)─────────────(7)──────┘ │  │                        │ │
│ │                                │  │                        │ │
│ └────────────────────────────────┘  └────────────────────────┘ │
└────────────────────────────────────────────────────────────────┘
   */

  /**
   * Creates and returns the HBox which includes the information and the image for the
   * MenuItemWithQuantity given in the parameter
   * @param menuItemWithQuantity MenuItemWithQuantity object to create the HBox of.
   * @return HBox which includes the information and the image for the MenuItemWithQuantity given in the parameter
   * @throws IOException when there was an error loading the image
   */
  private static HBox createDailyMenuItemBox(MenuItemWithQuantity menuItemWithQuantity) throws
      IOException {

    String itemName = menuItemWithQuantity.getName();
    String ingredients = String.join(", ", menuItemWithQuantity.getIngredients());
    String price = menuItemWithQuantity.getPrice() + " DKK";
    String quantity = menuItemWithQuantity.getQuantity() + "";
    String imagePath = menuItemWithQuantity.getImgPath();
    int imgSize = 90;



    Label nameLabel = new Label(itemName){{ // (1)
      setFont(Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, 24));
      setWrapText(true);
    }};
    Label ingredientsLabel = new Label(ingredients){{ // (2)
      setWrapText(true);
    }};
    Label priceLabel = new Label(price); // (3)

    VBox priceVbox = new VBox(){{ // (4)
      setAlignment(Pos.TOP_RIGHT);
      setPadding(new Insets(20, 0, 0, 0));
      getChildren().add(priceLabel);
    }};

    VBox rightVbox = new VBox(){{ // (5)
      setPadding(new Insets(10, 30, 10, 20));
      getChildren().addAll(
          nameLabel,
          ingredientsLabel,
          priceVbox
      );
    }};



    Label quantityTextLabel = new Label("Quantity: "); // (6)
    Label quantityCountLabel = new Label(quantity); // (7)

    HBox quantityBox = new HBox(){{ // (8)
      setAlignment(Pos.CENTER);
      getChildren().addAll(
          quantityTextLabel,
          quantityCountLabel
      );
    }};

    //read image from file
    java.awt.image.BufferedImage menuItemImage = ImageIO.read(new File(imagePath));
    //resize image
    java.awt.Image resizedImage = menuItemImage.getScaledInstance(imgSize, imgSize, BufferedImage.SCALE_SMOOTH);

    //convert image to buffered image
    menuItemImage = toBufferedImage(resizedImage);
    javafx.scene.image.Image javafxImage = SwingFXUtils.toFXImage(menuItemImage, null);

    ImageView menuItemImageView = new ImageView(){{ // (9)
      setImage(javafxImage);
      setPickOnBounds(true);
      setPreserveRatio(true);
    }};

    VBox leftVbox = new VBox(){{ // (10)
      getChildren().addAll(
          menuItemImageView,
          quantityBox
      );
    }};

    menuItemImageView.setFitWidth(leftVbox.getWidth() - 10);
    menuItemImageView.setFitHeight(leftVbox.getHeight() - 10);


    HBox wrapper = new HBox(){{ // (11)
      getChildren().addAll(
          leftVbox,
          rightVbox
      );
    }};

    return wrapper;
  }

  /**
   * Converts a given Image into a BufferedImage
   *
   * @param img The Image to be converted
   * @return The converted BufferedImage
   */
  private static BufferedImage toBufferedImage(java.awt.Image img)
  {
    if (img instanceof BufferedImage)
    {
      return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
  }






  // Test for viewmodel
  class TestViewModel extends DisplayMenuViewModel{

    public TestViewModel() {
      super(new MenuModelImp(new ClientFactory().getClient()));
    }

    private final ObservableList<MenuItemWithQuantity> menuItemsTest = FXCollections.observableArrayList();

    @Override public ObservableList<MenuItemWithQuantity> menuItemWithQuantitiesList() {
      return menuItemsTest;
    }

    @Override public void requestDailyMenuItems() {
      menuItemsTest.clear();

      for (int i = 0; i < 17; i++) {
        MenuItem menuItem = new MenuItem("abc", new ArrayList<>(Arrays.asList("cucumber", "banana", "hamburger")), 3.4, "Resources/MenuItemImages/abc.png");
        MenuItemWithQuantity menuItemWithQuantity = new MenuItemWithQuantity(menuItem, LocalDate.now(), 3);
        menuItemsTest.add(menuItemWithQuantity);
      }
    }
  }

}


