package me.richard.note.fragment.setting;

import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.StringRes;

import me.richard.note.PalmApp;
import me.richard.note.listener.OnSettingsChangedListener;
import me.richard.note.listener.SettingChangeType;

/**
 * Created by shouh on 2018/4/4.*/
public abstract class BaseFragment extends PreferenceFragment {

    public Preference findPreference(@StringRes int keyRes) {
        return super.findPreference(PalmApp.getStringCompact(keyRes));
    }

    protected void notifyDashboardChanged(SettingChangeType changeType) {
        if (getActivity() != null && getActivity() instanceof OnSettingsChangedListener) {
            ((OnSettingsChangedListener) getActivity()).onSettingChanged(changeType);
        }
    }
}
