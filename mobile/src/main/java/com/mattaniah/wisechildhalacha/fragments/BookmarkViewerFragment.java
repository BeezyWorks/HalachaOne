package com.mattaniah.wisechildhalacha.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mattaniah.wisechildhalacha.bookmarking.BookmarkManager;
import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.Sections;

import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Beezy Works Studios on 2/18/2016.
 */
public class BookmarkViewerFragment extends Fragment {

    Book book;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    Map<Sections, Map> bookmarkedSections = new HashMap<>();

    List<Object> data = new ArrayList<>();

    public static BookmarkViewerFragment getInstance(Book book) {
        BookmarkViewerFragment instance = new BookmarkViewerFragment();
        instance.setBook(book);
        return instance;
    }

    public void setBook(Book book) {
        HebrewDateFormatter hebrewDateFormatter = new HebrewDateFormatter();
        hebrewDateFormatter.setHebrewFormat(true);
        hebrewDateFormatter.setUseGershGershayim(false);
        this.book = book;
        for (Sections sections : Sections.values()) {
            Map<Integer, Map<Integer, String>> markedSimanim = BookmarkManager.getInstance().getBookmarksForSection(sections, book);
            if (!markedSimanim.isEmpty())
                bookmarkedSections.put(sections, markedSimanim);
        }
        for (Sections section : bookmarkedSections.keySet()) {
            data.add(section);
            for (Integer siman : BookmarkManager.getInstance().getBookmarksForSection(section, book).keySet()) {
                Map<Integer, String> bookmarks = BookmarkManager.getInstance().getBookmarksForSiman(section, book, siman);
                if (!bookmarks.isEmpty()) {
                    data.add(hebrewDateFormatter.formatHebrewNumber(siman));
                    data.addAll(bookmarks.keySet());
                }
            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setText(book.getName());

        return textView;
    }
}
