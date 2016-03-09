package com.mattaniah.wisechildhalacha.models;

import com.mattaniah.wisechildhalacha.helpers.Book;
import com.mattaniah.wisechildhalacha.helpers.Sections;

import java.util.List;

/**
 * Created by Beezy Works Studios on 3/9/2016.
 */
public class BookModel {
    Sections section;
    Book book;
    List<SimanModel> simanModels;

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

    public List<SimanModel> getSimanModels() {
        return simanModels;
    }

    public void setSimanModels(List<SimanModel> simanModels) {
        this.simanModels = simanModels;
    }

    public BookModel(Sections section, Book book, List<SimanModel> simanModels) {
        this.section = section;
        this.book = book;
        this.simanModels = simanModels;
    }
}
