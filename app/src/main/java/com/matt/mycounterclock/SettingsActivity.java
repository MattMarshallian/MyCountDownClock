package com.matt.mycounterclock;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Marcin on 12.10.2016.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

    }
}
