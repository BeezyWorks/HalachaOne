package com.mattaniah.wisechildhalacha.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mattaniah.wisechildhalacha.helpers.HostActivity;
import com.mattaniah.wisechildhalacha.helpers.Sections;
import com.mattaniah.wisechildhalacha.helpers.SettingsUtil;
import com.mattaniah.wisechildhalacha.helpers.ViewUtil;
import com.mattaniah.wisechildhalacha.R;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;

/**
 * Created by Mattaniah on 7/21/2015.
 * Controls the listview and linear layout that define the right navigation drawer
 */
public class SubnavFragment extends Fragment {
    HostActivity hostActivity;
    ViewUtil viewUtil;

    ListView listView;
    LinearLayout quickNavContainer;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        viewUtil = new ViewUtil((Activity) activity);
        this.hostActivity = (HostActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subnav_layout, container, false);

        listView = (ListView) view.findViewById(R.id.subnav_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isAdded())
                    return;
                hostActivity.simanSelected(position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isAdded())
                    return false;
                hostActivity.showSeifPicker(position);
                return true;
            }
        });

        quickNavContainer = (LinearLayout) view.findViewById(R.id.quicknav_container);

        viewUtil.setWidthAsDrawer(view);
        listView.setBackgroundColor(viewUtil.getBackgroundBlackOrWhite());
        setSection(new SettingsUtil(getActivity()).getSavedSection());
        addViewsToQuickNavContainer();
        return view;
    }

    public void resetColors() {
        listView.setBackgroundColor(viewUtil.getBackgroundBlackOrWhite());
        ((Adapter) listView.getAdapter()).notifyDataSetChanged();
        addViewsToQuickNavContainer();
    }

    private void addViewsToQuickNavContainer() {
        quickNavContainer.removeAllViews();
        quickNavContainer.setBackgroundColor(ContextCompat.getColor(getActivity(), viewUtil.isStyleDark() ? R.color.background_material_dark : android.R.color.white));

//        View displayOptions = viewUtil.getRowView("Display Options", ContextCompat.getDrawable( getActivity(), R.drawable.ic_filter_variant), getActivity().getLayoutInflater(), quickNavContainer);
//        displayOptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewUtil.showDisplayOptionsDialog(hostActivity);
//            }
//        });
//
//        quickNavContainer.addView(displayOptions);
    }

    public void setSection(Sections section) {
        if (!isAdded())
            return;
        listView.setAdapter(new Adapter(getActivity(), section));
    }

    public void setSelectedSiman(int relativeSimanNumber){
        if (!isAdded())
            return;
        listView.setSelection(relativeSimanNumber);
        listView.setItemChecked(relativeSimanNumber, true);
    }

    private class Adapter extends ArrayAdapter {
        Sections section;
        HebrewDateFormatter hFat = new HebrewDateFormatter();

        public Adapter(Context context, Sections section) {
            super(context, R.layout.subnavigation_item);
            this.section = section;
            hFat.setUseGershGershayim(false);
        }


        @Override
        public int getCount() {
            return section.getRange().length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = convertView != null ? (TextView) convertView : (TextView) getActivity().getLayoutInflater().inflate(R.layout.subnavigation_item, parent, false);
            textView.setText("סימן " + hFat.formatHebrewNumber(section.getRange()[position]));
            textView.setTextColor(viewUtil.getListViewTextColor());
            return textView;
        }
    }
}
