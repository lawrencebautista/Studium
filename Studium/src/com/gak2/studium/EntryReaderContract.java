package com.gak2.studium;

import android.provider.BaseColumns;

public final class EntryReaderContract {

	// Protection from accidental instantiation
	public EntryReaderContract() {}
	
	// Defines Table Contents
	public static abstract class StudyEntry implements BaseColumns {
		public static final String TABLE_NAME = "study";
		public static final String COLUMN_NAME_SUBJ_ID = "subject_id";
		public static final String COLUMN_NAME_TYPE = "type";
		public static final String COLUMN_NAME_UNIT = "unit";
		public static final String COLUMN_NAME_CHAPTER = "chapter";
		public static final String COLUMN_NAME_ENTRY = "entry";
		public static final String COLUMN_NAME_RESPONSE = "response";
	}
	
}
