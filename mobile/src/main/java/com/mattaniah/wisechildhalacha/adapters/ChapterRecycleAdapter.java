package com.mattaniah.wisechildhalacha.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Mattaniah on 7/16/2015.
 */
public class ChapterRecycleAdapter extends RecyclerView.Adapter {
    Context context;

    final int CHAPTER_HEADER=0;
    final int BODY=1;

    public ChapterRecycleAdapter(Context context){
        this.context=context;
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
