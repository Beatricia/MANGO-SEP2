package client.view.ViewHelpers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

/**
 * SelectionModel without selection
 * @param <T> type of the selectable item
 */
public class NoSelectionModel<T> extends MultipleSelectionModel<T>
{
  @Override public ObservableList<Integer> getSelectedIndices() {return FXCollections.observableArrayList();}
  @Override public ObservableList<T> getSelectedItems() {return FXCollections.observableArrayList();}
  @Override public void selectIndices(int i, int... ints) {}
  @Override public void selectAll() {}
  @Override public void selectFirst() {}
  @Override public void selectLast() {}
  @Override public void clearAndSelect(int i) {}
  @Override public void select(int i) {}
  @Override public void select(Object o) {}
  @Override public void clearSelection(int i) {}
  @Override public void clearSelection() {}
  @Override public boolean isSelected(int i) {return false;}
  @Override public boolean isEmpty() {return true;}
  @Override public void selectPrevious() {}
  @Override public void selectNext() {}
}
