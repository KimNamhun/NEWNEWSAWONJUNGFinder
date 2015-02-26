package com.nhncorp.student.newnewsawonjungfinder.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nhncorp.student.newnewsawonjungfinder.R;
import com.nhncorp.student.newnewsawonjungfinder.database.DbGetSet;
import com.nhncorp.student.newnewsawonjungfinder.function.manageActivity;
import com.nhncorp.student.newnewsawonjungfinder.map.MapActivity;
import com.nhncorp.student.newnewsawonjungfinder.registration.RegistrationActivity;

public class SettingActivity extends Activity {
	// private ImageButton logListButton;
	private ImageButton reRegistrationButton;
	private ImageButton lastLogButton;
	private ImageButton helpButton;

	private DbGetSet dbGetSet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		init();

	}

	private void init() {
		getView();
		addListener();
		dbGetSet = new DbGetSet(this);
	}

	private void getView() {
		// logListButton = (ImageButton) findViewById(R.id.logListButton);
		reRegistrationButton = (ImageButton) findViewById(R.id.reRegistrationButton);
		lastLogButton = (ImageButton) findViewById(R.id.lastLogButton);
		helpButton = (ImageButton) findViewById(R.id.helpButton);
	}

	private void addListener() {
		/*
		 * logListButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(SettingActivity.this, LogListActivity.class);
		 * startActivity(intent); } });
		 */
		reRegistrationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dbGetSet.getDeviceState().equals("1")) {
					Toast.makeText(SettingActivity.this,
							"사원증을 재등록하려면 스위치를'OFF'상태로 변경하여야 합니다.",
							Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent(SettingActivity.this,
							RegistrationActivity.class);
					SettingActivity.this.finish();
					manageActivity.getMainActivity().finish();
					startActivity(intent);
				}
			}
		});

		lastLogButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,
						MapActivity.class);
				startActivity(intent);
			}
		});

		helpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v == helpButton) {
					Context mContext = getApplicationContext();
					LayoutInflater inflater = (LayoutInflater) mContext
							.getSystemService(LAYOUT_INFLATER_SERVICE);

					View layout = inflater.inflate(R.layout.help_message,
							(ViewGroup) findViewById(R.id.helpMsg));
					AlertDialog.Builder aDialog = new AlertDialog.Builder(
							SettingActivity.this);

					aDialog.setTitle("도움말"); // 타이틀바 제목
					aDialog.setView(layout);

					// 그냥 닫기버튼을 위한 부분
					aDialog.setNegativeButton("닫기",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					// 팝업창 생성
					AlertDialog ad = aDialog.create();
					ad.show();// 보여줌!
				}
			}
		});
	}

}
