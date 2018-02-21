package com.example.ms.atry;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ConnectFour q = new ConnectFour(this);
		setContentView(q);
	}
	
	/**
	 * This updates the header information.
	 *
	 */
	private void updateHeader()
	{}
	
	/**
	 * This updates the fragment's information.
	 *
	 */
	private void updateContent()
	{}
	
	/**
	 * This updates old moves.
	 *
	 */
	private void createHistory()
	{}
	
	/**
	 * This updates right textly game information.
	 *
	 */
	private void rightXO()
	{}
	
	/**
	 * This creates right buttons.
	 * There is 3 buttons
	 * Settings, Restart, Undo
	 */
	private void buttons()
	{}
	
}
