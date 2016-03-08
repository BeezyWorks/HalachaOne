package com.mattaniah.wisechildhalacha.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mattaniah.wisechildhalacha.helpers.Book;

import java.util.Map;

/**
 * Created by Beezy Works Studios on 2/18/2016.
 */
public class BookmarkRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private Book book;

    private static int SECTION_HEADER_TYPE=0;
    private static int SIMAN_HEADER_TYPE=1;
    private static int BOOKMARK_HOLDER=2;

    private Map<String, Object> bookmarks;

    public BookmarkRecyclerAdapter(Context context, Book book) {
        this.context = context;
        this.book = book;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
