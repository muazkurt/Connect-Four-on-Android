package com.example.ms.atry;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by MSİ on 15.02.2018.
 */

public class LogPanel extends ScrollView
{
	TextView print_area;
	public LogPanel(Context context)
	{
		super(context);
		print_area = new TextView(context);
		print_area.setTextColor(Color.BLACK);
		print_area.setBackgroundColor(Color.WHITE);
		addView(print_area);
	}
	

	public void attach(String input)
	{
		update(String.format("%s%s", print_area.getText(), input));
	}
	
	public void update(String input)
	{
		print_area.setText(input);
	}
}
