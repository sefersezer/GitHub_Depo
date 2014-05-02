package com.proje.talkymessage;

import android.app.Activity;

public class BaseActivity extends Activity {
	
	public static final String PROFILE_PHOTO_FILE_NAME = "ProfilFotografim.jpg";
	
	public static final String TALKYMESSAGE_BASE_URL = "http://guneydogugucsistemleri.com/ts/talkymessage/";
	public static final String TALKYMESSAGE_PROFIL_KAYDET_URL = TALKYMESSAGE_BASE_URL + "profil_kaydet.php";
	public static final String TALKYMESSAGE_ARKADAS_LISTELE_URL = TALKYMESSAGE_BASE_URL + "arkadas_listele.php";
	public static final String TALKYMESSAGE_ARKADAS_EKLE_URL = TALKYMESSAGE_BASE_URL + "arkadas_ekle.php";
	public static final String TALKYMESSAGE_KONUM_SORGULA_URL = TALKYMESSAGE_BASE_URL + "konum_sorgula.php";
	public static final String TALKYMESSAGE_KONUM_KAYDET_URL = TALKYMESSAGE_BASE_URL + "konum_kaydet.php";
	
}
