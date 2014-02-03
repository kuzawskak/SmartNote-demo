package com.smartnote_demo.events_menu;
import com.example.smartnote_demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class Calendar extends Activity{
	CalendarView calendar;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.calendar_view);
	calendar = (CalendarView)findViewById(R.id.calendarView1);
	calendar.setOnDateChangeListener(new OnDateChangeListener() {
		
		@Override
		public void onSelectedDayChange(CalendarView view, int year, int month,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			
		}
	});
	 
	
	}
}
	

