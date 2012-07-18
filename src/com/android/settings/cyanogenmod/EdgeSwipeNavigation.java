/*
 * Copyright (C) 2012 The CyanogenMod Project
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

package com.android.settings.cyanogenmod;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

public class EdgeSwipeNavigation extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String EDGE_SWIPE_BOTTOM = "edge_swipe_bottom";
    private static final String EDGE_SWIPE_TOP = "edge_swipe_top";
    private static final String EDGE_SWIPE_LEFT = "edge_swipe_left";
    private static final String EDGE_SWIPE_RIGHT = "edge_swipe_right";
    private static final String EDGE_SWIPE_MOVE = "edge_swipe_move";
    private static final String EDGE_SWIPE_START = "edge_swipe_start";
    private static final String EDGE_SWIPE_DISTANCE = "edge_swipe_distance";

    private ListPreference mEdgeSwipeBottom;
    private ListPreference mEdgeSwipeTop;
    private ListPreference mEdgeSwipeRight;
    private ListPreference mEdgeSwipeLeft;
    private CheckBoxPreference mEdgeSwipeMove;
    private SeekBarPreference mEdgeSwipeStart;
    private SeekBarPreference mEdgeSwipeDistance;

    private ContentResolver mContentResolver;

    private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getActivity().getApplicationContext());

        addPreferencesFromResource(R.xml.edge_swipe);

        PreferenceScreen prefSet = getPreferenceScreen();

        mContentResolver = getActivity().getApplicationContext().getContentResolver();

        mEdgeSwipeBottom = (ListPreference) findPreference(EDGE_SWIPE_BOTTOM);
        mEdgeSwipeBottom.setOnPreferenceChangeListener(this);
        int bottom = Settings.System.getInt(mContentResolver, EDGE_SWIPE_BOTTOM, 0);
        mEdgeSwipeBottom.setValue(String.valueOf(bottom));
        updateSummary(mEdgeSwipeBottom, bottom);

        mEdgeSwipeTop = (ListPreference) findPreference(EDGE_SWIPE_TOP);
        mEdgeSwipeTop.setOnPreferenceChangeListener(this);
        int top = Settings.System.getInt(mContentResolver, EDGE_SWIPE_TOP, 0);
        mEdgeSwipeTop.setValue(String.valueOf(top));
        updateSummary(mEdgeSwipeTop, top);

        mEdgeSwipeRight = (ListPreference) findPreference(EDGE_SWIPE_RIGHT);
        mEdgeSwipeRight.setOnPreferenceChangeListener(this);
        int right = Settings.System.getInt(mContentResolver, EDGE_SWIPE_RIGHT, 0);
        mEdgeSwipeRight.setValue(String.valueOf(right));
        updateSummary(mEdgeSwipeRight, right);

        mEdgeSwipeLeft = (ListPreference) findPreference(EDGE_SWIPE_LEFT);
        mEdgeSwipeLeft.setOnPreferenceChangeListener(this);
        int left = Settings.System.getInt(mContentResolver, EDGE_SWIPE_LEFT, 0);
        mEdgeSwipeLeft.setValue(String.valueOf(left));
        updateSummary(mEdgeSwipeLeft, left);

        mEdgeSwipeMove = (CheckBoxPreference) findPreference(EDGE_SWIPE_MOVE);
        mEdgeSwipeMove.setChecked(Settings.System.getInt(mContentResolver,
                EDGE_SWIPE_MOVE, 0) == 1);

        mEdgeSwipeStart = (SeekBarPreference) prefSet.findPreference(EDGE_SWIPE_START);
        mEdgeSwipeStart.setDefault(Settings.System.getInt(getActivity().getApplicationContext()
                .getContentResolver(), Settings.System.EDGE_SWIPE_START, 35));
        mEdgeSwipeStart.setOnPreferenceChangeListener(this);
        mEdgeSwipeStart.setSummary(String.valueOf(mEdgeSwipeStart.getDefault()));

        mEdgeSwipeDistance = (SeekBarPreference) prefSet.findPreference(EDGE_SWIPE_DISTANCE);
        mEdgeSwipeDistance.setDefault(Settings.System.getInt(getActivity().getApplicationContext()
                .getContentResolver(), Settings.System.EDGE_SWIPE_DISTANCE, 40));
        mEdgeSwipeDistance.setOnPreferenceChangeListener(this);
        mEdgeSwipeDistance.setSummary(String.valueOf(mEdgeSwipeDistance.getDefault()));
    }

    private void updateSummary(ListPreference preference, int value) {
        final CharSequence[] entries = preference.getEntries();
        final CharSequence[] values = preference.getEntryValues();
        int best = 0;
        for (int i = 0; i < values.length; i++) {
            int summaryValue = Integer.parseInt(values[i].toString());
            if (value >= summaryValue) {
                best = i;
            }
        }
        preference.setSummary(entries[best].toString());
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        if (preference == mEdgeSwipeBottom) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putInt(mContentResolver, EDGE_SWIPE_BOTTOM, value);
            updateSummary(mEdgeSwipeBottom, value);
        } else if (preference == mEdgeSwipeTop) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putInt(mContentResolver, EDGE_SWIPE_TOP, value);
            updateSummary(mEdgeSwipeTop, value);
        } else if (preference == mEdgeSwipeRight) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putInt(mContentResolver, EDGE_SWIPE_RIGHT, value);
            updateSummary(mEdgeSwipeRight, value);
        } else if (preference == mEdgeSwipeLeft) {
            int value = Integer.parseInt((String) objValue);
            Settings.System.putInt(mContentResolver, EDGE_SWIPE_LEFT, value);
            updateSummary(mEdgeSwipeLeft, value);
        } else if (preference == mEdgeSwipeStart) {
            int value = (Integer) objValue;
            Settings.System.putInt(mContentResolver, EDGE_SWIPE_START, value);
            mEdgeSwipeStart.setSummary(String.valueOf(value));
        } else if (preference == mEdgeSwipeDistance) {
            int value = (Integer) objValue;
            Settings.System.putInt(mContentResolver, EDGE_SWIPE_DISTANCE, value);
            mEdgeSwipeDistance.setSummary(String.valueOf(value));
        }
        return true;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mEdgeSwipeMove) {
            Settings.System.putInt(getContentResolver(), Settings.System.EDGE_SWIPE_MOVE,
                    mEdgeSwipeMove.isChecked() ? 1 : 0);
        } else {
            // If we didn't handle it, let preferences handle it.
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return true;
    }
}
