package com.proje.talkymessage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.proje.talkymessage.BaseActivity;
import com.proje.talkymessage.Profil;

public class NetworkManager {
	
	private static final String TAG = "NetworkManager";
	
	public static String profilKaydet(Profil profil) {
		
		BufferedReader in = null;//xml i�in
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(BaseActivity.TALKYMESSAGE_PROFIL_KAYDET_URL);
			
			List<NameValuePair> parametreList = new ArrayList<NameValuePair>();
			parametreList.add(new BasicNameValuePair("kullanici_adi", profil.getKullaniciAdi()));
			parametreList.add(new BasicNameValuePair("ad", profil.getAd()));
			parametreList.add(new BasicNameValuePair("soyad", profil.getSoyad()));
			parametreList.add(new BasicNameValuePair("telefon", profil.getTelefon()));
			parametreList.add(new BasicNameValuePair("email", profil.getEmail()));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametreList);
			request.setEntity(entity);
						
			HttpResponse response = client.execute(request);
			
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			return in.readLine();
		
		} catch (Exception e) {
			Log.d(TAG, "Profil kaydedilirken hata olu�tu", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	public static List<Profil> arkadasSorgula(String kullaniciAdi) {
		
		if(TextUtils.isEmpty(kullaniciAdi))
			return new ArrayList<Profil>();
		
		InputStream content = null;
		
		try {
			
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(BaseActivity.TALKYMESSAGE_ARKADAS_LISTELE_URL);
			
			List<NameValuePair> parametreList = new ArrayList<NameValuePair>();
			parametreList.add(new BasicNameValuePair("kullanici_adi", kullaniciAdi));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametreList);
			request.setEntity(entity);
						
			HttpResponse response = client.execute(request);

			content = response.getEntity().getContent();
			
			List<Profil> arkadasList = getArkadasListInputStream(content);
			
			return arkadasList;
			
			
		} catch (Exception e) {
			Log.d(TAG, "HTTP ba�lant�s� kurulurken hata olu�tu", e);
		} finally {
			if (content != null) {
				try {
					content.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return new ArrayList<Profil>();
		
	}
	
	private static List<Profil> getArkadasListInputStream(InputStream content) {
		// xml verisi php dosyas�ndan �ekilecek. node yap�s� olu�turup istek g�nderiyoruz.
		List<Profil> arkadasList = new ArrayList<Profil>();
		
		try {
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document document = docBuilder.parse(content);
			Element root = document.getDocumentElement();
			NodeList arkadasNodeList = root.getElementsByTagName("arkadas");
			
			if(arkadasNodeList == null || arkadasNodeList.getLength() == 0)
				return arkadasList;
			
			int arkadasSayisi = arkadasNodeList.getLength();
			
			for (int i = 0; i < arkadasSayisi; i++) {
				Element arkadas = (Element) arkadasNodeList.item(i);
				Node kullaniciAdiNode = arkadas.getElementsByTagName("kullanici_adi").item(0);
				String kullaniciAdi = kullaniciAdiNode.getFirstChild().getNodeValue();
				
				Node adNode = arkadas.getElementsByTagName("ad").item(0).getFirstChild();
				String ad = adNode.getNodeValue();
				
				Node soyadNode = arkadas.getElementsByTagName("soyad").item(0).getFirstChild();
				String soyad = soyadNode.getNodeValue();
				
				Node telefonNode = arkadas.getElementsByTagName("telefon").item(0).getFirstChild();
				String telefon = (telefonNode != null) ? telefonNode.getNodeValue() : "";
				
				Node emailNode = arkadas.getElementsByTagName("email").item(0).getFirstChild();
				String email = (emailNode != null) ? emailNode.getNodeValue() : "";
				
				Profil profil = new Profil();
				profil.setKullaniciAdi(kullaniciAdi);
				profil.setAd(ad);
				profil.setSoyad(soyad);
				profil.setTelefon(telefon);
				profil.setEmail(email);
				
				arkadasList.add(profil);
			}
			
		} catch (Exception e) {
			Log.d(TAG, "xml sistem hatas�", e);
		}
		
		return arkadasList;
		
	}
	
	
	public static String arkadasEkle(String kullaniciAdi, String arkadasKullaniciAdi) {
		
		BufferedReader in = null;
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(BaseActivity.TALKYMESSAGE_ARKADAS_EKLE_URL);
			
			List<NameValuePair> parametreList = new ArrayList<NameValuePair>();
			parametreList.add(new BasicNameValuePair("kullanici_adi", kullaniciAdi));
			parametreList.add(new BasicNameValuePair("arkadas_kullanici_adi", arkadasKullaniciAdi));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametreList);
			request.setEntity(entity);
						
			HttpResponse response = client.execute(request);
			
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			return in.readLine();
		
		} catch (Exception e) {
			Log.d(TAG, "Arkada� eklenirken hata olu�tu", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
		
	}
	
	public static String konumKaydet(Konum konum) {
		
		BufferedReader in = null;
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(BaseActivity.TALKYMESSAGE_KONUM_KAYDET_URL);
			
			List<NameValuePair> parametreList = new ArrayList<NameValuePair>();
			parametreList.add(new BasicNameValuePair("kullanici_adi", konum.getKullaniciAdi()));
			parametreList.add(new BasicNameValuePair("enlem", String.valueOf(konum.getEnlem())));
			parametreList.add(new BasicNameValuePair("boylam", String.valueOf(konum.getBoylam())));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametreList);
			request.setEntity(entity);
						
			HttpResponse response = client.execute(request);
			
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			return in.readLine();
		
		} catch (Exception e) {
			Log.d(TAG, "Konum kaydedilirken hata olu�tu", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
		
	}
	
	public static List<Konum> konumSorgula(String kullaniciAdi) {
	
		if(TextUtils.isEmpty(kullaniciAdi))
			return new ArrayList<Konum>();
		
		InputStream content = null;
		
		try {
			
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(BaseActivity.TALKYMESSAGE_KONUM_SORGULA_URL);
			
			List<NameValuePair> parametreList = new ArrayList<NameValuePair>();
			parametreList.add(new BasicNameValuePair("kullanici_adi", kullaniciAdi));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametreList);
			request.setEntity(entity);
						
			HttpResponse response = client.execute(request);

			content = response.getEntity().getContent();
			
			List<Konum> konumList = getKonumListInputStream(content);
			
			return konumList;
			
			
		} catch (Exception e) {
			Log.d(TAG, "HTTP ba�lant�s� kurulurken hata olu�tu", e);
		} finally {
			if (content != null) {
				try {
					content.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return new ArrayList<Konum>();
		
	}
	
	private static List<Konum> getKonumListInputStream(InputStream content) {
		
		List<Konum> konumList = new ArrayList<Konum>();
		
		try {
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document document = docBuilder.parse(content);
			Element root = document.getDocumentElement();
			NodeList konumNodeList = root.getElementsByTagName("arkadas");
			
			if(konumNodeList == null || konumNodeList.getLength() == 0)
				return konumList;
			
			int arkadasSayisi = konumNodeList.getLength();
			
			for (int i = 0; i < arkadasSayisi; i++) {
				Element arkadas = (Element) konumNodeList.item(i);
				Node kullaniciAdiNode = arkadas.getElementsByTagName("kullanici_adi").item(0);
				String kullaniciAdi = kullaniciAdiNode.getFirstChild().getNodeValue();
				
				Node enlemNode = arkadas.getElementsByTagName("enlem").item(0).getFirstChild();
				String enlem = enlemNode.getNodeValue();
				
				Node boylamNode = arkadas.getElementsByTagName("boylam").item(0).getFirstChild();
				String boylam = boylamNode.getNodeValue();
				
				Node guncellemeZamaniNode = arkadas.getElementsByTagName("guncelleme_zamani").item(0).getFirstChild();
				String guncellemeZamani = guncellemeZamaniNode.getNodeValue(); 
				
				Konum konum = new Konum();
				konum.setKullaniciAdi(kullaniciAdi);
				konum.setEnlem(Double.valueOf(enlem));
				konum.setBoylam(Double.valueOf(boylam));
				konum.setGuncellemeZamani(Timestamp.valueOf(guncellemeZamani));
				
				konumList.add(konum);
			}
			
		} catch (Exception e) {
			Log.d(TAG, "Konum XML parse edilirken hata olu�tu", e);
		}
		
		return konumList;
		
	}

}
