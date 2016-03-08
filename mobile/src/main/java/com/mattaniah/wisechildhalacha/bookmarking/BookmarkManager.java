package com.mattaniah.wisechildhalacha.bookmarking;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.Sections;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mattaniah on 1/24/2016.
 */
public class BookmarkManager {

    private final String parseBookmarkKey = "bookmarks";

    private Map<Sections, Map<Book, Map<Integer, Map<Integer, String>>>> allBookmarks;

    private static BookmarkManager instance = new BookmarkManager();

    public static BookmarkManager getInstance() {
        return instance;
    }

    public void initialize(Context context) {
        allBookmarks = new Gson().fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString(parseBookmarkKey, null), new TypeToken<HashMap<Sections, Map<Book, Map<Integer, Map<Integer, String>>>>>() {
        }.getType());
        if (allBookmarks == null) {
            allBookmarks = new HashMap<>();
        }
    }

    public Map<Integer, Map<Integer, String>> getBookmarksForSection(Sections section, Book book) {
        Map<Book, Map<Integer, Map<Integer, String>>> sectionBookmarks = allBookmarks.get(section);
        if (sectionBookmarks == null) {
            sectionBookmarks = new HashMap<>();
            allBookmarks.put(section, sectionBookmarks);
        }
        Map<Integer, Map<Integer, String>> bookBookmarks = sectionBookmarks.get(book);
        if (bookBookmarks == null) {
            bookBookmarks = new HashMap<>();
            sectionBookmarks.put(book, bookBookmarks);
        }
        return bookBookmarks;
    }


    public Map<Integer, String> getBookmarksForSiman(Sections section, Book book, int siman) {
        Map<Integer, String> retMap = getBookmarksForSection(section, book).get(siman);
        if (retMap == null) {
            retMap = new HashMap<>();
            getBookmarksForSection(section, book).put(siman, retMap);
        }
        return retMap;
    }

    public boolean isSeifBookmarked(Sections sections, Book book, int siman, int seif) {
        Map<Integer, String> simanBookmarks = getBookmarksForSiman(sections, book, siman);
        return simanBookmarks.containsKey(seif);
    }

    public void addBookmark(Sections section, Book book, int siman, int seif, String bookMark) {
        Map<Integer, String> simanBookmarks = getBookmarksForSiman(section, book, siman);
        simanBookmarks.put(seif, bookMark);
    }

    public void removeBookmark(Sections section, Book book, int siman, int seif) {
        Map<Integer, String> simanBookmarks = getBookmarksForSiman(section, book, siman);
        if (simanBookmarks != null && simanBookmarks.containsKey(seif))
            simanBookmarks.remove(seif);
    }

    public void saveBookmarks(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(parseBookmarkKey, new Gson().toJson(allBookmarks)).apply();
    }


}
