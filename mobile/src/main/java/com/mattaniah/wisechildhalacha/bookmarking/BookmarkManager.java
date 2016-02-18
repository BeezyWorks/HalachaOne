package com.mattaniah.wisechildhalacha.bookmarking;

import android.util.Log;

import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.Sections;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mattaniah on 1/24/2016.
 */
public class BookmarkManager {
    ParseUser parseUser = ParseUser.getCurrentUser();

    private final String parseBookmarkKey = "bookmarks";

    public static final String sectionKey = "section";
    public static final String simanKey = "siman";
    public static final String seifKey = "seif";
    public static final String bookKey = "book";

    private Map<String, Object> allBookmarks;


    private static final String TAG = "bookmarks_manager";


    private static BookmarkManager ourInstance = new BookmarkManager();

    public static BookmarkManager getInstance() {
        return ourInstance;
    }


    private BookmarkManager() {
        allBookmarks = parseUser.getMap(parseBookmarkKey);
        if (allBookmarks == null) {
            allBookmarks = new HashMap<>();
        }
    }

    public Map<String, Map> getBookmarksForSection(Sections section, Book book) {
        Map<String, Map> sectionBookmarks = (Map<String, Map>) allBookmarks.get(getSectionKey(section));
        if (sectionBookmarks == null) {
            sectionBookmarks = new HashMap<>();
            allBookmarks.put(getSectionKey(section), sectionBookmarks);
        }
        Map<String, Map> bookBookmarks = sectionBookmarks.get(getBookKey(book));
        if (bookBookmarks == null) {
            bookBookmarks = new HashMap<>();
            sectionBookmarks.put(getBookKey(book), bookBookmarks);
        }
        return bookBookmarks;
    }


    public Map<String, String> getBookmarksForSiman(Sections section, Book book, int siman) {
        Map<String, String> retMap = getBookmarksForSection(section, book).get(getSimanKey(siman));
        if (retMap == null) {
            retMap = new HashMap<>();
            getBookmarksForSection(section, book).put(getSimanKey(siman), retMap);
        }
        return retMap;
    }

    public void addBookmark(Sections section, Book book, int siman, int seif, String bookMark) {
        Map<String, String> simanBookmarks = getBookmarksForSiman(section, book, siman);
        simanBookmarks.put(getSeifKey(seif), bookMark);
    }

    public void removeBookmark(Sections section, Book book, int siman, int seif) {
        Map<String, String> simanBookmarks = getBookmarksForSiman(section, book, siman);
        if (simanBookmarks != null && simanBookmarks.containsKey(getSeifKey(seif)))
            simanBookmarks.remove(getSeifKey(seif));
    }

    public static String getSectionKey(Sections section) {
        return sectionKey + section.getName();
    }

    public static String getBookKey(Book book) {
        return bookKey + book.getName();
    }

    public static String getSimanKey(int Siman) {
        return simanKey + Siman;
    }

    public static String getSeifKey(int Seif) {
        return seifKey + Seif;
    }

    public void saveBookmarks() {
        commitToParse();
    }

    private void commitToParse() {
        parseUser.put(parseBookmarkKey, allBookmarks);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Bookmarks Saved Success");
                } else {
                    Log.d(TAG, e.toString());
                }
            }
        });
    }


}
