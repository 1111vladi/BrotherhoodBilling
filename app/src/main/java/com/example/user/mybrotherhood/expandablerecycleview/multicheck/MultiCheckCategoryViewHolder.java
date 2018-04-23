package com.example.user.mybrotherhood.expandablerecycleview.multicheck;

import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;

import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.expandablerecycleview.viewholders.CheckableChildViewHolder;

public class MultiCheckCategoryViewHolder extends CheckableChildViewHolder {

  private CheckedTextView childCheckedTextView;

  public MultiCheckCategoryViewHolder(View itemView) {
    super(itemView);
    childCheckedTextView =
        (CheckedTextView) itemView.findViewById(R.id.list_item_multicheck_category_name);
  }

  @Override
  public Checkable getCheckable() {
    return childCheckedTextView;
  }

  public void setCategoryName(String CategoryName) {
    childCheckedTextView.setText(CategoryName);
  }
}
