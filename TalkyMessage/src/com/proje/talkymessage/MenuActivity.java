package com.proje.talkymessage;

import com.proje.talkymessage.AyarlarActivity;
import com.proje.talkymessage.MenuActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MenuActivity extends BaseActivity {
	
	private ImageButton ayarlarImageButton;
	private ImageButton profilImageButton;
	private ImageButton kimlerImageButton;
	private ImageButton haritaImageButton;
	private ImageButton hakkimizdaImageButton;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        initLayoutWidgets();
    }
	
	private void initLayoutWidgets() {
		
		ayarlarImageButton = (ImageButton) findViewById(R.id.ayarlarImageButton);
		ayarlarImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MenuActivity.this, AyarlarActivity.class);
            	startActivity(intent);
            }
        });
		
		profilImageButton = (ImageButton) findViewById(R.id.profilImageButton);
		profilImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MenuActivity.this, ProfilActivity.class);
            	startActivity(intent);
            }
        });
		
		kimlerImageButton = (ImageButton) findViewById(R.id.kisilerImageButton);
		kimlerImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MenuActivity.this, KisilerActivity.class);
            	startActivity(intent);
            }
        });
		
		haritaImageButton = (ImageButton) findViewById(R.id.haritaImageButton);
		haritaImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MenuActivity.this, HaritaActivity.class);
            	startActivity(intent);
            }
        });
		
		hakkimizdaImageButton = (ImageButton) findViewById(R.id.hakkimizdaImageButton);
		hakkimizdaImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MenuActivity.this, HakkimizdaActivity.class);
            	startActivity(intent);
            }
        });
	}

}
