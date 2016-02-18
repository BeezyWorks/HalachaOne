package com.mattaniah.wisechildhalacha.helpers;

import android.support.v7.widget.Toolbar;

/**
 * Created by Mattaniah on 7/16/2015.
 */
public interface HostActivity {
    public void setDrawerToggle(Toolbar toolbar);

    public void setSection(Sections section);

    public void simanSelected(int relativePosition);

    public void showSeifPicker(int relativeSimanPosition);

    public void resetColors();
}
