package com.proje.talkymessage;

import java.io.File;
import java.io.FileOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.proje.talkymessage.BaseActivity;
//bu sayfada "DatabaseHelper" yapýsý baz alýnarak veri yazma, oluþturma, güncelleme, sorgulama yapýlmýþtýr.
//sqllite yerel veritabanýna kayýt yapýlmaktadýr.
public class DatabaseManager {
	
	public static final int BASARILI = 1;
	public static final int BILINMEYEN_HATA = -1;
	public static final int PROFIL_VALIDASYON_HATASI = -2;
	
	private DatabaseHelper helper;
	private Context context;
	
	public DatabaseManager(Context context) {//context; helper denilen asýl yapý ile buradaki fonksiyonlarý baðlamaya yarýyor
		this.context = context;
		helper = new DatabaseHelper(context);
	}
	
	public Profil profilSorgula(String kullaniciAdi) {
		
		String where = null;
    	String [] whereArgs = null;
		
		
		if(!TextUtils.isEmpty(kullaniciAdi)) {//kullanýcý adý boþ DEÐÝL ise
			where = TalkyMessageDatabaseContract.Profil.COLUMN_KULLANICI_ADI + "= ?";
	    	whereArgs = new String [] {kullaniciAdi};
		}
		
    	SQLiteDatabase db = helper.getReadableDatabase();
    	Cursor cursor = db.query(TalkyMessageDatabaseContract.TABLO_ADI, TalkyMessageDatabaseContract.Profil.TABLO_YAPISI, where, whereArgs, null, null, null);
		
		return profilOlustur(cursor);
	}
	
	private Profil profilOlustur(Cursor cursor) {
		
		if(cursor == null || cursor.getCount() != 1 || !cursor.moveToNext())
    		return null;
		
		Profil profil = new Profil();
		
		int idIndex = cursor.getColumnIndex(TalkyMessageDatabaseContract.Profil._ID);
		profil.setId(cursor.getInt(idIndex));
		
		int kullaniciAdiIndex = cursor.getColumnIndex(TalkyMessageDatabaseContract.Profil.COLUMN_KULLANICI_ADI);
		profil.setKullaniciAdi(cursor.getString(kullaniciAdiIndex));
		
		int adIndex = cursor.getColumnIndex(TalkyMessageDatabaseContract.Profil.COLUMN_AD);
		profil.setAd(cursor.getString(adIndex));
		
		int soyadIndex = cursor.getColumnIndex(TalkyMessageDatabaseContract.Profil.COLUMN_SOYAD);
		profil.setSoyad(cursor.getString(soyadIndex));
		
		int telefonIndex = cursor.getColumnIndex(TalkyMessageDatabaseContract.Profil.COLUMN_TELEFON);
		profil.setTelefon(cursor.getString(telefonIndex));
		
		int emailIndex = cursor.getColumnIndex(TalkyMessageDatabaseContract.Profil.COLUMN_EMAIL);
		profil.setEmail(cursor.getString(emailIndex));	
		
		Bitmap profilPhoto = profilResimSorgula();
		profil.setProfilPhoto(profilPhoto);
		
		return profil;
		
	}
	
	public int profilKaydetGuncelle(Profil profil) {
		
		if(!isProfilValid(profil))
			return PROFIL_VALIDASYON_HATASI;
		
		ContentValues satir = new ContentValues();
    	satir.put(TalkyMessageDatabaseContract.Profil.COLUMN_KULLANICI_ADI, profil.getKullaniciAdi());
    	satir.put(TalkyMessageDatabaseContract.Profil.COLUMN_AD, profil.getAd());
    	satir.put(TalkyMessageDatabaseContract.Profil.COLUMN_SOYAD, profil.getSoyad());
    	satir.put(TalkyMessageDatabaseContract.Profil.COLUMN_TELEFON, profil.getTelefon());
    	satir.put(TalkyMessageDatabaseContract.Profil.COLUMN_EMAIL, profil.getEmail());
    	
    	profilResimKaydet(profil.getProfilPhoto());
    	
		Profil kayitliProfil = profilSorgula(profil.getKullaniciAdi());
		
		if(kayitliProfil != null)
			return profilGuncelle(kayitliProfil.getId(), satir);
		
		return profilKaydet(satir);
	}
	
	public int profilKaydet(ContentValues satir) {
		
		SQLiteDatabase db = helper.getWritableDatabase();
    	long profilId = db.insert(TalkyMessageDatabaseContract.TABLO_ADI, null, satir);
    	
    	if(profilId == -1)
    		return BILINMEYEN_HATA;
    	
    	return BASARILI;
		
	}
	
	public int profilGuncelle(int id, ContentValues satir) {
		
		SQLiteDatabase db = helper.getWritableDatabase();
    	String where = TalkyMessageDatabaseContract.Profil._ID + "=" + id;
    	
    	int guncellenenSatirSayisi = db.update(TalkyMessageDatabaseContract.TABLO_ADI, satir, where, null);
    	
    	if(guncellenenSatirSayisi != 1)
    		return BILINMEYEN_HATA;
    	
    	return BASARILI;
		
	}
	
	private boolean isProfilValid(Profil profil) {
		
		if(profil == null)
			return false;
		
		
		if (TextUtils.isEmpty(profil.getKullaniciAdi())
				|| TextUtils.isEmpty(profil.getAd())
				|| TextUtils.isEmpty(profil.getSoyad()))
			return false;
		
		return true;
	}
	
	private void profilResimKaydet(Bitmap profilPhoto) {
		//proje içine resim kaydetme
		try {
			
			if(profilPhoto == null)
				return;
			
			FileOutputStream fos = context.openFileOutput(BaseActivity.PROFILE_PHOTO_FILE_NAME, Context.MODE_PRIVATE);
			profilPhoto.compress(CompressFormat.JPEG, 100, fos);
			fos.close();
			
        } catch (Exception e) {
            Log.e("DatabaseManager", "Profil Fotografi kaydedilirken hata olustu", e);
        }
		
	}
	
	private Bitmap profilResimSorgula() {
		
		File icBellekAdres = context.getFilesDir();
		
		if(icBellekAdres == null)
			return null;
		
		File profilPhotoFile = new File(icBellekAdres, BaseActivity.PROFILE_PHOTO_FILE_NAME);
		
		if(!profilPhotoFile.exists())
			return null;
		
		return BitmapFactory.decodeFile(profilPhotoFile.getAbsolutePath());
		
	}
	
	

}
