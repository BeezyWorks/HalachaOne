package com.mattaniah.wisechildhalacha.services;

import android.app.IntentService;
import android.content.Intent;

import com.mattaniah.wisechildhalacha.bookmarking.BookmarkManager;

/**
 * Created by Beezy Works Studios on 3/9/2016.
 */
public class BookmarkSaveService extends IntentService {

    public BookmarkSaveService() {
        super("Bookmark Save Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        BookmarkManager.getInstance().saveBookmarks(this);
    }
}
