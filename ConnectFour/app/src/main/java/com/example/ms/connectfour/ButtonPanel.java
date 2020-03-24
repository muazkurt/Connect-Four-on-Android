package com.example.ms.connectfour;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by MSİ on 15.02.2018.
 */

public class ButtonPanel extends LinearLayout {
	private final ConnectFour holder;
	public ButtonPanel(Context context, ConnectFour input)
	{
		super(context);
		this.setOrientation(VERTICAL);
		holder = input;
		undo_create();
		restart_create();
		setting_create();
	}
	private void restart_create()
	{
		Button restart = new Button(getContext());
		restart.setOnClickListener(new OnClickListener()
		{                //Add it's action.
			
			@Override
			public void onClick(View event)
			{
				AlertDialog.Builder yes_no = new AlertDialog.Builder(getContext());
				yes_no.setMessage("Are you sure to Restart?").setCancelable(true)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialogInterface, int i)
									{
										holder.refreshMap();                              //Ask user does he/she sure? if YES-- Refresh Map
									}
									
								}
						)
						.setNegativeButton("No", null);
				AlertDialog alert = yes_no.create();
				alert.setTitle("Refresh Map");
				alert.show();
			}
		});
		restart.setText("Restart");
		addView(restart);                                    //Add reset button to field.
	}
	
	private void undo_create()
	{
		Button undo = new Button(getContext());
		undo.setOnClickListener(new OnClickListener(){                //Add it's action.
			
			@Override
			public void onClick(View event)
			{
				AlertDialog.Builder yes_no = new AlertDialog.Builder(getContext());
				yes_no.setMessage("Are you sure to Undo?").setCancelable(true)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialogInterface, int i)
									{
										holder.undo();                              //Ask user does he/she sure? if YES-- Refresh Map
									}
									
								}
						)
						.setNegativeButton("No", null);
				AlertDialog alert = yes_no.create();
				alert.setTitle("Undo");
				alert.show();
			}
		});
		undo.setText("Undo");
		addView(undo);                                    //Add reset button to field.
	}
	
	private void setting_create()
	{
		Button settings = new Button(getContext());
		settings.setOnClickListener(new OnClickListener()
		{                //Add it's action.
			
			@Override
			public void onClick(View event)
			{
				AlertDialog.Builder yes_no = new AlertDialog.Builder(getContext());
				yes_no.setMessage("This will change configuration of the game.\n Are you sure?").setCancelable(true)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialogInterface, int i)
									{
										holder.frameDelete();                              //Ask user does he/she sure? if YES-- Refresh Map
									}
								}
						)
						.setNegativeButton("No", null);
				AlertDialog alert = yes_no.create();
				alert.setTitle("Settings");
				alert.show();
			}
		});
		settings.setText("Settings");
		addView(settings);                                    //Add reset button to field.
		
	}
	
	
}
