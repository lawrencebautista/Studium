package com.gak2.studium;

import android.provider.BaseColumns;

public final class SubjectReaderContract {

	// Protection from accidental instantiation
	public SubjectReaderContract() {}
	
	// Defines Table Contents
	public static abstract class SubjectEntry implements BaseColumns {
		public static final String TABLE_NAME = "subject";
		public static final String COLUMN_NAME_TITLE = "title";
	}
	
	


}
