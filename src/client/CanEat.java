package client;

import client.core.ClientFactory;
import client.core.ModelFactory;
import client.core.ViewHandler;
import client.core.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The class' responsibility is to creat the ClientFactory, ModelFactory,
 * ViewModelFactory and ViewHandler objects and to call the start() method in
 * the ViewHandler. The class extends the Java build in class Application,
 * therefore overrides its start method.
 */
public class CanEat extends Application
{
  @Override public void start(Stage stage) throws Exception
  {
    ClientFactory clientFactory= new ClientFactory();
    ModelFactory modelFactory = new ModelFactory(clientFactory);
    ViewModelFactory viewModelFactory  = new ViewModelFactory(modelFactory);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory, stage);

    viewHandler.start();
  }
}
