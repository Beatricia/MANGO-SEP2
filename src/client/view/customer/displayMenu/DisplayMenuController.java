package client.view.customer.displayMenu;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayMenuController implements ViewController
{
  public VBox menuItemsVBox;
  @FXML private Label dateLabel;

  private DisplayMenuViewModel viewModel;

  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {
    //viewModel = viewModelFactory.getDisplayMenuViewModel();
    viewModel = new TestViewModel();
    viewModel.menuItemWithQuantitiesList().addListener(this::menuItemListChangeListener);

    LocalDate localDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    String dateText = localDate.getDayOfWeek() + " - " + localDate.format(formatter);
    dateLabel.setText(dateText);
  }

  private Pane getInsertPane(){
    return menuItemsVBox;
  }
  private void menuItemListChangeListener(ListChangeListener.Change<? extends MenuItemWithQuantity> change) {
    change.next();

    if(change.wasAdded()){
      addedMenuItemWithQuantity(change.getAddedSubList());
    }
    else if(change.wasRemoved()){
      menuItemsVBox.getChildren().clear();
    }
  }

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

  private void putIntoHBox(Pane menuItemPane){
    int vboxChildrenSize = menuItemsVBox.getChildren().size();
    HBox lastHBox = (HBox) menuItemsVBox.getChildren().get(vboxChildrenSize - 1);

    if(lastHBox.getChildren().size() >= 2){
      lastHBox = new HBox(){{
        setPadding(new Insets(50, 0, 0, 0));
      }};
      menuItemsVBox.getChildren().add(lastHBox);
    }

    lastHBox.getChildren().add(menuItemPane);
  }

  @Override public void refresh() {
    viewModel.requestDailyMenuItems();
  }

  private HBox createDailyMenuItemBox(MenuItemWithQuantity menuItemWithQuantity) throws
      IOException {
    String itemName = menuItemWithQuantity.getName();
    String ingredients = String.join(", ", menuItemWithQuantity.getIngredients());
    String price = menuItemWithQuantity.getPrice() + " DKK";
    String quantity = menuItemWithQuantity.getQuantity() + "";
    String imagePath = menuItemWithQuantity.getImgPath();



    Label nameLabel = new Label(itemName){{
      setFont(Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, 24));
      setWrapText(true);
    }};
    Label ingredientsLabel = new Label(ingredients){{
      setWrapText(true);
    }};
    Label priceLabel = new Label(price);

    VBox priceVbox = new VBox(){{
      setAlignment(Pos.TOP_RIGHT);
      getChildren().add(priceLabel);
      setPadding(new Insets(20, 0, 0, 0));
    }};

    VBox rightVbox = new VBox(){{
      setPadding(new Insets(10, 30, 10, 20));
      getChildren().addAll(
          nameLabel,
          ingredientsLabel,
          priceVbox
      );
    }};



    Label quantityTextLabel = new Label("Quantity: ");
    Label quantityCountLabel = new Label(quantity);

    HBox quantityBox = new HBox(){{
      setAlignment(Pos.CENTER);
      getChildren().addAll(
          quantityTextLabel,
          quantityCountLabel
      );
    }};

    BufferedImage menuItemImage = ImageIO.read(new File(imagePath));
    Image javafxImage = SwingFXUtils.toFXImage(menuItemImage, null);
    ImageView menuItemImageView = new ImageView(){{
      setImage(javafxImage);
    }};

    VBox leftVbox = new VBox(){{
      getChildren().addAll(
          menuItemImageView,
          quantityBox
      );
    }};

    menuItemImageView.setFitWidth(leftVbox.getWidth() - 10);
    menuItemImageView.setFitHeight(leftVbox.getHeight() - 10);


    HBox wrapper = new HBox(){{
      setMaxWidth((getInsertPane().getMaxWidth() - 1) / 2);
      getChildren().addAll(
          leftVbox,
          rightVbox
      );
    }};


    return wrapper;
  }
}


class TestViewModel extends DisplayMenuViewModel{

  public TestViewModel() {
    super(null);
  }

  private final ObservableList<MenuItemWithQuantity> menuItemsTest = FXCollections.observableArrayList();

  @Override public ObservableList<MenuItemWithQuantity> menuItemWithQuantitiesList() {
    return menuItemsTest;
  }

  @Override public void requestDailyMenuItems() {
    menuItemsTest.clear();

    for (int i = 0; i < 7; i++) {
      MenuItem menuItem = new MenuItem("abc", new ArrayList<>(Arrays.asList("cucumber", "banana", "hamburger")), 3.4, "Resources/MenuItemImages/abc.png");
      MenuItemWithQuantity menuItemWithQuantity = new MenuItemWithQuantity(menuItem, LocalDate.now(), 3);
      menuItemsTest.add(menuItemWithQuantity);
    }
  }
}