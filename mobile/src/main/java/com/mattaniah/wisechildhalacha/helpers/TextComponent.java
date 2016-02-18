package com.mattaniah.wisechildhalacha.helpers;

import java.util.ArrayList;

/**
 * Created by Mattaniah on 7/16/2015.
 */
public class TextComponent {
    CharSequence english;
    CharSequence hebrew;

    public TextComponent(CharSequence hebrew, CharSequence english) {
        this.english = english;
        this.hebrew = hebrew;
    }

    public CharSequence getEnglish() {
        return english;
    }

    public CharSequence getHebrew() {
        return hebrew;
    }

    public static class ComponentList extends ArrayList<TextComponent> {
        public boolean add(CharSequence hebrew, CharSequence english) {
            return super.add(new TextComponent(hebrew, english));
        }
    }
}
