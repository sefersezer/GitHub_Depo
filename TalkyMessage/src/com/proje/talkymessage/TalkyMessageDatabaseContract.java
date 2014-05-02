package com.proje.talkymessage;

import android.provider.BaseColumns;

public final class TalkyMessageDatabaseContract {
	
	public static final String VERITABANI_ADI = "gdguc";
	public static final String TABLO_ADI = "tbl_kullanici_profil";
	public static final int VERITABANI_VERSIYON = 1;
	
	public static class Profil implements BaseColumns {
		
		private Profil() {}
		
		public static final String COLUMN_KULLANICI_ADI = "kullanici_adi";
		public static final String COLUMN_AD = "ad";
		public static final String COLUMN_SOYAD = "soyad";
		public static final String COLUMN_TELEFON = "telefon";
		public static final String COLUMN_EMAIL = "email";
		
		public static final String DEFAULT_SORT_ORDER = "ad ASC";
		
		public static final String[] TABLO_YAPISI = new String[] { _ID, 
															COLUMN_KULLANICI_ADI, 
															COLUMN_AD,
															COLUMN_SOYAD,
															COLUMN_TELEFON,
															COLUMN_EMAIL};
		
	}
	
}
