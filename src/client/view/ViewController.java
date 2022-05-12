package client.view;

import client.core.ViewHandler;
import client.core.ViewModelFactory;

/**
 * Class responsible for connecting the ServerHandler with DatabaseConn.
 * @author Greg
 * @version 1
 */
public interface ViewController
{
  /**
   * Method assigned for initializing parameters and binding necessary fields with ViewModel
   * @param viewHandler instance of ViewHandler class, which is responsible for managing the GUI views
   * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
   */
  void init(ViewHandler viewHandler, ViewModelFactory viewModelFactory);

  /**
   * Refreshes the Pane associated with the controller. Furthermore, this method is called when the
   * user switches the tabs, and the gui automatically refreshes.
   */
  void refresh();
}
