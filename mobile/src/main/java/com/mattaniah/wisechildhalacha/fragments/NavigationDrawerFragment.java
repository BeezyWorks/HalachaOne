package com.mattaniah.wisechildhalacha.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.helpers.HostActivity;
import com.mattaniah.wisechildhalacha.helpers.Sections;
import com.mattaniah.wisechildhalacha.helpers.ViewUtil;

/**
 * Created by Mattaniah on 7/16/2015.
 */
public class NavigationDrawerFragment extends Fragment {
    ListView list;
    HostActivity hostActivity;


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.hostActivity = (HostActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        list = new ListView(getActivity());

        View headerView = inflater.inflate(R.layout.navigation_header, list, false);
        ImageView headerImage = (ImageView) headerView.findViewById(R.id.header_image);
        headerImage.setColorFilter(ContextCompat.getColor(getActivity(), R.color.primary), PorterDuff.Mode.MULTIPLY);

        list.addHeaderView(headerView);
        list.setAdapter(new Adapter());
        list.setDividerHeight(0);
        list.setBackgroundColor(new ViewUtil(getActivity()).getBackgroundBlackOrWhite());
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hostActivity.setSection(Sections.values()[position - 1]);
                list.setItemChecked(position, true);
            }
        });

        return list;
    }

    public void resetColor(){
        ViewUtil viewUtil = new ViewUtil(getActivity());
        list.setBackgroundColor(viewUtil.getBackgroundBlackOrWhite());
        list.setAdapter(new Adapter());
    }

    class Adapter extends ArrayAdapter {

        public Adapter() {
            super(getActivity(), R.layout.navigation_item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") View view = getActivity().getLayoutInflater().inflate(R.layout.navigation_item, parent, false);
            TextView textView = (TextView) view.findViewById(R.id.text_view);
            textView.setText(Sections.values()[position].getName());
            textView.setTextColor(new ViewUtil(getActivity()).getListViewTextColor());
            return view;
        }

        @Override
        public int getCount() {
            return Sections.values().length;
        }
    }
}
