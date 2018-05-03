package com.example.user.mybrotherhood.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.mybrotherhood.Brotherhood;
import com.example.user.mybrotherhood.FirebaseBrotherhood;
import com.example.user.mybrotherhood.itemtouchhelper.ItemTouchHelperAdapter;
import com.example.user.mybrotherhood.activities.BrotherhoodTabs;
import com.example.user.mybrotherhood.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 3/21/2018.
 */

public class BrotherhoodRVAdapter extends RecyclerView.Adapter<BrotherhoodRVAdapter.BrotherhoodViewHolder> implements ItemTouchHelperAdapter{

    public static class BrotherhoodViewHolder extends RecyclerView.ViewHolder {

        CardView brotherhoodCv;
        TextView brotherhoodName;


        public BrotherhoodViewHolder(View itemView) {
            super(itemView);
            brotherhoodCv = (CardView)itemView.findViewById(R.id.cv_brotherhood);
            brotherhoodName = (TextView)itemView.findViewById(R.id.brotherhoodName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseBrotherhood.setBrotherhoodName(brotherhoodName.getText().toString());
                    view.getContext().startActivity(new Intent(view.getContext(), BrotherhoodTabs.class));
                }
            });
        }


    }

    protected List<Brotherhood> brotherhoods;
    public BrotherhoodRVAdapter(List<Brotherhood> brotherhoods){
        this.brotherhoods = brotherhoods;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public BrotherhoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brotherhood, parent, false);
        BrotherhoodViewHolder bvh = new BrotherhoodViewHolder(view);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BrotherhoodViewHolder holder, int position) {

        holder.brotherhoodName.setText(brotherhoods.get(position).brotherhoodName);
    }

    @Override
    public int getItemCount() {
        return brotherhoods.size();
    }



    @Override
    public void onItemDismiss(int position) {
        notifyItemRemoved(position);
        brotherhoods.remove(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(brotherhoods, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(brotherhoods, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

}
