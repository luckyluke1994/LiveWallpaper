package com.example.maidaidien.matrixeffectcanvas;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.enrico.colorpicker.colorDialog;

/**
 * Created by mai.dai.dien on 21/02/2017.
 */

public class SettingsActivity extends AppCompatActivity implements colorDialog.ColorSelectedListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the Preference default value
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public void onColorSelection(DialogFragment dialogFragment, @ColorInt int selectedColor) {
        int tag;

        // get tag number from fragment
        tag = Integer.valueOf(dialogFragment.getTag());

        switch (tag) {
            case 1:
                //Set the picker dialog's color
                colorDialog.setPickerColor(SettingsActivity.this, 1, selectedColor);

                //set custom preference summary
                colorDialog.setColorPreferenceSummary(SettingsFragment.background_color, selectedColor, SettingsActivity.this, getResources());
                break;
            case 2:
                //Set the picker dialog's color
                colorDialog.setPickerColor(SettingsActivity.this, 2, selectedColor);

                //set custom preference summary
                colorDialog.setColorPreferenceSummary(SettingsFragment.text_color, selectedColor, SettingsActivity.this, getResources());
                break;
        }
    }

    public static class SettingsFragment extends PreferenceFragment {
        static Preference background_color, text_color;
        Context mContext;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mContext = context;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            // find the preference
            background_color = findPreference("background_color");
            text_color = findPreference("text_color");

            // get preferences colors
            int color = colorDialog.getPickerColor(getActivity(), 1);

            // set preferences colors
            colorDialog.setColorPreferenceSummary(background_color, color, getActivity(), getResources());

            // set the event listner for the color
            background_color.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    colorDialog.showColorPicker(appCompatActivity, 1);
                    return false;
                }
            });

            // pref text_color
            // get preferences colors
            int color2 = colorDialog.getPickerColor(getActivity(), 2);

            // set preferences colors
            colorDialog.setColorPreferenceSummary(text_color, color2, getActivity(), getResources());

            // set the event listner for the color
            text_color.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    colorDialog.showColorPicker(appCompatActivity, 2);
                    return false;
                }
            });
        }
    }
}
