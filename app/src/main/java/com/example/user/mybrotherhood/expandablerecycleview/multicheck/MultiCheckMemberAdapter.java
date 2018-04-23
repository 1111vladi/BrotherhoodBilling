package com.example.user.mybrotherhood.expandablerecycleview.multicheck;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.mybrotherhood.expandablerecycleview.Category;
import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.expandablerecycleview.viewholders.GenreViewHolder;

import java.util.List;

public class MultiCheckMemberAdapter extends
    CheckableChildRecyclerViewAdapter<GenreViewHolder, MultiCheckCategoryViewHolder> {

  public MultiCheckMemberAdapter(List<MultiCheckMember> groups) {
    super(groups);
  }

  @Override
  public MultiCheckCategoryViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_multicheck_category, parent, false);
    return new MultiCheckCategoryViewHolder(view);
  }

  @Override
  public void onBindCheckChildViewHolder(MultiCheckCategoryViewHolder holder, int position,
                                         CheckedExpandableGroup group, int childIndex) {
    final Category category = (Category) group.getItems().get(childIndex);
    holder.setCategoryName(category.getName());
  }

  @Override
  public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_brotherhood_member, parent, false);
    return new GenreViewHolder(view);
  }

  @Override
  public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition,
      ExpandableGroup group) {
    holder.setGenreTitle(group);
  }
}
