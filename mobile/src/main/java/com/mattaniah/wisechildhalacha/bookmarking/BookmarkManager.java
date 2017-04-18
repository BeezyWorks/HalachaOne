package com.mattaniah.wisechildhalacha.bookmarking;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.Sections;
import com.mattaniah.wisechildhalacha.models.SeifModel;
import com.mattaniah.wisechildhalacha.services.BookmarkSaveService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public void addBookmark(SeifModel seifModel){
        addBookmark(seifModel.getSection(), seifModel.getBook(), seifModel.getSiman(), seifModel.getSeif(), seifModel.getBookmark());
    }

    public void removeBookmark(Sections section, Book book, int siman, int seif) {
        Map<Integer, String> simanBookmarks = getBookmarksForSiman(section, book, siman);
        if (simanBookmarks != null && simanBookmarks.containsKey(seif))
            simanBookmarks.remove(seif);
    }

    public void saveBookmarksAsync(Context context){
        context.startService(new Intent(context, BookmarkSaveService.class));
    }

    public void saveBookmarks(Context context) {
        cleanOutUnusedSections();
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(parseBookmarkKey, new Gson().toJson(allBookmarks)).apply();
    }

    private void cleanOutUnusedSections() {
        Map<Sections, Map<Book, Map<Integer, Map<Integer, String>>>> wholeMap = new HashMap<>(allBookmarks);
        Set<Sections> sectionsSet = wholeMap.keySet();
        for (Sections sections : sectionsSet) {
            HashMap<Book, Map<Integer, Map<Integer, String>>> sectionsMap = new HashMap<>(wholeMap.get(sections));
            for (Book book : sectionsMap.keySet()) {
                if (sectionsMap.get(book)!=null) {
                    Map<Integer, Map<Integer, String>> bookMap = new HashMap<>(sectionsMap.get(book));
                    Set<Integer> simanSet = bookMap.keySet();
                    for (Integer siman : simanSet) {
                        Map<Integer, String> seifim = bookMap.get(siman);
                        if (seifim.isEmpty())
                            allBookmarks.get(sections).get(book).remove(siman);
                    }
                }
                if (allBookmarks.get(sections).get(book)==null||allBookmarks.get(sections).get(book).isEmpty())
                    allBookmarks.get(sections).remove(book);
            }
            if (allBookmarks.get(sections).isEmpty())
                allBookmarks.remove(sections);
        }
    }

    public Map<Sections, Map<Book, Map<Integer, Map<Integer, String>>>> getAllBookmarks() {
        return allBookmarks;
    }

    public void remove(Book book) {
        Set<Sections> keySet = allBookmarks.keySet();
        for (Sections section : keySet)
            allBookmarks.get(section).remove(book);
    }

    public void add(Sections sections, Book book,  Map<Integer, Map<Integer, String>> map){
        if (allBookmarks.get(sections).isEmpty())
            allBookmarks.put(sections, new HashMap<Book, Map<Integer, Map<Integer, String>>>());
        allBookmarks.get(sections).put(book, map);
    }
}
