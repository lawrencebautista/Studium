package com.gak2.studium;

import android.provider.BaseColumns;

public final class StatementReaderContract {
	// Protection from accidental instantiation
	public StatementReaderContract() {}
	
	// Defines Table Contents
	public static abstract class StatementEntry implements BaseColumns {
		public static final String TABLE_NAME = "statement";
		public static final String COLUMN_NAME_STATEMENT_ID = "statementid";
		public static final String COLUMN_NAME_CONTENT = "content";
	}

}
