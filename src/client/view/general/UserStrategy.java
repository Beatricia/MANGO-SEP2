package client.view.general;

import client.core.ViewHandler;
import client.view.ViewController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.HashMap;
import java.util.Objects;

public abstract class UserStrategy
{
  private TabPane tabPane;
  private ViewHandler viewHandler;
  private HashMap<Tab, ViewController> controllers;

  public UserStrategy(TabPane tabPane, ViewHandler viewHandler){
    this.tabPane = Objects.requireNonNull(tabPane);
    this.viewHandler = Objects.requireNonNull(viewHandler);
    controllers = new HashMap<>();
  }

  public abstract void loadTabs();

  protected void registerTab(Tab tab, ViewController viewController){
    Objects.requireNonNull(viewController);
    controllers.put(tab, viewController);
  }

  public void refreshTab(Tab tab){
    ViewController controller = controllers.get(tab);
    controller.refresh();
  }
}
