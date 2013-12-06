/*
 * Copyright (C) 2013 SlimRoms
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.hellkat;

import android.app.AlertDialog;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;   
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.text.Spannable; 
import android.widget.EditText;  

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.ArrayList;

public class LightsAndWarnings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_LOW_BATTERY_WARNING_POLICY = "pref_low_battery_warning_policy";  
    private static final String KEY_SCREEN_ON_NOTIFICATION_LED = "screen_on_notification_led";  

    private ListPreference mLowBatteryWarning; 
    private CheckBoxPreference mScreenOnNotificationLed;    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lights_and_warnings);

	// Low battery warning
        mLowBatteryWarning = (ListPreference) findPreference(KEY_LOW_BATTERY_WARNING_POLICY);
        int lowBatteryWarning = Settings.System.getInt(getActivity().getContentResolver(),
                                    Settings.System.POWER_UI_LOW_BATTERY_WARNING_POLICY, 0);
        mLowBatteryWarning.setValue(String.valueOf(lowBatteryWarning));
        mLowBatteryWarning.setSummary(mLowBatteryWarning.getEntry());
        mLowBatteryWarning.setOnPreferenceChangeListener(this);

        // Notification light when screen is on
        int statusScreenOnNotificationLed = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_ON_NOTIFICATION_LED, 1);
        mScreenOnNotificationLed = (CheckBoxPreference) findPreference(KEY_SCREEN_ON_NOTIFICATION_LED);
        mScreenOnNotificationLed.setChecked(Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.SCREEN_ON_NOTIFICATION_LED, 0) == 1);    

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mScreenOnNotificationLed) {
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.SCREEN_ON_NOTIFICATION_LED,
                    mScreenOnNotificationLed.isChecked() ? 1 : 0);
         return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
	if (preference == mLowBatteryWarning) {
            int lowBatteryWarning = Integer.valueOf((String) newValue);
            int index = mLowBatteryWarning.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_UI_LOW_BATTERY_WARNING_POLICY,
                    lowBatteryWarning);
            mLowBatteryWarning.setSummary(mLowBatteryWarning.getEntries()[index]);
            return true;
	}  
        return false;
    }
}
