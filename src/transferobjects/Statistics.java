package transferobjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class that is used for statistics in the admin view, it stores the number of orders of every week day in an array list
 * the top three meals from the menu items
 * and the income for every week day
 *
 * @author Beatricia
 */

public class Statistics implements Serializable
{
    private ArrayList<Integer> numberOfOrders;
    private ArrayList<MenuItem> topThreeMeals;
    private ArrayList<Double> income;

    /**
     * A no argument constructor that initializes the array lists
     */
    public Statistics(){
        numberOfOrders = new ArrayList<>();
        topThreeMeals = new ArrayList<>();
        income = new ArrayList<>();
    }

    /**
     * A three argument constructor that sets the current array lists to the ones that are given as parameters
     * @param numberOfOrders the array list that stores the number of orders for every week day
     * @param topThreeMeals the array list that stores the top three meals
     * @param income the array list that stores the income for every week day
     */
    public Statistics(ArrayList<Integer> numberOfOrders, ArrayList<MenuItem> topThreeMeals, ArrayList<Double> income){
        this.numberOfOrders = numberOfOrders;
        this.topThreeMeals = topThreeMeals;
        this.income = income;
    }

    /**
     * Clearing the previous array and setting the new one that will store the number of orders for every week day
     * @param nrOrders the number of orders
     */
    public void setNumberOfOrders(ArrayList<Integer> nrOrders){
        numberOfOrders.clear();
        numberOfOrders.addAll(nrOrders);
    }

    /**
     * Clearing the previous array and setting the new one that will store the top three menu items
     * @param top the top menu item
     */
    public void setTopThreeMeals(ArrayList<MenuItem> top){
        topThreeMeals.clear();
        topThreeMeals.addAll(top);
    }

    /**
     * Clearing the previous array and setting the new one that will store the income for every week day
     * @param inc the income
     */
    public void setIncome(ArrayList<Double> inc){
        inc.clear();
        income.addAll(inc);
    }

    /**
     * Getting the array list of number of orders
     * @return the array list of number of orders
     */
    public ArrayList<Integer> getNumberOfOrders(){
        return numberOfOrders;
    }

    /**
     * Getting the array list of top three meals
     * @return the array list of top three meals
     */
    public ArrayList<MenuItem> getTopThreeMeals(){
        return topThreeMeals;
    }

    /**
     * Getting the array list of incomes
     * @return the array list of incomes
     */
    public ArrayList<Double> getIncome(){
        return income;
    }

}
