package com.nhncorp.student.newnewsawonjungfinder.database;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

public class DbGetSet extends Activity {

	private DbOpenHelper mDbOpenHelper;

	private String macAddress;
	private String deviceState;
	private String longitude;
	private String latitude;

	private Cursor mCursor;

	private Context context;

	public DbGetSet(Context context) {
		setContext(context);
		dbMake();
	}

	private void dbMake() {
		dbOpen();
		dbClose();
	}

	private void dbOpen() {
		mDbOpenHelper = new DbOpenHelper(context);
		mDbOpenHelper.open();
		mCursor = mDbOpenHelper.getAll();
		mCursor.moveToFirst();
	}

	private void dbClose() {
		mDbOpenHelper.close();
	}

	public String getMacAddress() {
		dbOpen();
		macAddress = mCursor.getString(mCursor.getColumnIndex("macaddress"));
		dbClose();
		return macAddress;
	}

	public String getDeviceState() {
		dbOpen();
		deviceState = mCursor.getString(mCursor.getColumnIndex("devicestate"));
		dbClose();
		return deviceState;
	}

	public String getLongitude() {
		dbOpen();
		longitude = mCursor.getString(mCursor.getColumnIndex("longitude"));
		dbClose();
		return longitude;
	}

	public String getLatitude() {
		dbOpen();
		latitude = mCursor.getString(mCursor.getColumnIndex("latitude"));
		dbClose();
		return latitude;
	}

	public void setMacAddress(String macAddress) {
		setVariableAll();
		dbOpen();
		mDbOpenHelper.updateColumn(1, macAddress, deviceState, longitude,
				latitude);

		dbClose();
	}

	public void setDeviceState(String deviceState) {
		setVariableAll();
		dbOpen();
		mDbOpenHelper.updateColumn(1, macAddress, deviceState, longitude,
				latitude);
		dbClose();
	}

	public void setLongitude(String longitude) {
		setVariableAll();
		dbOpen();

		mDbOpenHelper.updateColumn(1, macAddress, deviceState, longitude,
				latitude);
		dbClose();
	}

	public void setLatitude(String latitude) {
		setVariableAll();
		dbOpen();

		mDbOpenHelper.updateColumn(1, macAddress, deviceState, longitude,
				latitude);
		dbClose();
	}

	public void setVariableAll() {
		dbOpen();
		macAddress = mCursor.getString(mCursor.getColumnIndex("macaddress"));
		deviceState = mCursor.getString(mCursor.getColumnIndex("devicestate"));
		longitude = mCursor.getString(mCursor.getColumnIndex("longitude"));
		latitude = mCursor.getString(mCursor.getColumnIndex("latitude"));
		dbClose();
	}

	public void setRegistration(String getBDaddress) {
		dbOpen();
		mDbOpenHelper.updateColumn(1, getBDaddress, "0", "127.123067",
				"37.388126");
		dbClose();
	}

	public void setContext(Context context) {
		this.context = context;
	}
}