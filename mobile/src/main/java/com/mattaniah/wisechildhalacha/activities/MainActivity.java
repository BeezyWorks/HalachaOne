package com.mattaniah.wisechildhalacha.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.adapters.HalachaRecyclerAdapter;
import com.mattaniah.wisechildhalacha.adapters.SeifListAdapter;
import com.mattaniah.wisechildhalacha.bookmarking.BookmarkManager;
import com.mattaniah.wisechildhalacha.fragments.NavigationDrawerFragment;
import com.mattaniah.wisechildhalacha.fragments.SubnavFragment;
import com.mattaniah.wisechildhalacha.goaltracking.GoalNotifications;
import com.mattaniah.wisechildhalacha.goaltracking.TimeTracker;
import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.CommonIntents;
import com.mattaniah.wisechildhalacha.helpers.HostActivity;
import com.mattaniah.wisechildhalacha.helpers.MyGestureDetector;
import com.mattaniah.wisechildhalacha.helpers.Sections;
import com.mattaniah.wisechildhalacha.helpers.SettingsUtil;
import com.mattaniah.wisechildhalacha.helpers.ViewUtil;
import com.pushbots.push.Pushbots;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements HostActivity, MyGestureDetector.GestureCallbackReciever,
        TabLayout.OnTabSelectedListener, Toolbar.OnMenuItemClickListener {
    DrawerLayout navigationDrawerLayout;
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout subNavigationDrawerLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    FrameLayout frameLayout;

    NavigationDrawerFragment navigationFragment;
    SubnavFragment subnavigationFragment;

    Book currentBook = Book.SHULCHAN_ARUCH;
    BookHolder[] bookHolders;

    ViewUtil viewUtil;
    SettingsUtil settingsUtil;

    private long timeStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Pushbots.sharedInstance().init(this);
        setContentView(R.layout.activity_main);
        settingsUtil = new SettingsUtil(this);
        viewUtil = new ViewUtil(this);
        currentBook = Book.getDefaultBook(settingsUtil);

        navigationDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        subNavigationDrawerLayout = (DrawerLayout) findViewById(R.id.fragment_drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        frameLayout = (FrameLayout) findViewById(R.id.halacha_frame);
        setDrawerToggle(toolbar);
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(this);

        frameLayout.setBackgroundColor(viewUtil.getBackgroundColor());

        navigationFragment = new NavigationDrawerFragment();
        subnavigationFragment = new SubnavFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.navigation_frame, navigationFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.subnavigation_drawer, subnavigationFragment).commit();

        viewUtil.setWidthAsDrawer(findViewById(R.id.navigation_frame));

        bookHolders = new BookHolder[Book.values().length];
        setSection(settingsUtil.getSavedSection());
        for (BookHolder bookHolder : bookHolders)
            bookHolder.scrollToSaved();
    }

    @Override
    public void resetColors() {
        frameLayout.removeAllViews();
        for (BookHolder holder : bookHolders) {
            holder.reasignAdapter();
        }
        frameLayout.setBackgroundColor(viewUtil.getBackgroundColor());

        subnavigationFragment.resetColors();
        frameLayout.addView(bookHolders[currentBook.ordinal()].getRecyclerView());
    }

    public void setDrawerToggle(Toolbar toolbar) {
        drawerToggle = new ActionBarDrawerToggle(this, navigationDrawerLayout, toolbar,
                R.string.abc_action_bar_home_description, R.string.abc_action_bar_up_description) {
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };


        navigationDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        final View rightDrawerToggle = toolbar.findViewById(R.id.rightDrawerToggle);
        rightDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subNavigationDrawerLayout.isDrawerOpen(Gravity.RIGHT))
                    subNavigationDrawerLayout.closeDrawer(Gravity.RIGHT);
                else
                    subNavigationDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        subNavigationDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                rightDrawerToggle.setRotation(slideOffset * 180 + 180);
            }
        });
    }

    @Override
    public void setSection(Sections section) {
        navigationDrawerLayout.closeDrawer(Gravity.LEFT);
        toolbar.setTitle(section.getName());
        tabLayout.removeAllTabs();
        for (Book book : Book.values()) {
            bookHolders[book.ordinal()] = new BookHolder(book, section);
            TabLayout.Tab newTab = tabLayout.newTab().setText(book.getName()).setTag(book);
            tabLayout.addTab(newTab);
        }
        tabLayout.setOnTabSelectedListener(this);
        //noinspection ConstantConditions
        tabLayout.getTabAt(currentBook.ordinal()).select();
        subnavigationFragment.setSection(section);
        settingsUtil.saveDefaultSection(section);
    }

    @Override
    public void simanSelected(int relativePosition) {
        subNavigationDrawerLayout.closeDrawer(Gravity.RIGHT);
        for (BookHolder bookHolder : bookHolders) {
            int scrollToPosition = bookHolder.adapter.getPositionForSiman(relativePosition);
            bookHolder.linearLayoutManager.scrollToPositionWithOffset(scrollToPosition, 0);
        }
    }

    @Override
    public void showSeifPicker(int relativeSimanPosition) {
        bookHolders[currentBook.ordinal()].showSeifPicker(relativeSimanPosition);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Book selectedBook = ((Book) tab.getTag());
        this.currentBook = selectedBook;
        frameLayout.removeAllViews();
        if (selectedBook != null) {
            frameLayout.addView(bookHolders[selectedBook.ordinal()].getRecyclerView());
            selectedBook.saveAsDefault(settingsUtil);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        onTabSelected(tab);
    }

    @Override
    public void swipeLeftToRight() {
        if (currentBook.ordinal() > 0) {
            currentBook = Book.values()[currentBook.ordinal() - 1];
            //noinspection ConstantConditions
            tabLayout.getTabAt(currentBook.ordinal()).select();
        }
    }

    @Override
    public void swipeRightToLeft() {
        if (currentBook.ordinal() < Book.values().length) {
            currentBook = Book.values()[currentBook.ordinal() + 1];
            //noinspection ConstantConditions
            tabLayout.getTabAt(currentBook.ordinal()).select();
        }
    }

    @Override
    public void doubleTap() {

    }

    @Override
    public void longPress() {

    }

    @Override
    public void zoomIn() {

    }

    @Override
    public void zoomOut() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        timeStarted = Calendar.getInstance().getTimeInMillis();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(GoalNotifications.goalNotiId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (BookHolder bookHolder : bookHolders)
            bookHolder.saveCurrentScrollPosition();

        TimeTracker timeTracker = new TimeTracker(this);
        timeTracker.addTime(timeStarted);

        if (timeTracker.goalMetToday()){
            new GoalNotifications(this).setRelaventNotification();
        }

        BookmarkManager.getInstance().saveBookmarks(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.displayOptions:
                viewUtil.showDisplayOptionsDialog(this);
                return true;
            case R.id.stats:
                startActivity(new Intent(this, StatsActivity.class));
                return true;
            case R.id.aboutOption:
                viewUtil.showAboutDialog();
                return true;
            case R.id.contactUs:
                CommonIntents commonIntents = new CommonIntents(this);
                commonIntents.safeLaunchIntent(commonIntents.contactUsIntent(), "Select Email");
                return true;

            case R.id.bookmarks:
                startActivity(new Intent(this, BookmarksActivity.class));
                return true;
        }
        return false;
    }

    private class BookHolder {
        private Book book;
        private Sections section;

        private LinearLayoutManager linearLayoutManager;
        private HalachaRecyclerAdapter adapter;
        private RecyclerView recyclerView;

        String scrollKey;
        int currentRelativeSiman = 0;

        public BookHolder(Book book, final Sections section) {
            this.book = book;
            this.section = section;
            this.adapter = new HalachaRecyclerAdapter(MainActivity.this, section, book);
            scrollKey = "key:scroll" + book.getName();
            this.linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView = new RecyclerView(MainActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setOnTouchListener(MyGestureDetector.getOnTouchListener(MainActivity.this, MainActivity.this));
            final HebrewDateFormatter hFat = new HebrewDateFormatter();
            hFat.setUseGershGershayim(false);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    for (int i = 0; i < adapter.getNumberOfSimanim(); i++) {
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() >= adapter.getPositionForSiman(i)) {
                            toolbar.setSubtitle("סימן " + hFat.formatHebrewNumber(section.getFirstSiman() + i));
                            currentRelativeSiman = i;
                            subnavigationFragment.setSelectedSiman(i);
                        }
                    }
                }
            });
        }

        public void reasignAdapter() {
            int current = linearLayoutManager.findFirstVisibleItemPosition();
            adapter = new HalachaRecyclerAdapter(MainActivity.this, section, book);
            recyclerView.setAdapter(adapter);
            linearLayoutManager.scrollToPositionWithOffset(current, 0);
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }


        public void saveCurrentScrollPosition() {
            int savePosition = linearLayoutManager.findFirstVisibleItemPosition();
            settingsUtil.getSharedPreferences().edit().putInt(scrollKey, savePosition).apply();
        }

        public void scrollToSaved() {
            int savedScroll = settingsUtil.getSharedPreferences().getInt(scrollKey, 0);
            linearLayoutManager.scrollToPositionWithOffset(savedScroll, 0);
        }

        public void scrollToSeif(int relativeSiman, int seif) {
            linearLayoutManager.scrollToPositionWithOffset(adapter.getPositionForSeif(relativeSiman, seif), 0);
        }

        public void showSeifPicker(final int relativeSiman) {
            ListView listView = viewUtil.getSimpleListView();

            listView.setAdapter(new SeifListAdapter(MainActivity.this, adapter.getSeifimForSiman(relativeSiman)));
            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setView(listView)
                    .setNegativeButton("Dismiss", null)
                    .create();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    scrollToSeif(relativeSiman, position);
                    alertDialog.dismiss();
                    subNavigationDrawerLayout.closeDrawer(Gravity.RIGHT);
                }
            });

            alertDialog.show();
        }

    }
}
