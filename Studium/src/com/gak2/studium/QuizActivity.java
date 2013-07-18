package com.gak2.studium;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gak2.studium.EntryReaderContract.StudyEntry;

public class QuizActivity extends Activity {

	protected long subjectId;
	protected String subjectName;
	protected Cursor cursor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Get the subject this came from
		Intent intent = getIntent();
		subjectId = intent.getLongExtra(MainActivity.EXTRA_SUBJ_ID, 0);
		subjectName = intent.getStringExtra(MainActivity.EXTRA_SUBJ_NAME);
		setTitle(subjectName + " Quiz");
	}
	
	protected void onStart() {
		super.onStart();
		EntryReaderDbHelper entryHelper = new EntryReaderDbHelper(this);
		SQLiteDatabase db = entryHelper.getReadableDatabase();
		
		String[] projection = {
				StudyEntry._ID,
				StudyEntry.COLUMN_NAME_SUBJ_ID,
				StudyEntry.COLUMN_NAME_TYPE,
				StudyEntry.COLUMN_NAME_UNIT,
				StudyEntry.COLUMN_NAME_CHAPTER,
				StudyEntry.COLUMN_NAME_ENTRY,
				StudyEntry.COLUMN_NAME_RESPONSE
		};
		
		cursor = db.query(StudyEntry.TABLE_NAME,
				projection,
				StudyEntry.COLUMN_NAME_SUBJ_ID + "=?",
				new String[] {"" + subjectId},
				null, null, "RANDOM()");
		cursor.moveToFirst();
		TextView entryTextView = (TextView) findViewById(R.id.quiz_entry);
		TextView responseTextView = (TextView) findViewById(R.id.quiz_response);
		entryTextView.setText(cursor.getString(5));
		responseTextView.setText(cursor.getString(6));
		db.close();
	}
	
	public void showResponse(View view) {
		TextView responseTextView = (TextView) findViewById(R.id.quiz_response);
		responseTextView.setVisibility(View.VISIBLE);
	}
	
	public void prevEntry(View view) {
		if (cursor.moveToPrevious()) {
			TextView entryTextView = (TextView) findViewById(R.id.quiz_entry);
			TextView responseTextView = (TextView) findViewById(R.id.quiz_response);
			responseTextView.setVisibility(View.INVISIBLE);
			entryTextView.setText(cursor.getString(5));
			responseTextView.setText(cursor.getString(6));
		}
		else {
			TextView responseTextView = (TextView) findViewById(R.id.quiz_response);
			responseTextView.setVisibility(View.INVISIBLE);
			cursor.moveToFirst();
			return;
		}
	}
	
	public void nextEntry(View view) {
		if (cursor.moveToNext()) {
			TextView entryTextView = (TextView) findViewById(R.id.quiz_entry);
			TextView responseTextView = (TextView) findViewById(R.id.quiz_response);
			responseTextView.setVisibility(View.INVISIBLE);
			entryTextView.setText(cursor.getString(5));
			responseTextView.setText(cursor.getString(6));
		}
		else {
			
			finish();
		}
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
