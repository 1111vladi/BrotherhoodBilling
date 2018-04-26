package com.example.user.mybrotherhood.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.mybrotherhood.R;

import java.util.List;

/**
 * Created by Nico on 26/04/2018.
 */

public class ShowUsersRVAdapter extends RecyclerView.Adapter<ShowUsersRVAdapter.ShowUsersViewHolder> {

    // Position of the selected item all the time
    private int selectedPos = 0;

    private List<String> foundUsers;

    // Constructor Overload - only with list of names
    public ShowUsersRVAdapter(List<String> foundUsers) {
        this.foundUsers = foundUsers;
    }

    public class ShowUsersViewHolder extends RecyclerView.ViewHolder {

        CardView categoryCv;
        TextView categoryName;

        ShowUsersViewHolder(View itemView) {
            super(itemView);
            categoryCv = (CardView) itemView.findViewById(R.id.cv_category);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);

        }

    }


    // Update the RecyclerView when an animation happen
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Creating the ViewHolder with the designated item
    @Override
    public ShowUsersRVAdapter.ShowUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ShowUsersRVAdapter.ShowUsersViewHolder(view);
    }


    // Set the settings of CardView
    @Override
    public void onBindViewHolder(ShowUsersRVAdapter.ShowUsersViewHolder holder, int position) {
        holder.categoryName.setText(foundUsers.get(position)); //  Which text to display
    }

    @Override
    public int getItemCount() {
        return foundUsers.size();
    }

}
