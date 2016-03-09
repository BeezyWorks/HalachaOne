package com.mattaniah.wisechildhalacha.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mattaniah.wisechildhalacha.R;
import com.mattaniah.wisechildhalacha.adapters.BookmarkRecyclerAdapter;
import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.Sections;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Beezy Works Studios on 2/18/2016.
 */
public class BookmarkViewerFragment extends Fragment {

    Book book;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    View deleteButton;

    Map<Sections, Map> bookmarkedSections = new HashMap<>();


    public static BookmarkViewerFragment getInstance(Book book) {
        BookmarkViewerFragment instance = new BookmarkViewerFragment();
        instance.setBook(book);
        return instance;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmark_page_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        final BookmarkRecyclerAdapter adapter = new BookmarkRecyclerAdapter(getActivity(), book, recyclerView);
        recyclerView.setAdapter(adapter);
        deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteAll();
            }
        });
        view.findViewById(R.id.emptyView).setVisibility(adapter.getItemCount() == 1 ? View.VISIBLE : View.GONE);
        return view;
    }
}
