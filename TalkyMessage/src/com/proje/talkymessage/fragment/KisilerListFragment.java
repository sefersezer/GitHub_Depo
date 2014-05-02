package com.proje.talkymessage.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.proje.talkymessage.HaritaActivity;
import com.proje.talkymessage.R;
import com.proje.talkymessage.ArkadasSorgulaAsyncTask;

public class KisilerListFragment extends ListFragment {
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		ListView arkadasListView = getListView();
		
		arkadasListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView arg0, View view, int position, long id) {
				RelativeLayout layout = (RelativeLayout) view;
				TextView kisilerListKullaniciAdiTextView = (TextView) layout.findViewById(R.id.kisilerListKullaniciAdiTextView);
				
				Intent intent = new Intent(getActivity(), HaritaActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(HaritaActivity.ARKADAS_INTENT_EXTRA, kisilerListKullaniciAdiTextView.getText());
				startActivity(intent);
			}
        });
		
		setListShown(true);
		
		ArkadasSorgulaAsyncTask task = new ArkadasSorgulaAsyncTask(getActivity(), this);
		task.execute();
		
		arkadasListView.setDividerHeight(5);
		
	}
	
}
