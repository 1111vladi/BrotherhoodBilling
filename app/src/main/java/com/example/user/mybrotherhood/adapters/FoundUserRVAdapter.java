package com.example.user.mybrotherhood.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.mybrotherhood.Category;
import com.example.user.mybrotherhood.R;

import java.util.List;


/**
 * Created by Nico on 13/04/2018.
 */

public class FoundUserRVAdapter extends RecyclerView.Adapter<FoundUserRVAdapter.FoundUserViewHolder>{

    // Position of the selected item all the time
    private int selectedPos = RecyclerView.NO_POSITION;

    private List<String> foundUsers;
    private FoundUserOnClickListener foundUserListener;

    // Constructor Overload - list of names and listener
    public FoundUserRVAdapter(List<String> foundUsers, FoundUserOnClickListener foundUserListener) {
        this.foundUsers = foundUsers;
        this.foundUserListener = foundUserListener;
    }

    // Interface
    public interface FoundUserOnClickListener {
        void passCategoryName(View view, int position);
    }

    public class FoundUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView categoryCv;
        TextView categoryName;

        FoundUserViewHolder(View itemView) {
            super(itemView);
            categoryCv = (CardView) itemView.findViewById(R.id.cv_category);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;

            // Updating background of old as well as new item positions
            notifyItemChanged(selectedPos);
            selectedPos = getAdapterPosition();
            notifyItemChanged(selectedPos);

            foundUserListener.passCategoryName(view, this.getLayoutPosition());
        }
    }


    // Update the RecyclerView when an animation happen
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Creating the ViewHolder with the designated item
    @Override
    public FoundUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new FoundUserViewHolder(view);
    }


    // Set the settings of CardView
    @Override
    public void onBindViewHolder(FoundUserViewHolder holder, int position) {
        holder.categoryName.setText(foundUsers.get(position)); //  Which text to display
        // This is the background color of an item before any other action was take,
        // the first item will be colored -> color = selected
        holder.categoryCv.setCardBackgroundColor(selectedPos == position ? Color.GREEN : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return foundUsers.size();
    }

}
