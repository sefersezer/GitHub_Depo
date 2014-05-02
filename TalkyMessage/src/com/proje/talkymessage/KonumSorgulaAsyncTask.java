package com.proje.talkymessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.proje.talkymessage.HaritaActivity;
import com.proje.talkymessage.R;
import com.proje.talkymessage.DatabaseManager;
import com.proje.talkymessage.Profil;
import com.proje.talkymessage.map.ArkadasKonumItemizedOverlay;
import com.proje.talkymessage.map.MevcutKonumItemizedOverlay;

public class KonumSorgulaAsyncTask extends AsyncTask<Void, Void, List<Konum>>{

	private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private Context context;
	private MapView mapView;
	private Location konum;
	
	public KonumSorgulaAsyncTask(Context context, Location location) {
		super();
		this.context = context;
		this.konum = location;
		//apý v1 de mapview kullanýldýðý için bunu kaldýrdýk.
		//mapView = (MapView) ((Activity)context).findViewById(R.id.mapView);
	}

	@Override
	protected List<Konum> doInBackground(Void... params) {
		return getKonumList();
	}
	
	private List<Konum> getKonumList() {
		
		String kullaniciAdi = getKullaniciAdi();
		if(TextUtils.isEmpty(kullaniciAdi))
			return new ArrayList<Konum>();
		
		//konum sorgula fonksiyonu statik old için Yeni NM oluþturmadan konum sorgula fnk çaðýrdýk.
		return NetworkManager.konumSorgula(kullaniciAdi);
	}
	
	@Override
	protected void onPostExecute(List<Konum> result) {
		
		if(result == null || result.size() == 0)
			return;
		//en can alýcý noktamýz; konum hesaplama!
		//V1 baz alýnarak yazýlan bu yapý v2'ye uyarlanacaktýr.
		//burada iþaretleyici ayarlanmaktadýr.
		Drawable arkadasKonum = context.getResources().getDrawable(R.drawable.ic_arkadas_konum);
        Drawable mevcutKonum = context.getResources().getDrawable(R.drawable.ic_mevcut_konum);
        ArkadasKonumItemizedOverlay arkadasKonumOverlay = new ArkadasKonumItemizedOverlay(arkadasKonum, context);
        MevcutKonumItemizedOverlay mevcutKonumOverlay = new MevcutKonumItemizedOverlay(mevcutKonum);
        
        
        mapView.getOverlays().clear();//tüm iþaretleyicileri temizledik.
        //arkadasKonumOverlay.clearOverlays();
        //mevcutKonumOverlay.clearOverlays();
        
        String arkGoster = getHaritadaGosterilecekArkadas();
        
		Iterator<Konum> iterator = result.iterator();
		while (iterator.hasNext()) {
			Konum konum = (Konum) iterator.next();
			GeoPoint point = new GeoPoint((int) (konum.getEnlem() * 1e6),	(int) (konum.getBoylam() * 1e6));
			String guncellemeZamani = context.getResources().getString(R.string.son_guncelleme, format.format(konum.getGuncellemeZamani()));
			OverlayItem item = new OverlayItem(point, konum.getKullaniciAdi(), guncellemeZamani);
			
			if(TextUtils.isEmpty(arkGoster))
			{
				arkadasKonumOverlay.addOverlay(item);
			}
			else if(arkGoster.equalsIgnoreCase(konum.getKullaniciAdi()))
			{
				arkadasKonumOverlay.addOverlay(item);
			}
		}
		
		mapView.getOverlays().add(arkadasKonumOverlay);
		
		GeoPoint cPoint = new GeoPoint((int) (konum.getLatitude() * 1e6),(int) (konum.getLongitude() * 1e6));
		OverlayItem item = new OverlayItem(cPoint, "", "");
		mevcutKonumOverlay.addOverlay(item);
		mapView.getOverlays().add(mevcutKonumOverlay);
		
		mapView.postInvalidate();
		
		haritayiOrtala(result, konum);
		
	}
	
	private void haritayiOrtala(List<Konum> result, Location location) {
		//konumlar verildikten sonra haritada orta kýsýmda gözükmesini saðladýk
		result.add(new Konum(null, location.getLatitude(), location.getLongitude()));
		
		double kuzey = -90;
		double guney = 90;
		double bati = 180;
		double dogu = -180;

		Iterator<Konum> iterator = result.iterator();
		while (iterator.hasNext()) {
			Konum konum = (Konum) iterator.next();
			kuzey = Math.max(kuzey, konum.getEnlem());
			guney = Math.min(guney, konum.getEnlem());
			bati = Math.min(bati, konum.getBoylam());
			dogu = Math.max(dogu, konum.getBoylam());
		}
		
		double dikeyOrta = (kuzey + guney) / 2;
		double yatayOrta = (bati + dogu) / 2;
		
		GeoPoint merkez = new GeoPoint((int) (dikeyOrta * 1e6), (int) (yatayOrta * 1e6));
		
		int dikeyMesafe = (int) (Math.abs(kuzey - guney) * 1e6 * 1.2);
	    int yatayMesafe = (int) (Math.abs(bati - dogu) * 1e6 * 1.2);

	    mapView.getController().animateTo(merkez);
	    mapView.getController().zoomToSpan(dikeyMesafe, yatayMesafe);
	}

	private String getHaritadaGosterilecekArkadas() {
		//haritada bir arkads gösterilecek olduðunda bunu kullanacaðýz
		Intent intent = ((Activity)context).getIntent();
        
        if(intent.getExtras() == null || intent.getExtras().getString(HaritaActivity.ARKADAS_INTENT_EXTRA) == null)
        	return null;
        
        return intent.getExtras().getString(HaritaActivity.ARKADAS_INTENT_EXTRA);
	}

	private String getKullaniciAdi() {
		
		DatabaseManager manager = new DatabaseManager(context);
		
		Profil profil = manager.profilSorgula(null);
		
		if(profil == null)
			return null;
		
		return profil.getKullaniciAdi();
	}
	
	
}
