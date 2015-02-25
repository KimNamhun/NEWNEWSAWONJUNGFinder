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

	Handler h;// �ڵ鷯 ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // ��Ʈ��ȭ���̹Ƿ� Ÿ��Ʋ�ٸ� ���ش�
		setContentView(R.layout.activity_intro);
		init();
		h = new Handler(); // �����̸� �ֱ� ���� �ڵ鷯 ����
		h.postDelayed(mrun, 1000); // ������ ( ����� ��ü�� mrun, �ð� 1��)
	}

	private void init() {
		bluetooth = new BlueToothEnabler();
		boolean isBluetooth = bluetooth.enableBlueTooth();
		if (isBluetooth) {
			Toast.makeText(this, "��������� �۵� �ǰ� �ֽ��ϴ�", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "�ش� �ܸ��� ��������� �������� �ʽ��ϴ�", Toast.LENGTH_LONG)
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
			// overridePendingTransition �̶� �Լ��� �̿��Ͽ� fade in,out ȿ������. ������ �߿�
		}
	};

	// ��Ʈ�� �߿� �ڷΰ��⸦ ���� ��� �ڵ鷯�� ������� �ƹ��� ���� ����� �κ�
	// �� ������ ��Ʈ�� �� �ڷΰ��⸦ ������ ��Ʈ�� �Ŀ� Ȩȭ���� ����.
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		h.removeCallbacks(mrun);
	}

}