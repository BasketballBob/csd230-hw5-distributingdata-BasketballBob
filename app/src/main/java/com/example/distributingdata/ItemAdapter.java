package com.example.distributingdata;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private ArrayList<GoogleBookModel> bookList;
    private ListItemClickListener listItemClickListener;
    private int itemCount;

    public ItemAdapter (ArrayList<GoogleBookModel> bookList, ListItemClickListener listener) {
        this.bookList = bookList;
        listItemClickListener = listener;

        if (bookList != null) itemCount = bookList.size();
        else itemCount = 0;
    }

    // region ListItemClickListener interface

    public interface ListItemClickListener {
        void onListItemClick(int i, View view);
    }

    // endregion

    // region ItemAdapter methods

    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.model_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ItemViewHolder viewHolder = new ItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int i) {
        holder.book = bookList.get(i);
        holder.bookNameView.setText(bookList.get(i).getTitle());
        holder.bookAuthorView.setText(bookList.get(i).getAuthors());

        if (i % 2 == 0) holder.itemView.setBackgroundColor(Color.rgb(225, 225, 225));
        else holder.itemView.setBackgroundColor(Color.rgb(200, 200, 200));
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public void setBookList(ArrayList<GoogleBookModel> bookList) {
        this.bookList = bookList;
        itemCount = bookList.size();
    }

    // endregion

    // region ItemViewHolder class

    class ItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        GoogleBookModel book;
        TextView bookNameView;
        TextView bookAuthorView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            bookNameView = (TextView) itemView.findViewById(R.id.tv_view_book_title);
            bookAuthorView = (TextView) itemView.findViewById(R.id.tv_view_book_author);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(clickedPosition, view);
        }
    }

    // endregion

}
