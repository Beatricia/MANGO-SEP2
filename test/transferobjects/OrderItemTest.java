package transferobjects;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class OrderItemTest
{

  static final String itemName = "test-item";
  static final String imgPath = "imageFile.png";
  static final String username = "best-customer";
  static final double price = 2.4;
  static final int quantity = 2;
  static final int code = 2;
  static final ArrayList<String> ingredients = new ArrayList<>(Arrays.asList("item1", "item2", "item3", "item4", "item5"));
  static final ArrayList<String> unselectedIngredients = new ArrayList<>(Arrays.asList("item1", "item4"));



  OrderItem orderItem;

  @Before public void setUp() {

    orderItem = new OrderItem(
        itemName, ingredients, price,
        imgPath, username, quantity,
        unselectedIngredients, code);
  }

  // -----------------------
  //    Menu Item methods
  // -----------------------

  @Test public void checkGetName() {
    assertEquals("test-item", orderItem.getName());
  }

  @Test public void checkGetPrice() {
    assertEquals(2.4, orderItem.getPrice(), 2);
  }

  @Test public void checkGetIngredients() {
    List<String> ingredients = Arrays.asList("item1", "item2", "item3", "item4", "item5");

    assertEquals(5, orderItem.getIngredients().size());

    for (int i = 0; i < ingredients.size(); i++) {
      assertEquals(orderItem.getIngredients().get(i), ingredients.get(i));
    }
  }

  @Test public void checkGetImagePath() {
    assertEquals("imageFile.png", orderItem.getImgPath());
  }

  @Test public void checkNullIngredients(){
    orderItem = new OrderItem(itemName, null, price,
        imgPath, username, quantity,
        new ArrayList<>(), code);

    assertNotEquals(null, orderItem.getIngredients());
    assertEquals(0, orderItem.getIngredients().size());
  }

  @Test(expected = NullPointerException.class) public void checkNameNull(){
    try{

      new OrderItem(null, ingredients, price,
          imgPath, username, quantity,
          unselectedIngredients, code);

    } catch (Exception e){

      assertEquals("item name is null", e.getMessage());
      throw e;
    }
  }


  @Test(expected = IllegalArgumentException.class) public void checkNameBlank(){
    try{

      new OrderItem("  ", ingredients, price,
          imgPath, username, quantity,
          unselectedIngredients, code);

    } catch (Exception e){

      assertEquals("item name is blank", e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class) public void checkPriceZero(){
    try{
      new OrderItem(itemName, ingredients, 0,
          imgPath, username, quantity,
          unselectedIngredients, code);

    } catch (Exception e){
      assertEquals("price must be higher than 0", e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class) public void checkPriceMinusTwo(){
    try{
      new OrderItem(itemName, ingredients, -2,
          imgPath, username, quantity,
          unselectedIngredients, code);

    } catch (Exception e){
      assertEquals("price must be higher than 0", e.getMessage());
      throw e;
    }
  }

  // -----------------------
  //    Cart Item methods
  // -----------------------

  @Test public void checkGetUsername() {
    assertEquals("best-customer", orderItem.getUsername());
  }

  @Test public void checkGetQuantity() {
    assertEquals(2, orderItem.getQuantity());
  }

  @Test public void setQuantityToFour() {
    orderItem.setQuantity(4);
    assertEquals(4, orderItem.getQuantity());
  }

  @Test public void checkGetUnselectedIngredients(){
    List<String> unselected = Arrays.asList("item1", "item4");

    assertEquals(2, orderItem.getUnselectedIngredients().size());

    for (int i = 0; i < unselected.size(); i++) {
      assertEquals(unselected.get(i), orderItem.getUnselectedIngredients().get(i));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void setQuantityToZero() {
    try{
      orderItem.setQuantity(0);
    } catch (Exception e){
      assertEquals("quantity is less than or equals to zero", e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void setQuantityToMinusOne() {
    try{
      orderItem.setQuantity(-1);
    } catch (Exception e){
      assertEquals("quantity is less than or equals to zero", e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class) public void checkQuantityZero(){
    try{
      new OrderItem(itemName, ingredients, price,
          imgPath, username, 0,
          unselectedIngredients, code);

    } catch (Exception e){
      assertEquals("quantity is less than or equals to zero", e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class) public void checkQuantityMinusTwo(){
    try{
      new OrderItem(itemName, ingredients, price,
          imgPath, username, -2,
          unselectedIngredients, code);

    } catch (Exception e){
      assertEquals("quantity is less than or equals to zero", e.getMessage());
      throw e;
    }
  }

  @Test(expected = NullPointerException.class) public void checkUsernameNull() {
    try{
      new OrderItem(itemName, ingredients, price,
          imgPath, null, quantity,
          unselectedIngredients, code);

    } catch (Exception e){
      assertEquals("username is null", e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class) public void checkUsernameBlank() {
    try{
      new OrderItem(itemName, ingredients, price,
          imgPath, " ", quantity,
          unselectedIngredients, code);

    } catch (Exception e){
      assertEquals("username is blank", e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class) public void checkWrongUnselectedIngredients() {
    try{
      ArrayList<String> wrongUnselected = new ArrayList<>(Arrays.asList("item1", "item6", "item4"));

      new OrderItem(itemName, ingredients, price,
          imgPath, username, quantity,
          wrongUnselected, code);

    } catch (Exception e){
      assertEquals("unselected ingredient item6 is not present in the ingredients list", e.getMessage());
      throw e;
    }
  }

  // -----------------------
  //    Order Item methods
  // -----------------------

  @Test public void checkGetCode() {
    assertEquals(2, orderItem.getCode());
  }

  @Test public void checkGetDate() {
    assertEquals(LocalDate.now(), orderItem.getDate());
  }
}