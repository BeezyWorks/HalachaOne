package com.mattaniah.wisechildhalacha.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.Sections;

/**
 * Created by Mattaniah on 7/20/2015.
 */
public class HalachaPageAdapter extends PagerAdapter {
    Sections section;
    Context context;

    public HalachaPageAdapter(Context context, Sections section) {
        this.section=section;
        this.context=context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return Book.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Book.values()[position].getName();
    }
}
