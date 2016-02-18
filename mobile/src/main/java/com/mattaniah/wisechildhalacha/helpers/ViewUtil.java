package com.mattaniah.wisechildhalacha.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mattaniah.wisechildhalacha.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Beezy Works Studios on 7/16/2015.
 */
public class ViewUtil {
    Activity context;

    static final String[] typefaces = {
            "TaameyFrank.ttf",
            "David.ttf",
            "Hadasim.ttf",
            "Mekorot-Vilna.ttf"
    };

    public enum Style {
        SEPIA, NIGHT, DAY, AMOLED;

        public Style value = this;

        public String getThemeName() {
            switch (value) {
                case SEPIA:
                    return "Sepia";
                case NIGHT:
                    return "Night";
                case DAY:
                    return "Day";
                case AMOLED:
                    return "AMOLED Night";
            }
            return "I am error";
        }

        public int getBackgroundColor() {
            switch (value) {
                case SEPIA:
                    return R.color.sepia;
                case NIGHT:
                    return R.color.background_material_dark;
                case DAY:
                    return R.color.background_material_light;
                case AMOLED:
                    return android.R.color.black;
            }
            return R.color.background_material_light;

        }

        public int getTextColor() {
            switch (value) {
                case SEPIA:
                    return R.color.abc_primary_text_material_light;
                case NIGHT:
                    return R.color.abc_primary_text_material_dark;
                case DAY:
                    return R.color.abc_primary_text_material_light;
                case AMOLED:
                    return R.color.abc_primary_text_material_dark;
            }
            return R.color.background_material_light;

        }

        public int getSpecialTextColor() {
            switch (value) {
                case SEPIA:
                    return R.color.accent_material_light;
                case NIGHT:
                    return R.color.accent_material_dark;
                case DAY:
                    return R.color.accent_material_light;
                case AMOLED:
                    return R.color.accent_material_dark;
            }
            return R.color.background_material_light;

        }
    }

    public ViewUtil(Activity context) {
        this.context = context;
    }

    public LayoutInflater getLayoutInflater() {
        return context.getLayoutInflater();
    }

    public float getTextSize() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.sizeKey);
        float size;
        try {
            size = Float.parseFloat(sharedPref.getString(key, "25"));
        } catch (NumberFormatException ex) {
            sharedPref.edit().putString(key, "25").apply();
            return getTextSize();
        }
        return size;
    }

    public int getSimanHeaderTextColor() {
        return ContextCompat.getColor(context, isStyleDark() ? R.color.accentBright : R.color.accent);
    }

    public int getSimanSeifCountColor(){
        return ContextCompat.getColor(context, isStyleDark() ? R.color.abc_secondary_text_material_dark:R.color.abc_secondary_text_material_light);
    }

    public int getSeifHeaderTextColor() {
        return ContextCompat.getColor(context, isStyleDark() ? R.color.primaryBright : R.color.primary);
    }

    public int getBookmarkedColor() {
        return ContextCompat.getColor(context, isStyleDark() ? R.color.primaryBright : R.color.primary);
    }

    public Typeface getTypeface() {
        String key = context.getString(R.string.typefaceKey);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return Typeface.createFromAsset(context.getAssets(), sharedPref.getString(key, typefaces[0]));
        } catch (Exception e) {
            sharedPref.edit().putString(key, typefaces[0]).apply();
            return getTypeface();
        }
    }

    public int getTextColor() {
        return ContextCompat.getColor(context, getStyle().getTextColor());
    }

    public int getListViewTextColor(){
        return ContextCompat.getColor(context, isStyleDark()? R.color.listviewtext_dark: R.color.listviewtext_light);
    }

    public int getSpecialTextColor() {
        return ContextCompat.getColor(context, getStyle().getSpecialTextColor());
    }

    public int getBackgroundColor() {
        return ContextCompat.getColor(context, getStyle().getBackgroundColor());
    }

    public int getDividerColor() {
        return ContextCompat.getColor(context, isStyleDark() ? R.color.dividerDark : R.color.dividerLight);
    }

    public ListView getSimpleListView() {
        ListView listView = new ListView(context);
        listView.setDividerHeight(0);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
//        listView.setAdapter(new RighNavDrawerAdapter(context, title, section));
        return listView;
    }



    public Style getStyle() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Style defaultValue = Style.DAY;
        String savedTheme = sharedPref.getString(context.getString(R.string.styleKey), defaultValue.getThemeName());

        for (Style theme : Style.values())
            if (savedTheme.equals(theme.getThemeName()))
                return theme;

        return defaultValue;
    }

    public boolean isStyleDark() {
        Style theme = getStyle();
        return theme == Style.NIGHT || theme == Style.AMOLED;
    }

    public float getLineSpacing() {
        String setting = PreferenceManager.getDefaultSharedPreferences(context).getString(LineSpacingOption.key, LineSpacingOption.SINGLE.getTitle());
        for (LineSpacingOption option : LineSpacingOption.values())
            if (option.getTitle().equals(setting))
                return option.getValue();
        return LineSpacingOption.SINGLE.getValue();
    }

    public enum LineSpacingOption {
        SINGLE, ONE_TWENTY, ONE_FOURTY_FIVE;
        public LineSpacingOption value = this;
        public static final String key = "lineSpaceOption";

        public String getTitle() {
            switch (value) {

                case SINGLE:
                    return "Single";
                case ONE_TWENTY:
                    return "120%";
                case ONE_FOURTY_FIVE:
                    return "145%";
            }
            return "Single";
        }

        public float getValue() {
            switch (value) {

                case SINGLE:
                    return 1f;
                case ONE_TWENTY:
                    return 1.2f;
                case ONE_FOURTY_FIVE:
                    return 1.45f;
            }
            return 1f;
        }
    }

    public int getBackgroundBlackOrWhite() {
        return ContextCompat.getColor(context, isStyleDark() ? R.color.background_floating_material_dark : R.color.background_floating_material_light);
    }

    public TextView getTextView(ViewGroup parent) {
        TextView tv = (TextView) getLayoutInflater().inflate(R.layout.text_component_simple, parent, false);
        tv.setTypeface(getTypeface());
        tv.setLineSpacing(1f, getLineSpacing());
        tv.setTextSize(getTextSize());
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.centeredKey), false))
            tv.setGravity(Gravity.CENTER);
        tv.setTextColor(getTextColor());
        return tv;
    }

    public void setWidthAsDrawer(View drawerView) {
        Resources res = context.getResources();
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float width = metrics.widthPixels;
        int actionBarHeight = (int) res.getDimension(R.dimen.abc_action_bar_default_height_material);
        float drawerWidth = width - actionBarHeight;
        int maxDrawerWidth = (int) res.getDimension(R.dimen.maxDrawerWidth);
        if (drawerWidth > maxDrawerWidth)
            drawerWidth = maxDrawerWidth;
        ViewGroup.LayoutParams params = drawerView.getLayoutParams();
        params.width = (int) drawerWidth;
        drawerView.setLayoutParams(params);
    }

    public View getRowView(String title, Drawable drawable, LayoutInflater inflater, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.drawerbottomitems, parent, false);
        rowView.findViewById(R.id.divider).setVisibility(View.GONE);

        TextView textView = (TextView) rowView.findViewById(R.id.bottomItemTitle);
        textView.setText(title);
        textView.setTextColor(ContextCompat.getColorStateList(context, isStyleDark() ? R.color.listviewtext_dark : R.color.listviewtext_light));

        ImageView imageView = (ImageView) rowView.findViewById(R.id.bottomItemImage);
        imageView.setImageDrawable(drawable);
        imageView.setColorFilter(ContextCompat.getColor(context, isStyleDark() ? R.color.background_material_light : R.color.secondary_text_default_material_light));

        rowView.setBackgroundResource(isStyleDark() ? R.drawable.listview_selector_dark : R.drawable.listview_selector_light);

        return rowView;
    }
    

    public void showDisplayOptionsDialog(final HostActivity callback) {
        new AlertDialog.Builder(context)
                .setView(getDisplayOptionsView())
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.resetColors();
                    }
                })
                .create()
                .show();
    }

    public void showAboutDialog(){
        View aboutDialog = context.getLayoutInflater().inflate(R.layout.about_layout, null);
        TextView versionNumber = (TextView) aboutDialog.findViewById(R.id.versionNumber);
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert info != null;
        versionNumber.setText(String.format("%s%s", context.getString(R.string.versionString), info.versionName));

        new AlertDialog.Builder(context)
                .setView(aboutDialog)
                .create()
                .show();
    }

    public View getDisplayOptionsView() {
        LinearLayout mainLL = new LinearLayout(context);
        mainLL.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) context.getResources().getDimension(R.dimen.activity_horizontal_margin);
        mainLL.setPadding(padding, padding / 2, padding, padding / 2);

        List<String> fontList = new ArrayList<>();
        Collections.addAll(fontList, typefaces);
        mainLL.addView(getSpinnerOption(context.getString(R.string.typefaceKey), fontList, "Typeface"));

        //add theme spinner
        List<String> themeList = new ArrayList<>();
        for (Style theme : ViewUtil.Style.values())
            themeList.add(theme.getThemeName());
        mainLL.addView(getSpinnerOption(context.getString(R.string.styleKey), themeList, "Style"));

        //add line spacing options
        List<String> lineSpacingOptionsList = new ArrayList<>();
        for (LineSpacingOption option : LineSpacingOption.values())
            lineSpacingOptionsList.add(option.getTitle());
        mainLL.addView(getSpinnerOption(LineSpacingOption.key, lineSpacingOptionsList, "Line Spacing"));

        //add font size
        mainLL.addView(getEditTextOption(context.getString(R.string.sizeKey), "30", InputType.TYPE_CLASS_NUMBER, "Text Size"));
        return mainLL;
    }

    public View getSpinnerOption(final String key, final List<String> values, String Title) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View retView = inflater.inflate(R.layout.spinner_option, null);
        Spinner spinner = (Spinner) retView.findViewById(R.id.optionSpinner);
        TextView textView = (TextView) retView.findViewById(R.id.optionTitle);
        textView.setText(Title);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1,
                values);
        spinner.setAdapter(adapter);
        int selection = 0;
        while (!sharedPref.getString(key, values.get(0)).equals(values.get(selection)))
            selection++;
        spinner.setSelection(selection);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPref.edit().putString(key, values.get(position)).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return retView;
    }

    public View getEditTextOption(final String key, String defaultValue, int inputType, String title) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View retView = inflater.inflate(R.layout.edittext_option, null);
        TextView titleTv = (TextView) retView.findViewById(R.id.editTextTitle);
        EditText editText = (EditText) retView.findViewById(R.id.editText);

        titleTv.setText(title);
        editText.setInputType(inputType);
        editText.setText(sharedPref.getString(key, defaultValue));

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    sharedPref.edit().putString(key, s.toString().trim()).apply();
                }
            }
        });

        return retView;
    }

}
