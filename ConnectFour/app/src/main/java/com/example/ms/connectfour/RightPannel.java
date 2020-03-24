package com.example.ms.connectfour;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

/**
 * Created by MSİ on 15.02.2018.
 */

public class RightPannel extends RelativeLayout {
	
	private final ConnectFour input;
	
	
	
	
	private RelativeLayout.LayoutParams button_param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	
	
	private RelativeLayout.LayoutParams log_param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	
	private LogPanel logArea;
	
	public RightPannel(Context context, ConnectFour input)
	{
		super(context);
		this.input = input;
		setBackgroundColor(Color.BLUE);
		rightPanel();
	}
	
	
	
	/**
	 * Create a TextArea for stringly game,
	 *        two Buttons for reset and update settings.
	 * Then add them in _rightPanel.
	 */
	private void rightPanel()
	{
		logArea = new LogPanel(getContext());                            //Create stringly game field.
		log_param.addRule(ALIGN_PARENT_TOP);
		log_param.setMargins(0,0, 0, 360);
		this.addView(logArea, log_param);
		
		
		
		
		
		ButtonPanel rightBottom = new ButtonPanel(getContext(), input);           //Create field for reset and settings
		button_param.addRule(ALIGN_PARENT_BOTTOM);
		button_param.addRule(BELOW, logArea.getId());
		button_param.setMargins(0, 40, 0, 0);
		this.addView(rightBottom, button_param);
	}//end of rightPanel
	
	
	public void refreshMap()
	{
		input.refreshMap();
	}
	
	public void update(String in)
	{
		logArea.update(in);
	}
	
	public void attach(String in)
	{
		logArea.attach(in);
	}
}
