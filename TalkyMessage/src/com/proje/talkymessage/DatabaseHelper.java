package com.proje.talkymessage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


//"sqllite" yerel veritabanýný kullanmak için yapýlandýrýlmýþ sayfa.
//bu sayfada profil bilgilerine ait tablo yapýsý kodlanmýþtýr.
public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_CREATE = 
			"CREATE TABLE " + TalkyMessageDatabaseContract.TABLO_ADI + " (" +
			TalkyMessageDatabaseContract.Profil._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TalkyMessageDatabaseContract.Profil.COLUMN_KULLANICI_ADI + " VARCHAR(20) NOT NULL, " +
            TalkyMessageDatabaseContract.Profil.COLUMN_AD + " VARCHAR(20) NOT NULL, " +
            TalkyMessageDatabaseContract.Profil.COLUMN_SOYAD + " VARCHAR(20) NOT NULL, " +
            TalkyMessageDatabaseContract.Profil.COLUMN_TELEFON + " VARCHAR(10) , " +
            TalkyMessageDatabaseContract.Profil.COLUMN_EMAIL + " VARCHAR(20) );";
	
	//bu string SQLite'daki veritabanýný silmek için kullanýlacak
	public static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + TalkyMessageDatabaseContract.TABLO_ADI;
	
	public DatabaseHelper(Context context) {
		super(context, TalkyMessageDatabaseContract.VERITABANI_ADI, null, TalkyMessageDatabaseContract.VERITABANI_VERSIYON);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//sqllite veritabanýn günccellenmesi için bunlar kullanýlacak
		Log.w("DatabaseHelper", "Veritabani " + oldVersion + "\'dan" + newVersion + "\'a guncelleniyor");
		
		db.execSQL(DATABASE_DROP);
		onCreate(db);
		
	}

}
