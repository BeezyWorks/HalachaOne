package com.mattaniah.wisechildhalacha.models;

import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.Sections;

import java.util.List;

/**
 * Created by Beezy Works Studios on 3/9/2016.
 */
public class SimanModel {
    Sections section;
    Book book;
    List<SeifModel> seifim;

    public Sections getSection() {
        return section;
    }

    public void setSection(Sections section) {
        this.section = section;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<SeifModel> getSeifim() {
        return seifim;
    }

    public void setSeifim(List<SeifModel> seifim) {
        this.seifim = seifim;
    }

    public SimanModel(Sections section, Book book, List<SeifModel> seifim) {

        this.section = section;
        this.book = book;
        this.seifim = seifim;
    }


}
