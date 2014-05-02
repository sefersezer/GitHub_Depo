package com.proje.talkymessage.map;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MevcutKonumItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	
	public MevcutKonumItemizedOverlay(Drawable drawable) {
		super(boundCenter(drawable));
	}
	
	public void addOverlay(OverlayItem overlay) {
		overlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();
	}
	
	public void clearOverlays() {
		overlays.clear();
		populate();
	}

}
