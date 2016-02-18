package com.mattaniah.wisechildhalacha.helpers;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**

 * Created by Mattaniah on 7/16/2015.
 */
public class JSONHelper {
    Context context;

    public static final String tehilimJSONResourceName="tehilim.json";
    public static final String hebrewKey="hebrew";
    public static final String englishKey="english";

    JSONObject fullJson;

    public JSONHelper(Context context) {
        this.context = context;
        fullJson=loadJSONFromAsset();
    }

    public JSONObject getFullJson() {
        return fullJson;
    }

    private JSONObject loadJSONFromAsset() {
        try {
            InputStream is = context.getAssets().open(tehilimJSONResourceName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String rawJson = new String(buffer, "UTF-8");
            return new JSONObject(rawJson);
        } catch (IOException e) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
