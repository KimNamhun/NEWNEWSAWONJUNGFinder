package com.nhncorp.student.newnewsawonjungfinder.map;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.NMapView.OnMapViewTouchEventListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapCalloutOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager.OnCalloutOverlayListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay.OnStateChangeListener;
import com.nhncorp.student.newnewsawonjungfinder.R;
import com.nhncorp.student.newnewsawonjungfinder.constants.Constants;
import com.nhncorp.student.newnewsawonjungfinder.database.DbGetSet;

public class MapActivity extends NMapActivity implements
		OnMapStateChangeListener, OnMapViewTouchEventListener,
		OnCalloutOverlayListener {

	NMapView mMapView = null;
	NMapController mMapController = null;
	LinearLayout MapContainer;

	NMapViewerResourceProvider mMapViewerResourceProvider = null;
	NMapOverlayManager mOverlayManager;
	OnStateChangeListener onPOIdataStateChangeListener = null;

	NMapPOIdata poiData;

	private DbGetSet dbGetSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MapContainer = (LinearLayout) findViewById(R.id.map);
		mMapView = new NMapView(this);

		mMapView.setApiKey(Constants.API_KEY);

		setContentView(mMapView);

		mMapView.setClickable(true);

		mMapView.setOnMapStateChangeListener(this);
		mMapView.setOnMapViewTouchEventListener(this);

		mMapView.setBuiltInZoomControls(true, null);
		mMapController = mMapView.getMapController();

		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
		mOverlayManager = new NMapOverlayManager(this, mMapView,
				mMapViewerResourceProvider);

		poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
		poiData.beginPOIdata(2);

		dbGetSet = new DbGetSet(this);

		if (dbGetSet.getLongitude().equals("0")
				&& dbGetSet.getLongitude().equals("0")) {
			Toast.makeText(this, "위치정보가 없어요", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "1km이내외의 오차가 있을 수 있습니다.", Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onLongPress(NMapView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLongPressCanceled(NMapView arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(NMapView arg0, MotionEvent arg1, MotionEvent arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSingleTapUp(NMapView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouchDown(NMapView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouchUp(NMapView arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStateChange(NMapView arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapCenterChange(NMapView arg0, NGeoPoint arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapCenterChangeFine(NMapView arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
		int markerId = NMapPOIflagType.PIN;
		if (errorInfo == null) {

			mMapController.setMapCenter(
					new NGeoPoint(Double.parseDouble(dbGetSet.getLongitude()),
							Double.parseDouble(dbGetSet.getLatitude())), 11);
			poiData.addPOIitem(Double.parseDouble(dbGetSet.getLongitude()),
					Double.parseDouble(dbGetSet.getLatitude()),
					"이 구역에서 없어졌을 가능성이 높아요!", markerId, 0);
			poiData.endPOIdata();

			NMapPOIdataOverlay poiDataOverlay = mOverlayManager
					.createPOIdataOverlay(poiData, null);
			poiDataOverlay.showAllPOIdata(0);

		} else { // fail
			android.util.Log.e("NMAP",
					"onMapInitHandler: error=" + errorInfo.toString());
		}

	}

	@Override
	public void onZoomLevelChange(NMapView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,
			NMapOverlayItem arg1, Rect arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
