package client.view.admin.statistics;

import client.model.AdminModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.Log;
import transferobjects.Statistics;
import util.PropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * ViewModel of the statistics.fxml view
 *
 * @author Simon
 */
public class StatisticsViewModel implements PropertyChangeSubject
{
  private AdminModel adminModel;
  private StringProperty itemTop1;
  private StringProperty itemTop2;
  private StringProperty itemTop3;
  private PropertyChangeSupport propertyChangeSupport;

  /**
   * Constructor of the ViewModel, that initializes the private variables
   *
   * @param adminModel instance of AdminModel
   */
  public StatisticsViewModel(AdminModel adminModel)
  {
    this.adminModel = adminModel;
    adminModel.addListener(AdminModel.STATISTICS_RECEIVED, this::statisticsReceived);
    propertyChangeSupport = new PropertyChangeSupport(this);

    itemTop1 = new SimpleStringProperty("");
    itemTop2 = new SimpleStringProperty("");
    itemTop3 = new SimpleStringProperty("");
  }

  /**
   * Method which calls the requestStatistics method in the AdminModel
   */
  public void requestStatistics()
  {
    Log.log("StatisticsViewModel: requestStatistics called in AdminModel");
    adminModel.requestStatistics();
  }

  /**
   * Method which is called when STATISTICS_RECEIVED change is received.
   * Updates bound properties.
   * Fires propertyChange with STATISTICS_RECEIVED event name.
   * @param propertyChangeEvent change received
   */
  private void statisticsReceived(PropertyChangeEvent propertyChangeEvent)
  {
    Statistics statistics = (Statistics) propertyChangeEvent.getNewValue();

    Platform.runLater(() -> {
      try{
        itemTop1.set(statistics.getTopThreeMeals().get(0).getName());
        itemTop2.set(statistics.getTopThreeMeals().get(1).getName());
        itemTop3.set(statistics.getTopThreeMeals().get(2).getName());
      } catch (IndexOutOfBoundsException e){
        e.printStackTrace();
      }
    });

    propertyChangeSupport.firePropertyChange(AdminModel.STATISTICS_RECEIVED, null, statistics);
  }

  /**
   * Method which is observed by the ViewController
   *
   * @return StringProperty which updates value of the Label in the Controller
   */
  public StringProperty getItemTop1()
  {
    return itemTop1;
  }

  /**
   * Method which is observed by the ViewController
   *
   * @return StringProperty which updates value of the Label in the Controller
   */
  public StringProperty getItemTop2()
  {
    return itemTop2;
  }

  /**
   * Method which is observed by the ViewController
   *
   * @return StringProperty which updates value of the Label in the Controller
   */
  public StringProperty getItemTop3()
  {
    return itemTop3;
  }

  @Override public void addListener(String event,
      PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(event, listener);
  }

  @Override public void addListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }
}
