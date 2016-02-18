package com.mattaniah.wisechildhalacha.helpers;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Mattaniah on 7/19/2015.
 */
public class ParseEverythings {
    Context context;

    JSONArray shulchaAruch;
    JSONArray mishnaBrura;
    JSONArray arochHaShulchan;

    String aruchHashulchanResource="aruchhashulchan.json";
    String mishnaBrurahREsource="mishna_berurah.json";
    String shulchanAruchResource="shulcha_aruch.json";


    public ParseEverythings(Context context) throws IOException, JSONException {
        this.context = context;
        arochHaShulchan = loadJSONFromAsset(aruchHashulchanResource);
        mishnaBrura=loadJSONFromAsset(mishnaBrurahREsource);
        shulchaAruch=loadJSONFromAsset(shulchanAruchResource);

        File root = new File(Environment.getExternalStorageDirectory(), "HALACHA JSONS");
        if (!root.exists()) {
            root.mkdirs();
        }

        for (Sections section: Sections.values()){
            JSONObject sectionObject=new JSONObject();
            for (Book book : Book.values()){
                JSONArray readingFrom=getRightJSONArray(book);
                JSONArray sectionArray=new JSONArray();
                for (int i: section.getRange()){
                    sectionArray.put(readingFrom.get(i-1));
                }
                sectionObject.put(book.name(), sectionArray);
            }

            File file = new File(root, section.name()+".json");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(sectionObject.toString().getBytes());
        }
        Toast.makeText(context, "All Done", Toast.LENGTH_LONG).show();
    }

    public class HalachaSection {
        int firstSiman;
        List<JSONArray> simanim;

        public HalachaSection(int firstSiman, List<JSONArray> simanim) {
            this.firstSiman = firstSiman;
            this.simanim = simanim;
        }
    }

    private JSONArray getRightJSONArray(Book book){
        switch (book){

            case MISHNA_BERURA:
                return mishnaBrura;
            case AROCH_HASHULCHA:
                return arochHaShulchan;
            case SHULCHAN_ARUCH:
                return shulchaAruch;
        }
        return null;
    }


    private JSONArray loadJSONFromAsset(String resource) {
        try {
            InputStream is = context.getAssets().open(resource);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String rawJson = new String(buffer, "UTF-8");
            JSONObject object = new JSONObject(rawJson);
            return object.getJSONArray("text");
        } catch (IOException e) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
