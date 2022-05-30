package client.view.admin.displayMenu;

import client.core.ViewModelFactory;
import client.imageHandler.ClientImageLoader;
import client.view.TabController;
import client.view.customer.displayMenu.DisplayMenuViewModel;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import transferobjects.MenuItemWithQuantity;
import util.ImageTools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for DisplayMenuView
 *
 * @author Greg
 * @version 1
 */
public class DisplayMenuController implements TabController
{
  @FXML private VBox menuItemsVBox;
  @FXML private Label dateLabel;

  private DisplayMenuViewModel viewModel;

  /**
   * Initializes the controller
   * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
   */
  @Override public void init(ViewModelFactory viewModelFactory) {
    viewModel = viewModelFactory.getDisplayMenuViewModel();
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

    Platform.runLater(() -> {
      if(change.wasAdded()){
        addedMenuItemWithQuantity(change.getAddedSubList());
      }
      else if(change.wasRemoved()){
        menuItemsVBox.getChildren().clear();
      }
    });
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
        System.err.println("Image could not be loaded for menu item " + menuItem.getName());
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

    if (vboxChildrenSize == 0)
    {
      menuItemsVBox.getChildren().add(new HBox());
      vboxChildrenSize++;
    }

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

  /**
   * Refreshing the daily menu items
   */
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
    int imgSize = 150;



    Label nameLabel = new Label(itemName){{ // (1)
      setHeight(80);
      setFont(Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, 18));
      setWrapText(true);
    }};
    Label ingredientsLabel = new Label(ingredients){{ // (2)
      setWidth(170);
      setHeight(70);
      setWrapText(true);
    }};
    Label priceLabel = new Label(price); // (3)

    VBox priceVbox = new VBox(){{ // (4)
      setAlignment(Pos.TOP_RIGHT);
      setPadding(new Insets(30, 0, 0, 0));
      getChildren().add(priceLabel);
    }};

    VBox rightVbox = new VBox(){{ // (9)
      setPadding(new Insets(40, 30, 10, 20));
      setMinWidth(250);
      getChildren().addAll(
          nameLabel,
          ingredientsLabel,
          priceVbox
      );
    }};



    Label quantityTextLabel = new Label("Quantity: "); // (10)
    Label quantityCountLabel = new Label(quantity); // (11)

    HBox quantityBox = new HBox(){{ // (12)
      setHeight(100);
      setAlignment(Pos.CENTER);
      getChildren().addAll(
          quantityTextLabel,
          quantityCountLabel
      );
    }};

    ImageView menuItemImageView = new ImageView() {{ // (13)
      setPickOnBounds(true);
      setPreserveRatio(true);
    }};
    ClientImageLoader.loadImage(imagePath, menuItemImageView, imgSize, imgSize);

    VBox leftVbox = new VBox(){{ // (14)
      getChildren().addAll(
          menuItemImageView,
          quantityBox
      );
    }};

    menuItemImageView.setFitWidth(leftVbox.getWidth() - 10);
    menuItemImageView.setFitHeight(leftVbox.getHeight() - 10);


    HBox wrapper = new HBox(){{ // (15)
      setHeight(256);
      setWidth(400);
      setMinWidth(400);
      setPadding(new Insets(10,10,10,10));
      getChildren().addAll(
          leftVbox,
          rightVbox
      );
    }};

    return wrapper;
  }
}


