package client.view.admin.statistics;

import client.core.ViewModelFactory;
import client.model.AdminModel;
import client.view.TabController;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import transferobjects.Statistics;

import java.beans.PropertyChangeEvent;

/**
 * Controller for the statistics.fxml
 * @author Simon
 */
public class StatisticsController implements TabController
{
  @FXML private Label itemTop1;
  @FXML private Label itemTop2;
  @FXML private Label itemTop3;
  @FXML private BarChart<String, Number> barChart;
  @FXML private CategoryAxis xAxis;
  @FXML private NumberAxis yAxis;

  private StatisticsViewModel viewModel;

  /**
   * Method that initializes the controller
   * @param viewModelFactory instance of ViewModelFactory class, where ViewModels are created
   */
  @Override public void init(ViewModelFactory viewModelFactory)
  {
    viewModel = viewModelFactory.getStatisticsViewModel();
    viewModel.addListener(AdminModel.STATISTICS_RECEIVED, this::updateBarChart);

    //top3 items
    itemTop1.textProperty().bind(viewModel.getItemTop1());
    itemTop2.textProperty().bind(viewModel.getItemTop2());
    itemTop3.textProperty().bind(viewModel.getItemTop3());

    //chart
    xAxis = new CategoryAxis();
    yAxis = new NumberAxis();
    barChart.setAnimated(false);
    barChart.setLegendVisible(false);

  }

  @Override public void refresh()
  {
    viewModel.requestStatistics();
  }

  /**
   * Method that updates the barChart (Clears data first, then inserts new data from received change)
   * @param propertyChangeEvent received event
   */
  private void updateBarChart(PropertyChangeEvent propertyChangeEvent){
    Statistics statistics = (Statistics) propertyChangeEvent.getNewValue();

    int numberOfOrdersMonday = statistics.getNumberOfOrders().get(0);
    int numberOfOrdersTuesday = statistics.getNumberOfOrders().get(1);
    int numberOfOrdersWednesday = statistics.getNumberOfOrders().get(2);
    int numberOfOrdersThursday = statistics.getNumberOfOrders().get(3);
    int numberOfOrdersFriday = statistics.getNumberOfOrders().get(4);

    double incomeMonday = statistics.getIncome().get(0);
    double incomeTuesday = statistics.getIncome().get(1);
    double incomeWednesday = statistics.getIncome().get(2);
    double incomeThursday = statistics.getIncome().get(3);
    double incomeFriday = statistics.getIncome().get(4);

    barChart.getData().clear();
    XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    series1.getData().add(new XYChart.Data<String, Number>("Monday\n" + numberOfOrdersMonday, incomeMonday));
    series1.getData().add(new XYChart.Data<String, Number>("Tuesday\n" + numberOfOrdersTuesday, incomeTuesday));
    series1.getData().add(new XYChart.Data<String, Number>("Wednesday\n" + numberOfOrdersWednesday, incomeWednesday));
    series1.getData().add(new XYChart.Data<String, Number>("Thursday\n" + numberOfOrdersThursday, incomeThursday));
    series1.getData().add(new XYChart.Data<String, Number>("Friday\n" + numberOfOrdersFriday, incomeFriday));
    barChart.getData().add(series1);
  }
}
