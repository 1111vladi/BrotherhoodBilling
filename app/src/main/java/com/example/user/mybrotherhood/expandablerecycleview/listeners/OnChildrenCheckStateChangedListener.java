package com.example.user.mybrotherhood.expandablerecycleview.listeners;

import com.example.user.mybrotherhood.expandablerecycleview.multicheck.CheckedExpandableGroup;

public interface OnChildrenCheckStateChangedListener {

  /**
   * @param firstChildFlattenedIndex the flat position of the first child in the {@link
   * CheckedExpandableGroup}
   * @param numChildren the total number of children in the {@link CheckedExpandableGroup}
   */
  void updateChildrenCheckState(int firstChildFlattenedIndex, int numChildren);
}