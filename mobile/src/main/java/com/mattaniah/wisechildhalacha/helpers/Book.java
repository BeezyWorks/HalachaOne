package com.mattaniah.wisechildhalacha.helpers;

/**
 * Created by Mattaniah on 7/19/2015.
 */
public enum Book {
    AROCH_HASHULCHA, MISHNA_BERURA,SHULCHAN_ARUCH,;

    final static String saveKey="key:saveAsDefaultBook";

    public String getName() {
        switch (this) {

            case SHULCHAN_ARUCH:
                return "שולחן ערוך";
            case AROCH_HASHULCHA:
                return "ערוך השולחן";
            case MISHNA_BERURA:
                return "משנה ברורה";
        }
        return SHULCHAN_ARUCH.getName();
    }

    public void saveAsDefault(SettingsUtil settingsUtil){
        settingsUtil.getSharedPreferences().edit().putString(saveKey, this.getName()).apply();
    }

    public static Book getDefaultBook(SettingsUtil settingsUtil){
        String saved = settingsUtil.getSharedPreferences().getString(saveKey, SHULCHAN_ARUCH.getName());
        for (Book book : Book.values())
            if (book.getName().equals(saved))
                return book;
        return SHULCHAN_ARUCH;
    }

}
