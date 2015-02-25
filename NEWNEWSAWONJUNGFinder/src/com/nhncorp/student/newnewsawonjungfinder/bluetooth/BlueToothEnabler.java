package com.nhncorp.student.newnewsawonjungfinder.bluetooth;

import android.bluetooth.BluetoothAdapter;

public class BlueToothEnabler {

	private BluetoothAdapter bluetoothAdapter = null;

	public boolean enableBlueTooth() {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			return false; // 블루투스 지원안함
		} else {

			if (bluetoothAdapter.isEnabled()) {
				return true; // 지원
			} else {
				bluetoothAdapter.enable();
				return true; // 지원

			}
		}
	}

}