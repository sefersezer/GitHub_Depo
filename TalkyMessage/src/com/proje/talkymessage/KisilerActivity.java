package com.proje.talkymessage;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.proje.talkymessage.ArkadasEkleAsyncTask;


public class KisilerActivity extends BaseActivity {
	
	private static final int ARKADAS_EKLE_DIALOG_GOSTER = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kisiler);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
			case ARKADAS_EKLE_DIALOG_GOSTER:
				dialog = getArkadasEkleDialog();
				break;
			default:
				dialog = null;
		}
		
		return dialog;
	}
	
	private Dialog getArkadasEkleDialog() {
		
		final Dialog arkadasEkleDialog = new Dialog(this);
		
		arkadasEkleDialog.setContentView(R.layout.arkadas_ekle_dialog);
		arkadasEkleDialog.setTitle(getResources().getString(R.string.arkadas_ekle));
		
		final EditText txtKullaniciAdi = (EditText) arkadasEkleDialog.findViewById(R.id.txtKullaniciAdi);
		
		Button DiaBtnArkadasEkle = (Button) arkadasEkleDialog.findViewById(R.id.DiaBtnArkadasEkle);
		DiaBtnArkadasEkle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Editable kullaniciAdiEditable = txtKullaniciAdi.getText();
				String kullaniciAdi = (kullaniciAdiEditable != null) ? kullaniciAdiEditable.toString() : "";
            	arkadasEkle(kullaniciAdi);
            	arkadasEkleDialog.dismiss();
            }
        });
		
		Button iptalButton = (Button) arkadasEkleDialog.findViewById(R.id.DiaBtnArkIptal);
		iptalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	arkadasEkleDialog.dismiss();
            }
        });
		
		return arkadasEkleDialog;
		
	}
	
	private void arkadasEkle(String arkadasKullaniciAdi) {
		
		if(TextUtils.isEmpty(arkadasKullaniciAdi)) {
			String message = getResources().getString(R.string.toast_bos_parametre_hatasi, "Kullanýcý Adý");
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			return;
		}
		
		FragmentManager fragmentManager = getFragmentManager();
		ListFragment kisilerListFragment = (ListFragment) fragmentManager.findFragmentById(R.id.kisilerListFragment);
		ArkadasEkleAsyncTask task = new ArkadasEkleAsyncTask(this, kisilerListFragment);
		task.execute(arkadasKullaniciAdi);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu_kisiler, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean onOptionsItemSelected(MenuItem item) {
	    
		switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(this, MenuActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	              case R.id.kisiEkleBtn:
	            showDialog(ARKADAS_EKLE_DIALOG_GOSTER);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		
	}
	
}
