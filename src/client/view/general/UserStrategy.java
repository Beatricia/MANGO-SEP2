package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public abstract class UserStrategy
{
  private final TabPane tabPane;
  private final ViewHandler viewHandler;
  private final ViewModelFactory viewModelFactory;
  private final HashMap<Tab, ViewController> controllers;

  public UserStrategy(TabPane tabPane, ViewHandler viewHandler, ViewModelFactory viewModelFactory){
    this.tabPane = Objects.requireNonNull(tabPane);
    this.viewHandler = Objects.requireNonNull(viewHandler);
    this.viewModelFactory = Objects.requireNonNull(viewModelFactory);
    controllers = new HashMap<>();
  }

  public abstract void loadTabs();

  protected void loadTabs(String[] tabs){
    for (int i = 0; i < tabs.length; i += 2) {
      String path = tabs[i];
      String tabName = tabs[i + 1];

      loadTabFromFile(path, tabName);
    }
  }

  protected void loadTabFromFile(String path, String tabName){
    FXMLLoader loader = new FXMLLoader();
    try{
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

    ViewController controller = loader.getController();
    controller.init(viewHandler, viewModelFactory);


    Tab tab = new Tab();
    tab.setContent(root);
    tab.setText(tabName);

    registerTab(tab, controller);
    tabPane.getTabs().add(tab);
  }

  private void registerTab(Tab tab, ViewController viewController){
    Objects.requireNonNull(viewController);
    controllers.put(tab, viewController);
  }

  public void refreshTab(Tab tab){
    ViewController controller = controllers.get(tab);
    controller.refresh();
  }
}
