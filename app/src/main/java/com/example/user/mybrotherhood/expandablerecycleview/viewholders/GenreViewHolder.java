package com.example.user.mybrotherhood.expandablerecycleview.viewholders;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.mybrotherhood.R;
import com.example.user.mybrotherhood.expandablerecycleview.multicheck.ExpandableGroup;
import com.example.user.mybrotherhood.expandablerecycleview.multicheck.MultiCheckMember;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class GenreViewHolder extends GroupViewHolder {

  private TextView genreName;
  private ImageView arrow;


  public GenreViewHolder(View itemView) {
    super(itemView);
    genreName = (TextView) itemView.findViewById(R.id.list_item_member_name);
    arrow = (ImageView) itemView.findViewById(R.id.list_item_member_arrow);

  }

  public void setGenreTitle(ExpandableGroup genre) {


    if (genre instanceof MultiCheckMember) {
      genreName.setText(genre.getTitle());
    }

  }

  @Override
  public void expand() {
    animateExpand();
  }

  @Override
  public void collapse() {
    animateCollapse();
  }

  private void animateExpand() {
    RotateAnimation rotate =
        new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
    rotate.setDuration(300);
    rotate.setFillAfter(true);
    arrow.setAnimation(rotate);
  }

  private void animateCollapse() {
    RotateAnimation rotate =
        new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
    rotate.setDuration(300);
    rotate.setFillAfter(true);
    arrow.setAnimation(rotate);
  }
}
