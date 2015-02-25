package com.nhncorp.student.newnewsawonjungfinder.database;

import android.provider.BaseColumns;

public class Database {
	public static final class CreateDB implements BaseColumns {
		public static final String ID = "id";
		public static final String MACADDRESS = "macaddress";
		public static final String DEVICESTATE = "devicestate";
		public static final String LONGITUDE = "longitude";
		public static final String LATITUDE = "latitude";
		public static final String _TABLENAME = "beaconfinder";
		public static final String _CREATE = "create table " + _TABLENAME + "("
				+ ID + " integer primary key, " + MACADDRESS
				+ " text not null , " + DEVICESTATE + " text not null, "
				+ LONGITUDE + " text not null, " + LATITUDE
				+ " text not null );";
		public static final String _INSERT = "insert into " + _TABLENAME
				+ " VALUES " + "(1, '0', '0', '127.123067', '37.388126');";

		// 로그 리스트
		public static final String ID2 = "id";
		public static final String LONGITUDE2 = "longitude";
		public static final String LATITUDE2 = "latitude";
		public static final String ADDRESS = "address";
		public static final String TIME = "time";
		public static final String _TABLENAME2 = "beaconfinderlist";
		public static final String _CREATE2 = "create table " + _TABLENAME2
				+ "(" + ID2 + " integer primary key autoincrement, "
				+ LONGITUDE2 + " text not null , " + LATITUDE2
				+ " text not null, " + ADDRESS + " text not null, " + TIME
				+ " text not null );";
	}
}