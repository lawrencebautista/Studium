package com.gak2.studium;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EntryReaderDbHelper extends SQLiteOpenHelper {

	// Used 
	private static final String LONG_TYPE = " INTEGER";
	private static final String TYPE_TYPE = " TEXT";
	private static final String UNIT_TYPE = " INTEGER";
	private static final String CHAPTER_TYPE = " INTEGER";
	private static final String ENTRY_TYPE = " TEXT";
	private static final String RESPONSE_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES =
	    "CREATE TABLE " + EntryReaderContract.StudyEntry.TABLE_NAME + " (" +
	    EntryReaderContract.StudyEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + 
	    EntryReaderContract.StudyEntry.COLUMN_NAME_SUBJ_ID + LONG_TYPE + COMMA_SEP +
	    EntryReaderContract.StudyEntry.COLUMN_NAME_TYPE + TYPE_TYPE + COMMA_SEP +
	    EntryReaderContract.StudyEntry.COLUMN_NAME_UNIT + UNIT_TYPE + COMMA_SEP +
	    EntryReaderContract.StudyEntry.COLUMN_NAME_CHAPTER + CHAPTER_TYPE + COMMA_SEP +
	    EntryReaderContract.StudyEntry.COLUMN_NAME_ENTRY + ENTRY_TYPE + COMMA_SEP +
	    EntryReaderContract.StudyEntry.COLUMN_NAME_RESPONSE + RESPONSE_TYPE +
	    " )";

	private static final String SQL_DELETE_ENTRIES =
	    "DROP TABLE IF EXISTS " + EntryReaderContract.StudyEntry.TABLE_NAME;
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "entries.db";
	
	public EntryReaderDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);

	}

	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
	
	@Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
