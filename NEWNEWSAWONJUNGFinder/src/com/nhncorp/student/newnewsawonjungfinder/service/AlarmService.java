package com.nhncorp.student.newnewsawonjungfinder.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;

import com.nhncorp.student.newnewsawonjungfinder.R;
import com.nhncorp.student.newnewsawonjungfinder.constants.Constants;
import com.nhncorp.student.newnewsawonjungfinder.database.DbGetSet;
import com.wizturn.sdk.central.Central;
import com.wizturn.sdk.central.CentralManager;
import com.wizturn.sdk.peripheral.Peripheral;
import com.wizturn.sdk.peripheral.PeripheralScanListener;

public class AlarmService extends Service implements LocationListener {

	private CentralManager centralManager;

	// notification
	private NotificationManager notificationManager;
	private Notification notification; // 통신 두절 알람
	private Notification notification2; // 사원증 감지중 알람
	private Notification notification3; // 멀어짐 알람

	// thread 사용 위한 선언
	private Thread mUiThread;
	private final Handler mHandler = new Handler();

	private int setAlarm = 0; // false
	private int setdistance = 0; // false

	// new algorithm variable
	ArrayList<Double> storedArr = new ArrayList<Double>();
	ArrayList<Double> dataArr = new ArrayList<Double>();
	private double sum = 0;
	SimpleDateFormat formatter = new SimpleDateFormat("ss", Locale.KOREA);
	private String scaleTime = "blank";
	private String currentTime = null;
	private static final int FREQUENCY = 20;

	private LocationManager locationManager;
	private String provider;

	private Location loc = null;
	private GpsLocationListener listener = null;

	private DbGetSet dbGetSet;

	private String macAddress;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mTimerHandler.sendEmptyMessage(0);
	}

	Handler mTimerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mTimerHandler.sendEmptyMessageDelayed(0, 1000);
			Constants.NOTIFYCOUNT++;
			if (Constants.NOTIFYCOUNT > 4) {
				if (notificationManager != null) {
					loadGps();
					getgps();
				}

			}
		}

	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		setCentralManager();
		dbGetSet = new DbGetSet(this);
		Intent intent2 = new Intent(
				"com.nhncorp.student.sawonjungfinder.service");
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent2, 0);
		notification2 = new Notification.Builder(getApplicationContext())
				.setContentTitle("사원증 찾기 동작중").setContentText("서비스가 동작 중입니다.")
				.setSmallIcon(R.drawable.main_icon)
				.setContentIntent(pendingIntent).build();
		macAddress = dbGetSet.getMacAddress();
		System.out.println("=====================start alarm service=========");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		System.out.println("=================stop service====================");
		super.onDestroy();
		if (notificationManager != null) {
			loadGps();
			getgps();
		}
		centralManager.stopScanning();
	}

	private void setCentralManager() {

		centralManager = CentralManager.getInstance();
		centralManager.init(getApplicationContext());
		centralManager.setPeripheralScanListener(new PeripheralScanListener() {
			@Override
			public void onPeripheralScan(Central central,
					final Peripheral peripheral) {

				if (macAddress.equals(peripheral.getBDAddress())) {

					runOnUiThread(new Runnable() {
						public void run() {
							Constants.NOTIFYCOUNT = 0;

							// //////////////////////////////////////////////////////////////////////
							// 거리 수신값 최적화

							// storedArr에 값을 넣는다.

							if (storedArr.size() < FREQUENCY) {

								storedArr.add(0, peripheral.getDistance());

							} else {

								// 데이터 받은 시간 저장

								currentTime = formatter.format(new Date());

								// 기준시간과 데이터 받은시간 비교

								if (scaleTime.equals(currentTime)) {

									dataArr.add(0, peripheral.getDistance());

								} else {

									// dataArr의 개수를 FREQUENCY로 맞춘다.

									if (dataArr.size() < FREQUENCY) {

										/*
										 * 
										 * for(int i = 0;
										 * i<FREQUENCY-dataArr.size(); i++){
										 * 
										 * dataArr.add(0,storedArr.get(i)); }
										 * 
										 * scan이 계속 일어나서 for문 수행이 제대로 이루어지지 않음
										 */

										if (dataArr.size() == 0) {

											dataArr = (ArrayList<Double>) storedArr

											.clone();

										} else if (dataArr.size() == 1) {

											dataArr.add(storedArr.get(18));

											dataArr.add(storedArr.get(17));

											dataArr.add(storedArr.get(16));

											dataArr.add(storedArr.get(15));

											dataArr.add(storedArr.get(14));

											dataArr.add(storedArr.get(13));

											dataArr.add(storedArr.get(12));

											dataArr.add(storedArr.get(11));

											dataArr.add(storedArr.get(10));

											dataArr.add(storedArr.get(9));

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 2) {

											dataArr.add(storedArr.get(17));

											dataArr.add(storedArr.get(16));

											dataArr.add(storedArr.get(15));

											dataArr.add(storedArr.get(14));

											dataArr.add(storedArr.get(13));

											dataArr.add(storedArr.get(12));

											dataArr.add(storedArr.get(11));

											dataArr.add(storedArr.get(10));

											dataArr.add(storedArr.get(9));

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 3) {

											dataArr.add(storedArr.get(16));

											dataArr.add(storedArr.get(15));

											dataArr.add(storedArr.get(14));

											dataArr.add(storedArr.get(13));

											dataArr.add(storedArr.get(12));

											dataArr.add(storedArr.get(11));

											dataArr.add(storedArr.get(10));

											dataArr.add(storedArr.get(9));

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 4) {

											dataArr.add(storedArr.get(15));

											dataArr.add(storedArr.get(14));

											dataArr.add(storedArr.get(13));

											dataArr.add(storedArr.get(12));

											dataArr.add(storedArr.get(11));

											dataArr.add(storedArr.get(10));

											dataArr.add(storedArr.get(9));

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 5) {

											dataArr.add(storedArr.get(14));

											dataArr.add(storedArr.get(13));

											dataArr.add(storedArr.get(12));

											dataArr.add(storedArr.get(11));

											dataArr.add(storedArr.get(10));

											dataArr.add(storedArr.get(9));

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 6) {

											dataArr.add(storedArr.get(13));

											dataArr.add(storedArr.get(12));

											dataArr.add(storedArr.get(11));

											dataArr.add(storedArr.get(10));

											dataArr.add(storedArr.get(9));

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 7) {

											dataArr.add(storedArr.get(12));

											dataArr.add(storedArr.get(11));

											dataArr.add(storedArr.get(10));

											dataArr.add(storedArr.get(9));

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 8) {

											dataArr.add(storedArr.get(11));

											dataArr.add(storedArr.get(10));

											dataArr.add(storedArr.get(9));

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 9) {

											dataArr.add(storedArr.get(10));

											dataArr.add(storedArr.get(9));

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 10) {

											dataArr.add(storedArr.get(9));

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 11) {

											dataArr.add(storedArr.get(8));

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 12) {

											dataArr.add(storedArr.get(7));

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 13) {

											dataArr.add(storedArr.get(6));

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 14) {

											dataArr.add(storedArr.get(5));

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 15) {

											dataArr.add(storedArr.get(4));

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 16) {

											dataArr.add(storedArr.get(3));

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 17) {

											dataArr.add(storedArr.get(2));

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 18) {

											dataArr.add(storedArr.get(1));

											dataArr.add(storedArr.get(0));

										} else if (dataArr.size() == 19) {

											dataArr.add(storedArr.get(0));

										}

									}

									// 개수를 맞추면 avg를 구한다. / 갱신한다.

									/*
									 * 
									 * for(int i = 0; i < FREQUENCY; i++){ sum
									 * 
									 * += dataArr.get(i); Constants.DISTANCE =
									 * 
									 * sum/FREQUENCY; }
									 */

									sum = dataArr.get(0) + dataArr.get(1)

									+ dataArr.get(2) + dataArr.get(3)

									+ dataArr.get(4) + dataArr.get(5)

									+ dataArr.get(6) + dataArr.get(7)

									+ dataArr.get(8) + dataArr.get(9)

									+ dataArr.get(10) + dataArr.get(11)

									+ dataArr.get(12) + dataArr.get(13)

									+ dataArr.get(14) + dataArr.get(15)

									+ dataArr.get(16) + dataArr.get(17)

									+ dataArr.get(18) + dataArr.get(19);

									Constants.DISTANCE = (int) Math.round(sum

									/ FREQUENCY);

									// storedArr를 갱신한다.

									storedArr = (ArrayList<Double>) dataArr

									.clone();

									dataArr.clear();

									dataArr.add(0, peripheral.getDistance());

									// 기준시간을 갱신한다.

									scaleTime = currentTime;

								}

							}

							setNotification(Constants.DISTANCE);

						}

					});

				}
			}

		});
		centralManager.startScanning();
	}

	private void setNotification(double distance) {

		if (Constants.DISTANCE < 10 && Constants.DISTANCE > 3) {
			setAlarm = 1;
		}

		if (Constants.DISTANCE > 15 && setAlarm == 1) {
			setDistanceNotify();
		}

		if (setdistance == 1 && Constants.DISTANCE < 10
				&& Constants.DISTANCE > 3) {
			if (notificationManager != null) {
				notificationManager.cancel(2);
				setdistance = 0;
			}
		}

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		System.out.println("push=============================== service");
		notificationManager.notify(1, notification2);
		// push notification

	}

	public final void runOnUiThread(Runnable action) {
		if (Thread.currentThread() != mUiThread) {
			mHandler.post(action);

		} else {
			action.run();

		}
	}

	public void getgps() {

		loc = getLocation();
		locationManager.removeUpdates(listener);

		if (loc == null) {
			setDistanceNullNotify();
		} else {
			setDistanceNotNullNotify();
			new Location(loc);
			dbGetSet.setLongitude(Double.toString(loc.getLongitude()));
			dbGetSet.setLatitude(Double.toString(loc.getLatitude()));
		}

	}

	private void setDistanceNotify() {
		Intent intent = new Intent(
				"com.nhncorp.student.sawonjungfinder.service");
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification3 = new Notification.Builder(getApplicationContext())
				.setContentTitle("사원증이 멀어졌습니다").setContentText("사원증을 두고 갔습니다")
				.setSmallIcon(R.drawable.main_alarm_distance_icon)
				.setTicker("사원증을 두고 가지 않으셨나요?").setAutoCancel(true)
				.setVibrate(new long[] { 1000, 1000 })
				.setContentIntent(pendingIntent).build();
		System.out.println("push===============================alarm");
		notificationManager.notify(2, notification3);
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(700);
		setAlarm = 0;
		setdistance = 1;

	}

	public void setDistanceNullNotify() {
		Intent intent = new Intent(
				"com.nhncorp.student.sawonjungfinder.service");
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification = new Notification.Builder(getApplicationContext())
				.setContentTitle("사원증과 통신이 끊겼습니다")
				.setContentText("WARNING! 위치 정보가 저장되지 않았습니다")
				.setSmallIcon(R.drawable.main_alarm_icon)
				.setTicker("사원증과 통신이 끊겼습니다").setAutoCancel(true)
				.setVibrate(new long[] { 1000, 1000 })
				.setContentIntent(pendingIntent).build();
		System.out.println("push===============================alarm");
		notificationManager.notify(1, notification);
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(1000);

	}

	public void setDistanceNotNullNotify() {
		Intent intent = new Intent(
				"com.nhncorp.student.sawonjungfinder.service");
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification = new Notification.Builder(getApplicationContext())
				.setContentTitle("사원증과 통신이 끊겼습니다")
				.setContentText("위치 정보가 저장되었습니다")
				.setSmallIcon(R.drawable.main_alarm_icon)
				.setTicker("사원증과 통신이 끊겼습니다").setAutoCancel(true)
				.setVibrate(new long[] { 1000, 1000 })
				.setContentIntent(pendingIntent).build();
		System.out.println("push===============================alarm");
		notificationManager.notify(1, notification);
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(1000);
	}

	private Location getLocation() {

		Location location = locationManager.getLastKnownLocation(provider);

		if (location == null) {
			System.out
					.println("NETWORK+++++++++++++++++++++++++++++++++++++++++");

			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		}

		return location;
	}

	public void loadGps() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 정확도
		criteria.setPowerRequirement(Criteria.ACCURACY_HIGH); // 전원 소비량
		criteria.setAltitudeRequired(false); // 고도, 높이 값을 얻어 올지를 결정
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false); // 속도
		criteria.setCostAllowed(true); // 위치 정보를 얻어 오는데 들어가는 금전적 비용
		provider = locationManager.getBestProvider(criteria, true);
		listener = new GpsLocationListener();
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			System.out.println("GPS+++++++++++++++++++++++++++++++++++++++++");
			locationManager.requestLocationUpdates(provider, 1000, 5, listener);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	private class GpsLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {

		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	}

}