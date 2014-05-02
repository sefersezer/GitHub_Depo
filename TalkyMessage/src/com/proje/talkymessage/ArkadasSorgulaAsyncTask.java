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


//arkaplanda arkadas sorgulama yapmak i�in tan�mlanm�� class
public class ArkadasSorgulaAsyncTask extends AsyncTask<Void, String, List<Profil>> {
	// bu sayfa arkada� sorgulanmas� gerekti�inde
	//program� dondurmadan
	//arkaplanda sorgulama i�lemi yapmaktad�r.
	//onPostExecute fonksiyonu ile geri d�nen sonucu ilgili ekrandaki view'a atamaktad�r
	public static final String PROFIL_BULUNAMADI_ERROR = "-1";
	private String sonucKodu;
	
	private Context context;//context, bu class�n kullan�ld��� activity View'�na denk gelmektedir.
	private ProgressDialog progressDialog;
	private ListFragment kisilerListFragment;
	
	public ArkadasSorgulaAsyncTask(Context context, ListFragment kisilerListFragment) {
		super();
		this.context = context;
		this.kisilerListFragment = kisilerListFragment;
	}
	
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(context, "L�tfen Bekleyiniz", "��lem Y�r�t�l�yor", true, true);
	}

	@Override
	protected List<Profil> doInBackground(Void... params) {
		return getArkadasList();
	}

	private List<Profil> getArkadasList() {
		
		String kullaniciAdi = getKullaniciAdi();
		if(TextUtils.isEmpty(kullaniciAdi)) {
			sonucKodu = PROFIL_BULUNAMADI_ERROR;
			return new ArrayList<Profil>();
		}
		
		publishProgress("Arkada� listesi sorgulan�yor");
		
		return NetworkManager.arkadasSorgula(kullaniciAdi);
		
	}
	
	protected void onProgressUpdate(String... progress) {
		progressDialog.setMessage(progress[0]);
	}

	@Override
	protected void onPostExecute(List<Profil> result) {
		
		String sonucMessage = sonucMessage(sonucKodu);
		
		if(!TextUtils.isEmpty(sonucMessage)) {
			Toast.makeText(context, sonucMessage, Toast.LENGTH_LONG).show();
			progressDialog.cancel();
			return;
		}
		
		if(result == null || result.size() == 0) {
    		String mesaj = context.getResources().getString(R.string.toast_arkadas_bulunamadi);
    		Toast.makeText(context, mesaj, Toast.LENGTH_LONG).show();
    		progressDialog.cancel();
			return;
    	}
		
		KisilerListAdapter adapter = new KisilerListAdapter(context, R.layout.kisiler_list_item, result);
		kisilerListFragment.getListView().setAdapter(adapter);
		kisilerListFragment.setListShown(true);
    	progressDialog.cancel();
    	
	}
	
	private String sonucMessage(String sonuc) {
		
		if(PROFIL_BULUNAMADI_ERROR.equals(sonuc))
			return context.getResources().getString(R.string.toast_kayitli_profil_yok);
		return null;
		
	}
	
	private String getKullaniciAdi() {
		
		DatabaseManager manager = new DatabaseManager(context);
		
		Profil profil = manager.profilSorgula(null);
		
		if(profil == null)
			return null;
		
		return profil.getKullaniciAdi();
	}

}
