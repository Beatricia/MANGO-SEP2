package util;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * This is a helper class, contains date related method(s) .
 *
 * @author Greg
 * @version 1
 */
public class DateHelper
{

  /**
   * Gets the current week's monday if the current day is friday or before; else gets the next monday.
   * @return LocalDate monday object
   */
  public static LocalDate getCurrentAvailableMonday(){
    LocalDate now = LocalDate.now();

    DayOfWeek dayOfWeek = now.getDayOfWeek();

    if(dayOfWeek.getValue() >= 6) { // check if its weekend
      return now.plusDays(8 - dayOfWeek.getValue()); // if yes, then add days to the next monday
    } else {
      return now.minusDays(dayOfWeek.getValue() - 1); // if not, subtract days till the previous monday
    }
  }
}
