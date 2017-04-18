package com.mattaniah.wisechildhalacha.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.fragments.BookmarkViewerFragment;
import com.mattaniah.wisechildhalacha.helpers.Book;

/**
 * Created by Beezy Works Studios on 2/18/2016.
 */
public class BookmarksActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark_activity_layout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new BookmarkFragmentAdapter());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar!=null;
        toolbar.findViewById(R.id.rightDrawerToggle).setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class BookmarkFragmentAdapter extends FragmentPagerAdapter{
        BookmarkViewerFragment[] fragments = new BookmarkViewerFragment[Book.values().length];

        public BookmarkFragmentAdapter() {
            super(getSupportFragmentManager());
            for (Book book:Book.values())
                fragments[book.ordinal()]=BookmarkViewerFragment.getInstance(book);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Book.values()[position].getName();
        }
    }
}
