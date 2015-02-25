package com.nhncorp.student.newnewsawonjungfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import com.nhncorp.student.newnewsawonjungfinder.bluetooth.BlueToothEnabler;
import com.nhncorp.student.newnewsawonjungfinder.database.DbGetSet;
import com.nhncorp.student.newnewsawonjungfinder.registration.RegistrationActivity;

public class IntroActivity extends Activity {

	private BlueToothEnabler bluetooth;

	private DbGetSet dbGetSet;

	Handler h;// 핸들러 선언

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 인트로화면이므로 타이틀바를 없앤다
		setContentView(R.layout.activity_intro);
		init();
		h = new Handler(); // 딜래이를 주기 위해 핸들러 생성
		h.postDelayed(mrun, 1000); // 딜레이 ( 런어블 객체는 mrun, 시간 1초)
	}

	private void init() {
		bluetooth = new BlueToothEnabler();
		boolean isBluetooth = bluetooth.enableBlueTooth();
		if (isBluetooth) {
			Toast.makeText(this, "블루투스가 작동 되고 있습니다", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "해당 단말은 블루투스를 지원하지 않습니다", Toast.LENGTH_LONG)
					.show();
		}
		dbGetSet = new DbGetSet(IntroActivity.this);

	}

	Runnable mrun = new Runnable() {
		@Override
		public void run() {
			Intent i;
			if (dbGetSet.getMacAddress().equals("0")) {
				i = new Intent(IntroActivity.this, RegistrationActivity.class);
			} else {
				i = new Intent(IntroActivity.this, MainActivity.class);
			}
			startActivity(i);
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			// overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요
		}
	};

	// 인트로 중에 뒤로가기를 누를 경우 핸들러를 끊어버려 아무일 없게 만드는 부분
	// 미 설정시 인트로 중 뒤로가기를 누르면 인트로 후에 홈화면이 나옴.
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		h.removeCallbacks(mrun);
	}

}