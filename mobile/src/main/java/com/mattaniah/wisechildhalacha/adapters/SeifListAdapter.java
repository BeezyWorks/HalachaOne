package com.mattaniah.wisechildhalacha.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.helpers.ViewUtil;

import java.util.List;

/**
 * Created by Mattaniah on 1/24/2016.
 */
public class SeifListAdapter extends ArrayAdapter {
    ViewUtil viewUtil;
    List<HalachaRecyclerAdapter.SeifHeader> seifHeaders;

    public SeifListAdapter(Activity context, List<HalachaRecyclerAdapter.SeifHeader> seifHeaders) {
        super(context, R.layout.divider);
        this.seifHeaders=seifHeaders;
        this.viewUtil= new ViewUtil(context);
    }

    @Override
    public int getCount() {
        return seifHeaders.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View retView = viewUtil.getRowView(seifHeaders.get(position).getSeifName(), ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark_outline), LayoutInflater.from(getContext()), parent);
        ImageView bookmarkButton = (ImageView) retView.findViewById(R.id.bottomItemImage);
        if (seifHeaders.get(position).isBookmarked){
            bookmarkButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark));
            bookmarkButton.setColorFilter(viewUtil.getBookmarkedColor());
        }
        return retView;
    }
}
