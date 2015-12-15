package com.sien.cpluginhost;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		 SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        Date date;
			try {
				date = format.parse("2015/09/10 13:25:07");
				 Calendar cal = Calendar.getInstance();
			        cal.setTime(date);
				System.out.println("-----------" + cal.getTimeInMillis());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	       
	}
}
