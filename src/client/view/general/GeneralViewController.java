package client.view.general;

import client.core.ViewHandler;
import client.core.ViewModelFactory;
import client.view.ViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

public class GeneralViewController implements ViewController
{
  @FXML private TabPane tabPane;
  @FXML private Label nameLabel;

  private UserStrategy userStrategy;

  @Override public void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory) {

  }

  @Override public void refresh() {

  }
}
