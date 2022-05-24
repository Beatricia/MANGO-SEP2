package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.TabController;
import client.view.ViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import shared.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

/**
 * Base class for the strategy pattern. This class is responsible for loading
 * the right tabs into the GeneralView's tab pane.<br>
 * The classes which extends UserStrategy have to implement the loadTabs() method, which should
 * call the inner protected loadTabs(String[] tabs) method.
 * @author Greg
 * @version 1
 */
public abstract class UserStrategy
{
  private final TabPane tabPane; // tabpane the tabs will loaded into
  private final ViewHandler viewHandler;
  private final ViewModelFactory viewModelFactory;
  private final HashMap<Tab, TabController> controllers; // Tabs and their controllers

  /**
   * Initialize the UserStrategy object, throwing NullPointerException if any of the parameter is null
   * @param tabPane tabpane the tabs will be loaded into it
   * @param viewHandler view handler
   * @param viewModelFactory view model factory
   * @throws NullPointerException if any of the parameter is null
   */
  public UserStrategy(TabPane tabPane, ViewHandler viewHandler, ViewModelFactory viewModelFactory){
    this.tabPane = Objects.requireNonNull(tabPane);
    this.viewHandler = Objects.requireNonNull(viewHandler);
    this.viewModelFactory = Objects.requireNonNull(viewModelFactory);
    controllers = new HashMap<>();

    Log.log("UserStrategy Initializing " + this);
  }

  /**
   * Base method for loading tabs, must be overriden. This method should be called outside of the Strategy pattern.
   */
  public abstract void loadTabs();

  /**
   * Inner helper method loading tabs from the disk. The tabs' path and name should be in the array
   * given in the parameter. The structure of the array must be like this:
   * <ul>
   *   <li>tab path 1</li>
   *   <li>tab name 1</li>
   *   <li>tab path 2</li>
   *   <li>tab name 2</li>
   * </ul>
   * @param basePath common path to the tabs (this will be joined with the tab paths)
   * @param tabs tab paths to load in (relative to the base path)
   */
  protected void loadTabs(String basePath, String[] tabs){
    for (int i = 0; i < tabs.length; i += 2) {
      String path = basePath + tabs[i];
      String tabName = tabs[i + 1];

      Log.log("Loading tab " + tabName + " from " + path);

      loadTabFromFile(path, tabName);
    }
  }

  /**
   * Load one tab from file
   * @param path tab path to load (path must start from src/)
   * @param tabName name of the tab
   */
  private void loadTabFromFile(String path, String tabName){
    FXMLLoader loader = new FXMLLoader();
    try{
      //set path for the tab (it starts from src/ folder)
      URL url = new URL(new URL("file:"), path);
      loader.setLocation(url);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    Pane root = null;

    try {
      root = loader.load();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    TabController controller = loader.getController();
    controller.init(viewModelFactory);


    Tab tab = new Tab();
    tab.setContent(root);
    tab.setText(tabName);

    //register the tab so it can be refreshed later
    registerTab(tab, controller);
    //add the tab to the tab pane
    tabPane.getTabs().add(tab);
  }

  /**
   * Checks if the tab is null, and puts the tab with it's associated controller into the hash map
   * @param tab the tab
   * @param tabController associated controller
   */
  private void registerTab(Tab tab, TabController tabController){
    Log.log("Registering tab " + tab.getText());
    Objects.requireNonNull(tabController);
    controllers.put(tab, tabController);
  }

  /**
   * Refresh a specific tab by calling refresh method on it's controller
   * @param tab tab to refresh
   */
  public void refreshTab(Tab tab){
    Log.log("Refreshing tab " + tab.getText());
    TabController controller = controllers.get(tab);

    try{
      controller.refresh();
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  /**
   * Returns the class's name
   * @return the class's name
   */
  @Override public String toString(){
    return getClass().getSimpleName();
  }
}
