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
import com.example.user.mybrotherhood.itemtouchhelper.ItemTouchHelperAdapter;

import java.util.Collections;
import java.util.List;


/**
 * Created by Nico on 13/04/2018.
 */

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.CategoryViewHolder> implements ItemTouchHelperAdapter {

    // Position of the selected item all the time
    private int selectedPos = 0;

    private List<Category> categories;
    private CategoryOnClickListener categoryListener;

    // Constructor
    public CategoryRVAdapter(List<Category> categories, CategoryOnClickListener categoryListener) {
        this.categories = categories;
        this.categoryListener = categoryListener;
    }

    // Interface
    public interface CategoryOnClickListener {
        void passCategoryName(View view, int position);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView categoryCv;
        TextView categoryName;

        CategoryViewHolder(View itemView) {
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

            categoryListener.passCategoryName(view, this.getLayoutPosition());
        }
    }


    // Update the RecyclerView when an animation happen
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Creating the ViewHolder with the designated item
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }


    // Set the settings of CardView
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.categoryName.setText(categories.get(position).categoryName); //  Which text to display

        // This is the background color of an item before any other action was take,
        // the first item will be colored -> color = selected

        holder.categoryCv.setCardBackgroundColor(selectedPos == position ? Color.GREEN : Color.WHITE);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override // ItemTouchHelperAdapter
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) { // Moved Downward
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(categories, i, i + 1);
            }
            //
        } else { // Moved Upward
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(categories, i, i - 1);
            }
        }
        // Updating background of old as well as new item positions
        notifyItemChanged(selectedPos);
        selectedPos = toPosition;
        notifyItemChanged(selectedPos);
        // Notify all the observers about the item which was moved
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override // ItemTouchHelperAdapter
    public void onItemDismiss(int position) {
        notifyItemRemoved(position); // Notify all the observers about the item which was removed
        categories.remove(position); // Remove from the list
    }


}
