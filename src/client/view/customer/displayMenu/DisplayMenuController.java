package client.view.customer.displayMenu;

import client.core.ViewModelFactory;
import client.model.CartModel;
import client.imageHandler.ClientImageLoader;
import client.model.MenuModel;
import client.view.TabController;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import shared.Log;
import transferobjects.CartItem;
import transferobjects.MenuItemWithQuantity;
import util.ImageTools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for DisplayMenuView
 *
 * @author Greg, Simon
 * @version 1
 */
public class DisplayMenuController implements TabController
{
  @FXML private Label canteenClosedText;
  @FXML private VBox menuItemsVBox;
  @FXML private Label dateLabel;

  private static DisplayMenuViewModel viewModel;
  private static ArrayList<String>  itemsInCart;
  private static ArrayList<Button> buttons;

  /**
   * Initializes the controller
   * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
   */
  @Override public void init(ViewModelFactory viewModelFactory) {
    viewModel = viewModelFactory.getDisplayMenuViewModel();
    viewModel.menuItemWithQuantitiesList().addListener(this::menuItemListChangeListener);
    viewModel.addListener(MenuModel.OPENING_HOURS_RECEIVED, this::canteenStateChanged);
    viewModel.addListener(CartModel.IS_ITEM_IN_CART, this::itemsInShoppingCartReceived);


    LocalDate localDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    String dateText = localDate.getDayOfWeek() + " - " + localDate.format(formatter);
    dateLabel.setText(dateText);
    buttons = new ArrayList<>();
    itemsInCart = new ArrayList<>();
    canteenClosedText.setText("");
  }

  /**
   * Calls the method addMenuItemToCart in viewModel
   * @param menuItem instance of MenuItemWithQuantity which is added to cart
   */
  private static void addMenuItemToCart(MenuItemWithQuantity menuItem){
    Log.log("DisplayMenuController: Add item to a cart");
    viewModel.addMenuItemToCart(menuItem);
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
   * Listener for DisplayMenuViewModel. If change called OPENING_HOURS_RECEIVED is received it disables the daily menu vBox if canteen is closed
   * or it enables the vBox if the canteen is open
   * @param propertyChangeEvent Change in the opening state of the canteen
   */
  private void canteenStateChanged(PropertyChangeEvent propertyChangeEvent){
    Log.log("DisplayMenuController: canteen state change received");
    boolean canteenClosed = (boolean) propertyChangeEvent.getNewValue();

    if (canteenClosed){
      menuItemsVBox.setDisable(true);
      canteenClosedText.setText("Canteen is closed");
    }
    else {
      menuItemsVBox.setDisable(false);
      canteenClosedText.setText("");
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
   * Refreshes Display Menu tab.
   */
  @Override public void refresh() {
    buttons.clear();
    viewModel.requestDailyMenuItems();
    viewModel.requestOpeningHours();
    viewModel.isCanteenClosed();
    viewModel.itemsInShoppingCartRequest();
  }

  /**
   * This method listens to the viewModel. When change is caught, itemsIcCart list is updated and method checkButtons is called.
   * @param propertyChangeEvent event changed
   */
  private void itemsInShoppingCartReceived(PropertyChangeEvent propertyChangeEvent){
    PropertyChangeEvent propertyChangeEvent1 = (PropertyChangeEvent) propertyChangeEvent.getNewValue();
    itemsInCart = (ArrayList<String>) propertyChangeEvent1.getNewValue();


    checkButtons(itemsInCart);
  }


  /**
   * This method checks if any of the items on Daily menu are in the customer's cart.
   * If yes, it disables its button.
   * @param itemNames The list of names of items in cart
   */
  private void checkButtons(ArrayList<String> itemNames){
    Platform.runLater(()-> {
      for (String itemName:itemNames
      )
      {
        for (int i = 0; i < buttons.size(); i++)
        {
          String buttonItemName = buttons.get(i).getText();
          if (buttonItemName.contains(itemName)){
            buttons.get(i).setDisable(true);
            buttons.get(i).setText("Added");
          }
        }
      }
    });

  }


  /*
  createDailyMenuItemBox returns this structure for one MenuItemWithQuantity



       -Wrapper Hbox- (15)
     ┌──────────────────────────────────────────────────────────────────────────┐
     │   -Left VBox- (14)                       -Right VBox- (9)                │
     │ ┌───────────────────────────────────┐   ┌──────────────────────────────┐ │
     │ │  -ImageView- (13)                 │   │                              │ │
     │ │ ┌────────────────────────────┐    │   │  -Crown VBox- (8)            │ │
     │ │ │                            │    │   │ ┌──────────────────────────┐ │ │
     │ │ │                            │    │   │ │                  ┌─────┐ │ │ │
     │ │ │                            │    │   │ │    (7) ImageView │     │ │ │ │
     │ │ │                            │    │   │ │                  └─────┘ │ │ │
     │ │ │                            │    │   │ └──────────────────────────┘ │ │
     │ │ │                            │    │   │                              │ │
     │ │ │                            │    │   │  Name Label (1)              │ │
     │ │ │                            │    │   │                              │ │
     │ │ │                            │    │   │  Ingredients Label (2)       │ │
     │ │ │                            │    │   │                              │ │
     │ │ │                            │    │   │                              │ │
     │ │ │                            │    │   │ -Price VBox-(4)              │ │
     │ │ │                            │    │   │ ┌─────────────────────────┐  │ │
     │ │ └────────────────────────────┘    │   │ │         (3) Price Label │  │ │
     │ │                                   │   │ └─────────────────────────┘  │ │
     │ │   -Quantity HBox- (12)            │   │                              │ │
     │ │  ┌───────────────────────────┐    │   │     -Button VBox- (6)        │ │
     │ │  │QuantityLabel: number label│    │   │        ┌──────────┐          │ │
     │ │  └────(10)──────────(11)─────┘    │   │        │Button (5)│          │ │
     │ │                                   │   │        └──────────┘          │ │
     │ └───────────────────────────────────┘   └──────────────────────────────┘ │
     └──────────────────────────────────────────────────────────────────────────┘


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

    Button addMenuItemToCart = new Button(){{ // (5)
      setText("Add " + itemName);
      setOnAction(event -> {
        addMenuItemToCart(menuItemWithQuantity);
        setDisable(true);
        setText("Added");
      });
    }};

    buttons.add(addMenuItemToCart);

    VBox buttonVBox = new VBox(){{ // (6)
      setAlignment(Pos.TOP_RIGHT);
      setPadding(new Insets(10, 0, 0, 0));
      getChildren().add(addMenuItemToCart);
    }};


    BufferedImage image = ImageIO.read(new File("src/client/view/customer/displayMenu/crownImage/crown.png"));
    BufferedImage resizedImage = ImageTools.resizeImage(image,30,30);

    ImageView imageView = new ImageView(){{ // (7)
      setImage(ImageTools.convertToFXImage(resizedImage));
    }};
    VBox crownVBox = new VBox(){{ // (8)
      setAlignment(Pos.TOP_RIGHT);
      getChildren().add(imageView);
    }};

    VBox rightVbox = new VBox(){{ // (9)
      setPadding(new Insets(10, 30, 10, 20));
      setMinWidth(250);
      if (menuItemWithQuantity.isTopThree()){
        getChildren().add(crownVBox);
      }
      getChildren().addAll(
          nameLabel,
          ingredientsLabel,
          priceVbox,
          buttonVBox
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
}


