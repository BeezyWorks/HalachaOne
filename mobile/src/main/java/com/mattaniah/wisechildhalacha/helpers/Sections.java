package com.mattaniah.wisechildhalacha.helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mattaniah on 7/16/2015.
 */
public enum Sections {
    MORNING, TZITZIT, TEFILIN, BIRKOS_HASHACHAR, KRIAS_SHMA, TEFILAH, NESIAS_KAPAIM, NEFILAS_APAIM, KRIAS_SEFER_TORAH,
    BEIS_HAKENESES,  WASHIN_HANDS, BETZIAS_HAPAS, DVARIM_HANOHAGIM_BSEUDA, BIKRAS_HAMAZON, BIRKOS_HAPEIRO_VREIACH, SHAAR_MINEI_BROCHOS, TEFILAS_HAMINCHA, KRIAS_SHMA_VARVIS,
    TZNIUS, KLLALIM_VMELACHA_ALEDIE_GOY, SEDER_YOM_SHABBOS, MALACHOS_SHABBOS, HOTZHA_VHACHNASA_MERESHUS_LRESHUS, EIRUBEI_CHATZEIROS, TECHUM_SHABBOS, EIRUVEI_TECHUMIN,
    ROSH_CHODESH, PESACH, YOM_TOV, CHOL_HAMOED,
    TISHA_BAV, TAANIS, ROSH_HASHANA, YOM_KIPPUR, SUCCOS, LULAV, CHANUKA, PURIM;

    public static String key="key:section";
    public static Sections defaultSection=MORNING;

    public String getName() {
        return sectionTitles[this.ordinal()];
    }

    public JSONArray getJSONArray(Context context, Book book){
        try {
            InputStream is = context.getAssets().open(this.name()+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String rawJson = new String(buffer, "UTF-8");
            JSONObject object = new JSONObject(rawJson);
            return object.getJSONArray(book.name());
        } catch (IOException e) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getFirstSiman() {
        try {
            return firstSimanim[this.ordinal()];
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            Log.d(this.getName(), this.getName());
        }
        return 0;
    }

    public int[] getRange() {
        int length = 697 - this.getFirstSiman();
        if (this != PURIM) {
            Sections nextSection = Sections.values()[this.ordinal() + 1];
            length = nextSection.getFirstSiman() - this.getFirstSiman();
        }

        int[] range = new int[length];
        for (int i = 0; i < range.length; i++)
            range[i] = this.getFirstSiman() + i;
        return range;
    }


    static final String[] sectionTitles = {
            "הנהגת האדם בבוקר",
            "ציצית",
            "תפילין",
            "ברכות השחר ושאר ברכות",
            "קריאת שמע",
            "תפילה",
            "נשיאת כפים",
            "נפילת אפים",
            "קריאת ספר תורה",
            "בית הכנסת",
            "נטילת ידים",
            "בציעת הפת",
            "דברים הנוהגים בסעודה",
            "ברכת המזון",
            "ברכת הפירות והריח",
            "שאר מיני ברכות",
            "תפילת המנחה",
            "קריאת שמע ותפילת ערבית",
            "הלכות צניעות",
            "כללים ומלאכה על ידי אינו יהודי",
            "סדר יום השבת",
            "מלאכות שבת",
            "מוצאה והכנסה מרשות לרשות",
            "עירובי חצרות",
            "תחום שבת",
            "עירובי תחומין",
            "ראש חודש",
            "פסח",
            "יום טוב",
            "חול המועד",
            "תשעה באב ושאר תעניות",
            "תענית",
            "ראש השנה",
            "יום הכיפורים",
            "סוכה",
            "לולב",
            "חנוכה",
            "מגילה ופורים"
    };
    final static int[] firstSimanim = {
            1,
            8,
            25,
            46,
            58,
            89,
            128,
            131,
            135,
            150,
            157,
            166,
            169,
            182,
            202,
            218,
            232,
            235,
            240,
            242,
            249,
            301,
            345,
            366,
            396,
            408,
            417,
            429,
            495,
            530,
            549,
            562,
            581,
            604,
            625,
            645,
            670,
            686,
            697
    };
}
