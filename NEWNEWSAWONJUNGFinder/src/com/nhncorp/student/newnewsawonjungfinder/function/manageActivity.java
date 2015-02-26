package com.nhncorp.student.newnewsawonjungfinder.function;

import android.app.Activity;

public class manageActivity {
	private static Activity mainActivity;

	public static Activity getMainActivity() {
		return mainActivity;
	}

	public static void setMainActivity(Activity mainAct) {
		mainActivity = mainAct;
	}
}
