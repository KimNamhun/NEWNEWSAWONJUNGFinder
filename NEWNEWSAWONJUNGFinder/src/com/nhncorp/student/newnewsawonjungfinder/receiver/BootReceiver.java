package com.nhncorp.student.newnewsawonjungfinder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nhncorp.student.newnewsawonjungfinder.service.AlarmService;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent bootIntent = new Intent(context, AlarmService.class);

			context.startService(bootIntent);
			System.out.println("===========bootreceiver=============start");
		}
	}

}
