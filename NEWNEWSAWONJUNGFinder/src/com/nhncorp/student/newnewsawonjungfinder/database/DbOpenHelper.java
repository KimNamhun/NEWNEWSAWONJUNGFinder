package com.nhncorp.student.newnewsawonjungfinder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {
	private static final String DATABASE_NAME = "beaconfinder.db";
	private static final int DATABASE_VERSION = 1;
	public static SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;
	private Context mCtx;

	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(Database.CreateDB._CREATE);
			db.execSQL(Database.CreateDB._INSERT);
			db.execSQL(Database.CreateDB._CREATE2);
			System.out
					.println("DATABASE CREATE========================================");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS " + Database.CreateDB._TABLENAME);
			db.execSQL("DROP TABLE IF EXISTS " + Database.CreateDB._TABLENAME2);
			onCreate(db);
			System.out
					.println("UPDATE========================================");
		}
	}

	public DbOpenHelper(Context context) {
		this.mCtx = context;
	}

	public DbOpenHelper open() throws SQLException {
		mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null,
				DATABASE_VERSION);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDB.close();
	}

	public long insertColumn(int id, String macaddress, String devicestate,
			String longitude, String latitude) {
		ContentValues values = new ContentValues();
		values.put(Database.CreateDB.ID, id);
		values.put(Database.CreateDB.MACADDRESS, macaddress);
		values.put(Database.CreateDB.DEVICESTATE, devicestate);
		values.put(Database.CreateDB.LONGITUDE, longitude);
		values.put(Database.CreateDB.LATITUDE, latitude);
		return mDB.insert(Database.CreateDB._TABLENAME, null, values);
	}

	public boolean updateColumn(int id, String macaddress, String devicestate,
			String longitude, String latitude) {
		ContentValues values = new ContentValues();
		values.put(Database.CreateDB.ID, id);
		values.put(Database.CreateDB.MACADDRESS, macaddress);
		values.put(Database.CreateDB.DEVICESTATE, devicestate);
		values.put(Database.CreateDB.LONGITUDE, longitude);
		values.put(Database.CreateDB.LATITUDE, latitude);
		return mDB.update(Database.CreateDB._TABLENAME, values, "id=" + id,
				null) > 0;
	}

	public boolean deleteColumn() {
		return mDB.delete(Database.CreateDB._TABLENAME, null, null) > 0;
	}

	public Cursor getMatchName(String name) {
		Cursor c = mDB.rawQuery("select * from beaconfinder where name=" + "'"
				+ name + "'", null);
		return c;
	}

	public Cursor getAll() {
		return mDB.query(Database.CreateDB._TABLENAME, null, null, null, null,
				null, null);
	}
}