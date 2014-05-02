package com.proje.talkymessage.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.proje.talkymessage.R;

public class GizlilikPreferencesFragment extends PreferenceFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.gizlilik_preferences);
	}

}