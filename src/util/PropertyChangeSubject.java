package util;

import java.beans.PropertyChangeListener;

/**An interface shared by all the classes that should be listened to(Observer Pattern)
 * Classes as: UserModelImpl, Client etc.
 */

public interface PropertyChangeSubject
{
  /**
   * A method that adds the listener for a specific type of event
   * @param event the event to be listened
   * @param listener to be added to listen to the subject
   */
  void addListener(String event, PropertyChangeListener listener);

  /**
   * A method that adds the listener to listen to all events
   * @param listener to be added to listen to the subject
   */
  void addListener(PropertyChangeListener listener);
}
