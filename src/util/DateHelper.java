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


    // 1: Monday         5: Friday
    // 2: Tuesday        6: Saturday
    // 3: Wednesday      7: Sunday
    // 4: Thursday
    int dayOfWeekNum = dayOfWeek.getValue();

    if(dayOfWeekNum >= 6) { // check if its weekend
      return now.plusDays(8 - dayOfWeekNum); // if yes, then add days to the next monday
    } else {
      return now.minusDays(dayOfWeekNum - 1); // if not, subtract days till the previous monday
    }
  }

  /**
   * Gets the current week's monday
   * @return LocalDate monday object
   */
  public static LocalDate getCurrentAvailableMondayForStatistic()
  {
    LocalDate now = LocalDate.now();

    DayOfWeek dayOfWeek = now.getDayOfWeek();


    // 1: Monday         5: Friday
    // 2: Tuesday        6: Saturday
    // 3: Wednesday      7: Sunday
    // 4: Thursday
    int dayOfWeekNum = dayOfWeek.getValue();

      return now.minusDays(dayOfWeekNum - 1);

  }
}
