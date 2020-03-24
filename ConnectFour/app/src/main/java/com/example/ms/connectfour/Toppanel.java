package com.example.ms.connectfour;


import android.content.Context;
import android.os.CountDownTimer;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by MSİ on 23.02.2018.
 */

public class Toppanel extends LinearLayout {
	private final TextView _userPanel;
	private CountDownTimer timer;
	private ConnectFour boss;
	public Toppanel( Context context, ConnectFour random_player)
	{
		super(context);
		boss = random_player;
		this.setOrientation(HORIZONTAL);
		_userPanel = new TextView(getContext());
		this.addView(_userPanel);
	}
	public Toppanel(int time_count, Context context, ConnectFour random_player)
	{
		super(context);
		boss = random_player;
		this.setOrientation(HORIZONTAL);
		_userPanel = new TextView(getContext());
		this.addView(_userPanel);
		final TextView timer_place = new TextView(getContext());
		timer = new CountDownTimer(time_count * 1000, 1000) {
			
			public void onTick(long millisUntilFinished) {
				timer_place.setText("seconds remaining: " + millisUntilFinished / 1000);
			}
			public void onFinish() {
				boss.random_move();
			}
		}.start();
		addView(timer_place);
	}
	public void setText(String input)
	{
		_userPanel.setText(input);
	}
	
	public void refresh_timer()
	{
		timer.start();
	}
	
	public void stop_timer()
	{
		timer.cancel();
	}
}

