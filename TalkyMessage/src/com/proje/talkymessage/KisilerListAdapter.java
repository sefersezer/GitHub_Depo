package com.proje.talkymessage;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.proje.talkymessage.R;
import com.proje.talkymessage.Profil;

public class KisilerListAdapter extends ArrayAdapter<Profil> {

	private List<Profil> arkadasList;//arkadaþ listesini nesne olarak getiren dizi
	private Context context;
	private int layoutResourceId;
	
	public KisilerListAdapter(Context context, int layoutResourceId, List<Profil> arkadasList) {
		//arkadaþ listesi ile liste görünümünü baðlamayý yarayan yapýcý fonksiyon
		super(context, layoutResourceId, arkadasList);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.arkadasList = arkadasList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		
		if(view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(layoutResourceId, null);
		}
		
		Profil profil = arkadasList.get(position);
		
		TextView kisilerListKullaniciAdiTextView = (TextView) view.findViewById(R.id.kisilerListKullaniciAdiTextView);
		TextView kisilerListAdSoyadTextView = (TextView) view.findViewById(R.id.kisilerListAdSoyadTextView);
		
		kisilerListKullaniciAdiTextView.setText(profil.getKullaniciAdi());
		kisilerListAdSoyadTextView.setText(profil.getAd() + " " + profil.getSoyad());

		return view;
	}

}
