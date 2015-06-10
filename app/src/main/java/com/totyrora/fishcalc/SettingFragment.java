package com.totyrora.fishcalc;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by thomasb on 2015-06-06.
 */

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_preference);

    }

}
