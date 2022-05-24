package client.view;

import client.core.ViewHandler;
import client.core.ViewModelFactory;

public interface TabController {
    /**
     * Method assigned for initializing parameters and binding necessary fields with ViewModel
     * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
     */
    void init(ViewModelFactory viewModelFactory);

    /**
     * Refreshes the Pane associated with the controller. Furthermore, this method is called when the
     * user switches the tabs, and the gui automatically refreshes.
     */
    void refresh();
}
