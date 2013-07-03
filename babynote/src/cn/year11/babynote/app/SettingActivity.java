package cn.year11.babynote.app;


import cn.year11.babynote.R;
import cn.year11.babynote.logic.SettingManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.text.TextUtils;

public class SettingActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager prefMgr = getPreferenceManager();

		addPreferencesFromResource(R.xml.preferences);

		int sdkLevel = 1;
		try {
			sdkLevel = Integer.parseInt(Build.VERSION.SDK);
		} catch (NumberFormatException nfe) {
			// ignore (assume sdkLevel == 1)
		}

		if (sdkLevel < 3) {
			// Older versions don't show the title bar for PreferenceActivity
			PreferenceCategory cat = new PreferenceCategory(this);
			cat.setOrder(0);
			getPreferenceScreen().addPreference(cat);
			cat.setTitle("ÉèÖÃ");
			//cat.addPreference(mStatusPref);
		} else {
			// Newer SDK version show the title bar for PreferenceActivity
			//mStatusPref.setOrder(0);
			//getPreferenceScreen().addPreference(mStatusPref);
		}

        Preference pref = prefMgr.findPreference(SettingManager.LOGIN_USERNAME);
        pref.setOnPreferenceChangeListener(this);
        
        pref = prefMgr.findPreference(SettingManager.LOGIN_PASSWORD);
        pref.setOnPreferenceChangeListener(this);	
    
        pref = prefMgr.findPreference(SettingManager.MAILBOX_PROVIDER);
        pref.setOnPreferenceChangeListener(this);	
	}

    @Override
    protected void onResume() {
        super.onResume();
        updateUsernameLabelFromPref();
        updatePasswordLabelFromPref();
        updateMailboxProviderLabelFromPref();
    }
    
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (SettingManager.MAILBOX_PROVIDER.equals(preference.getKey())) {
            preference.setTitle(newValue.toString());
        }
        else if (SettingManager.LOGIN_USERNAME.equals(preference.getKey())) {
            preference.setTitle(newValue.toString());
        }
        else if (SettingManager.LOGIN_PASSWORD.equals(preference.getKey())) {
        	if (!TextUtils.isEmpty(newValue.toString())) {
                preference.setTitle("******");
        	}
        	else {
                preference.setTitle("ÉèÖÃÃÜÂë");
        	}
        }
        
        return true;
	}

    private void updateMailboxProviderLabelFromPref() {
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
        String mailboxProvider = prefs.getString(SettingManager.MAILBOX_PROVIDER,
                null);
        Preference pref = getPreferenceManager().findPreference(SettingManager.MAILBOX_PROVIDER);
        if (!TextUtils.isEmpty(mailboxProvider))
        	pref.setTitle(mailboxProvider);
    }
    
    private void updateUsernameLabelFromPref() {
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
        String username = prefs.getString(SettingManager.LOGIN_USERNAME,
                null);
        Preference pref = getPreferenceManager().findPreference(SettingManager.LOGIN_USERNAME);
        if (!TextUtils.isEmpty(username))
        pref.setTitle(username);
    }
    
    private void updatePasswordLabelFromPref() {
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
        String password = prefs.getString(SettingManager.LOGIN_PASSWORD,
                null);
        Preference pref = getPreferenceManager().findPreference(SettingManager.LOGIN_PASSWORD);
        if (TextUtils.isEmpty(password)) {
        	pref.setTitle("ÉèÖÃÃÜÂë");
        }
        else {
        	pref.setTitle("******");
        }
    }
}
