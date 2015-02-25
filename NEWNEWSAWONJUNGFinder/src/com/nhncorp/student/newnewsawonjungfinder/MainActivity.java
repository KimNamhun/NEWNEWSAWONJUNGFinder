package com.nhncorp.student.newnewsawonjungfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nhncorp.student.newnewsawonjungfinder.constants.Constants;
import com.nhncorp.student.newnewsawonjungfinder.database.DbGetSet;
import com.nhncorp.student.newnewsawonjungfinder.service.AlarmService;

public class MainActivity extends Activity {

	private ImageButton devOnOffBtn;

	private TextView distanceData;
	private TextView distanceMessage;

	private Thread myThread = null;
	private boolean runThread = true;

	private DbGetSet dbGetSet;

	int countVibrator = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (myThread != null && myThread.isAlive()) {
					runThread = false;
				}
				this.finish();
			default:
			}
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		getView();
		dbGetSet = new DbGetSet(MainActivity.this);
		deviceConfirm(1); // nothing
		addListener();
	}

	private void deviceConfirm(int sel) {
		if (sel == 2) { // dev버튼을 누른 경우 devicestate의 값을 변경

			if (dbGetSet.getDeviceState().equals("0")) {
				dbGetSet.setDeviceState("1");
				devOnOffBtn.setImageResource(R.drawable.main_service_on);
				showSettingsAlert();
				setService(1); // start
				myThread.start();
			} else if (dbGetSet.getDeviceState().equals("1")) {
				dbGetSet.setDeviceState("0");
				devOnOffBtn.setImageResource(R.drawable.main_service_off);
				setService(0); // stop
			}
		} else if (sel == 1) { // 아무일도 하지 않음
			if (dbGetSet.getDeviceState().equals("0")) {
				devOnOffBtn.setImageResource(R.drawable.main_service_off);
			} else if (dbGetSet.getDeviceState().equals("1")) {
				devOnOffBtn.setImageResource(R.drawable.main_service_on);
			}

		}

	}

	private void addListener() {
		devOnOffBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				deviceConfirm(2); // dev
				System.out
						.println("DEVONOFF CLICK====================================");

			}
		});

	}

	private void getView() {
		devOnOffBtn = (ImageButton) findViewById(R.id.alarmButton);
		distanceData = (TextView) findViewById(R.id.distanceData);
		distanceMessage = (TextView) findViewById(R.id.stateMessage);

	}

	private void setService(int i) {
		Intent intent = new Intent(this, AlarmService.class);
		if (i == 0) { // stop
			stopService(intent);
		} else if (i == 1) { // start
			startService(intent);
		}
	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setTitle("서비스 실행 중!");
		alertDialog
				.setMessage("사원증과 통신이 끊겼을 때 위치 정보를 저장하려면, '위치서비스' 사용을 허용해야합니다.");

		// OK 를 누르게 되면 설정창으로 이동합니다.
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						MainActivity.this.startActivity(intent);
					}
				});
		// Cancel하면 종료 합니다.
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateThread();
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		myThread = new Thread(new Runnable() {
			public void run() {
				while (runThread) {
					try {
						handler.sendMessage(handler.obtainMessage());
						Thread.sleep(100);
					} catch (Throwable t) {
					}
				}
			}
		});
		if (dbGetSet.getDeviceState().equals("1")) {
			myThread.start();
		}
	}

	private void updateThread() {

		// 거리는 1m단위로 제공
		if (Constants.DISTANCE <= 30) {
			distanceData.setText(Constants.DISTANCE + "M");
		} else {
			distanceData.setText("...");
		}

		// 2m 이하일 때 메세지 표시, 진동 발생
		if (Constants.DISTANCE <= 2) {
			distanceMessage.setText("근접!");
			Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			if (countVibrator == 0) {
				vibrator.vibrate(200);
				countVibrator++;
			}
		} else {
			distanceMessage.setText(" ");
			countVibrator = 0;
		}

		// 신호가 없는 경우
		if (Constants.NOTIFYCOUNT > 4) {
			Constants.DISTANCE = 40;
			distanceMessage.setText("no signal");
		}
	}

}
