package com.proje.talkymessage;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


public class KonumGuncelleService extends Service implements LocationListener {
	
	private LocationManager konumYonetici;
	
	@Override
	public void onCreate() {
		//konum guncelle servisi arkaplanda çalıştığında bu kodlar işleyecek
		super.onCreate();
		
		//konum yönetici oluşturduk
		konumYonetici = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.d("KonumGuncelleService", "started");
		
		konumYonetici.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
		
		return START_NOT_STICKY;//START_STICKY || servis çalışırken nasıl davranacağını belirledik.
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void onLocationChanged(Location location) {
		//konum değiştiğinde tekrar uzaklık hesaplaması gerekecek
		UzaklikHesaplaAsyncTask task = new UzaklikHesaplaAsyncTask(this);
		task.execute(new Location[] {location});
	}
	
	
	//bu zorunlu - implemented fonksiyonları kullanmadık
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	public void onProviderEnabled(String provider) {}
	public void onProviderDisabled(String provider) {}

}
