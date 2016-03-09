package com.mattaniah.wisechildhalacha.models;

import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.Sections;

/**
 * Created by Beezy Works Studios on 3/9/2016.
 */
public class SeifModel {
    Sections section;
    Book book;
    int siman;
    int seif;
    String bookmark;



    public SeifModel(Sections section, Book book, int siman, int seif, String bookmark) {
        this.section = section;
        this.book=book;

        this.siman = siman;
        this.seif = seif;
        this.bookmark = bookmark;
    }
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Sections getSection() {
        return section;
    }

    public void setSection(Sections section) {
        this.section = section;
    }

    public int getSiman() {
        return siman;
    }

    public void setSiman(int siman) {
        this.siman = siman;
    }

    public int getSeif() {
        return seif;
    }

    public void setSeif(int seif) {
        this.seif = seif;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }
}
