package com.proje.talkymessage;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.proje.talkymessage.R;
import com.proje.talkymessage.KisilerListAdapter;
import com.proje.talkymessage.DatabaseManager;
import com.proje.talkymessage.Profil;
//arkaplanda arkadas ekleme yapmak için tanýmlanmýþ class
public class ArkadasEkleAsyncTask extends AsyncTask<String, String, List<Profil>> {
	
	public static final String BASARILI = "1";
	public static final String BASARISIZ = "-1";
	public static final String ARKADAS_BULUNAMADI_ERROR = "-2";
	public static final String ARKADAS_ZATEN_MEVCUT_ERROR = "-3";
	public static final String PROFIL_BULUNAMADI_ERROR = "-4";
	private String sonucKodu;
	
	private Context context;
	private ProgressDialog progressDialog;
	private ListFragment kisilerListFragment;
	
	public ArkadasEkleAsyncTask(Context context, ListFragment kisilerListFragment) {
		super();
		this.context = context;
		this.kisilerListFragment = kisilerListFragment;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(context, "Lütfen Bekleyiniz", "Ýþlem Yürütülüyor", true, true);
	}
	
	@Override
	protected List<Profil> doInBackground(String... params) {
		return arkadasEkle(params[0]);
	}
	
	private List<Profil> arkadasEkle(String arkadasKullaniciAdi) {
		
		String kullaniciAdi = getKullaniciAdi();
		if(TextUtils.isEmpty(kullaniciAdi)) {
			sonucKodu = PROFIL_BULUNAMADI_ERROR;
			return new ArrayList<Profil>();
		}
		
		publishProgress("Arkadaþ ekleniyor");
		
		sonucKodu = NetworkManager.arkadasEkle(kullaniciAdi, arkadasKullaniciAdi);
		
		if(BASARILI.equals(sonucKodu)) {
			publishProgress("Liste güncelleniyor");
			return NetworkManager.arkadasSorgula(kullaniciAdi);
		}
			
		return new ArrayList<Profil>();
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		progressDialog.setMessage(progress[0]);
	}
	
	@Override
	protected void onPostExecute(List<Profil> result) {
		
		String sonucMessage = getArkadasEkleSonucMessage(sonucKodu);
		Toast.makeText(context, sonucMessage, Toast.LENGTH_LONG).show();
		
		if(result == null || result.size() == 0) {
			progressDialog.cancel();
			return;
		}
		
		KisilerListAdapter adapter = new KisilerListAdapter(context, R.layout.kisiler_list_item, result);
		kisilerListFragment.getListView().setAdapter(adapter);
		kisilerListFragment.setListShown(true);
    	progressDialog.cancel();
		
	}

	private String getArkadasEkleSonucMessage(String sonuc) {
		
		if(BASARILI.equals(sonuc))
			return context.getResources().getString(R.string.toast_arkadas_ekle_basarili);
		else if(ARKADAS_BULUNAMADI_ERROR.equals(sonuc))
			return context.getResources().getString(R.string.toast_arkadas_profil_bulunamadi);
		else if(ARKADAS_ZATEN_MEVCUT_ERROR.equals(sonuc))
			return context.getResources().getString(R.string.toast_arkadas_zaten_mevcut);
		else if(PROFIL_BULUNAMADI_ERROR.equals(sonuc))
			return context.getResources().getString(R.string.toast_kayitli_profil_yok);
		else
			return context.getResources().getString(R.string.toast_bilinmeyen_hata);
		
	}
	
	private String getKullaniciAdi() {
		
		DatabaseManager manager = new DatabaseManager(context);
		
		Profil profil = manager.profilSorgula(null);
		
		if(profil == null)
			return null;
		
		return profil.getKullaniciAdi();
	}

}
