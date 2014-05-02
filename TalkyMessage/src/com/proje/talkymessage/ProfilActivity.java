package com.proje.talkymessage;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.proje.talkymessage.ProfilKaydetAsyncTask;
import com.proje.talkymessage.DatabaseManager;
import com.proje.talkymessage.Profil;

public class ProfilActivity extends BaseActivity {
	
	private DatabaseManager manager;
	private static final int CAMERA_REQUEST = 1;
	
	private ImageButton profilImageButton;
	private Bitmap profilResmi;
	private EditText txtKullaniciAdi;
	private EditText txtAd;
	private EditText txtSoyad;
	private EditText txtTel;
	private EditText txtEmail;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kimlik);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        ekranKontrolleriniOlustur();
    }
	
	private void ekranKontrolleriniOlustur() {
		
		manager = new DatabaseManager(this);
		
		final Profil profil = manager.profilSorgula(null);
		
		profilImageButton = (ImageButton) findViewById(R.id.profilImageButton);
		txtKullaniciAdi = (EditText) findViewById(R.id.txtKullaniciAdi);
		txtAd = (EditText) findViewById(R.id.txtAd);
		txtSoyad = (EditText) findViewById(R.id.txtSoyad);
		txtTel = (EditText) findViewById(R.id.txtTel);
		txtEmail = (EditText) findViewById(R.id.txtMail);
		
		ekranGuncelle(profil);
		
		Button kaydetButton = (Button) findViewById(R.id.btnKaydet);
        kaydetButton.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			Profil kaydedilecekProfil = ekranDegerleriniOku();
    			
    			int sonuc = manager.profilKaydetGuncelle(kaydedilecekProfil);
    			if(internetkontrol()==true)
    			{
    				Log.d("Olumlu Durum","Ýnternet baðlantýsýnda hata yok.");
	    			if(DatabaseManager.BASARILI == sonuc) {
	    				ProfilKaydetAsyncTask task = new ProfilKaydetAsyncTask(ProfilActivity.this);
	    				task.execute(kaydedilecekProfil);
	    			} else {
	    				String profilKaydetSonucMessage = sonucMesaj(sonuc);
	    				Toast.makeText(getApplicationContext(), profilKaydetSonucMessage, Toast.LENGTH_LONG).show();
	    			}
    			}
    			else
    			{
    				Log.d("Hata","Ýnternet baðlantýsýnda hata oluþtu.");
    			}
    		}
    	});
        
        profilImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
		
	}
	
	private boolean internetkontrol(){
		ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		  if (i == null)
		    return false;
		  if (!i.isConnected())
		    return false;
		  if (!i.isAvailable())
		    return false;
		  return true;
	}
	
	private void ekranGuncelle(Profil profil) {
		
		if(profil == null)
			return;
		
		txtKullaniciAdi.setText(profil.getKullaniciAdi());
		txtKullaniciAdi.setEnabled(false);
		
		txtAd.setText(profil.getAd());
		txtSoyad.setText(profil.getSoyad());
		txtTel.setText(profil.getTelefon());
		txtEmail.setText(profil.getEmail());
		
		if(profil.getProfilPhoto() != null)
			profilImageButton.setImageBitmap(profil.getProfilPhoto());
		
	}
	
	private Profil ekranDegerleriniOku() {
		
		Profil profil = new Profil();
		
		if(txtKullaniciAdi.getText() != null)
			profil.setKullaniciAdi(txtKullaniciAdi.getText().toString());
		
		if(txtAd.getText() != null)
			profil.setAd(txtAd.getText().toString());
		
		if(txtSoyad.getText() != null)
			profil.setSoyad(txtSoyad.getText().toString());
		
		if(txtTel.getText() != null)
			profil.setTelefon(txtTel.getText().toString());
		
		if(txtEmail.getText() != null)
			profil.setEmail(txtEmail.getText().toString());
		
		if(profilResmi != null)
			profil.setProfilPhoto(profilResmi);
		
		return profil;
	}
	
	private String sonucMesaj(int sonuc) {
		
		if(DatabaseManager.BASARILI == sonuc)
			return getResources().getString(R.string.toast_profil_kaydet_basarili);
		
		if(DatabaseManager.PROFIL_VALIDASYON_HATASI == sonuc)
			return getResources().getString(R.string.toast_profil_validasyon_hatasi);
		
		return getResources().getString(R.string.toast_bilinmeyen_hata);
			
	}

	@Override
	protected void onActivityResult(int istekKodu, int sonucKodu, Intent data) {

		switch (istekKodu)
		{
			case CAMERA_REQUEST:
				
				if(sonucKodu == Activity.RESULT_OK)
				{
					profilResmi = (Bitmap) data.getExtras().get("data");
					
					if(profilResmi != null)
					{
						profilResmi = profilResminiYenidenBoyutlandir(profilResmi);
						profilImageButton.setImageBitmap(null);
						profilImageButton.setImageBitmap(profilResmi);
					}
				}
				
				break;
		}

	}
	
	private Bitmap profilResminiYenidenBoyutlandir(Bitmap profilPhoto) {
		
		int en = profilPhoto.getWidth();
		int boy = profilPhoto.getHeight();
		
		int yeniEn = (en >= boy) ? 150 : (int) ((float) 150 * ((float) en / (float) boy));
        int yeniBoy = (boy >= en) ? 150 : (int) ((float) 150 * ((float) boy / (float) en));

        return Bitmap.createScaledBitmap(profilPhoto, yeniEn, yeniBoy, true);
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(this, MenuActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		
	}
	

}
