package com.example.ms.connectfour;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.InvalidParameterException;

/**
 * Created by MSİ on 14.02.2018.
 */

public class ConnectFour extends RelativeLayout
{
	
	
	/**
	 * Game' s size property.
	 */
	public static int _size;                           //
	/**
	 * Turn changer. If false -- 1st user.
	 *                  true  -- 2nd user.
	 */
	public boolean _turn;                      // -- to static
	/**
	 * Game property, does game pwp or pwc?
	 * If true-- PWP
	 *    false-- PWC
	 */
	public boolean _aiopen;                    // -- if true, ai closed.
	
	
	public int _time;
	
	
	
	
	
	
	
	/**
	 * Game properties container.
	 * The header panel to show who to move.
	 */
	private Toppanel _userPanel;
	/**
	 * Creats a panel to contain Cell objects.
	 */
	private GamePanel _container;                          // -- Button Panel
	/**
	 * Right panel of frame.
	 */
	private RightPannel _rightPanel;
	
	
	
	
	
	
	
	
	private RelativeLayout.LayoutParams rp_special = new RelativeLayout.LayoutParams(240, ViewGroup.LayoutParams.MATCH_PARENT);
	
	
	private RelativeLayout.LayoutParams cp_special = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	
	
	private RelativeLayout.LayoutParams up_special = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
	
	
	
	
	
	
	
	//ConnectFour(10, false)
	public ConnectFour(Context context)
	{
		this(10, context);
	}
	
	/**
	 * ConnectFour(size_input, false)
	 * @param size_input property of game size.
	 */
	public ConnectFour(int size_input, Context context)
	{
		this(size_input, false, context);
	}
	
	
	/**
	 * @param size_input property of game size.
	 * @param ai_info  property of game PWP or PWC.
	 * @exception InvalidParameterException if size_input is less than 4.
	 * Game won't be playable then.
	 */
	public ConnectFour(int size_input, boolean ai_info, Context context) throws InvalidParameterException
	{
		super(context);                      //Name of the frame.
		this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		if(size_input <= 4)
			throw new InvalidParameterException();
		_size = 0;
		_turn = false;
		_aiopen = ai_info;
		try
		{
			settings_update();
		}
		catch (Throwable k)
		{
			System.err.println("ERROR COUSE: " + k.getMessage());
			System.err.println("ERROR Info" + k.getCause());
		}
		//	frameCreate();                              //Updates the game frame properties.
	}//end of ConnectFour.
	
	
	
	
	
	
	/**
	 * Empty all Cells and frame componnent's index.
	 * Give the next turn to 1st user.
	 */
	public void refreshMap()
	{
		_container.refreshMap();
		_userPanel.setText(String.format("USER: %d\nPWC: %s", (!_turn ? 1: 2), (!_aiopen ? "true" : "false")));
		updateLog(_container.toString());
	}//end of refreshMap
	
	
	
	public void undo()
	{
		if(_container.undo())
		{
			switchTurn();
			attachLog(String.format("User %d made undo.\n", (_turn ? 1 : 2)));
			if(!_aiopen && _turn)
			{
				_container.playPC();
			}
		}
	}
	
	public void random_move()
	{
		if(!_container.WinSituation() && !_container.GameDraw())
		{
			_container.playPC();
			if (!_aiopen)
				_container.playPC();
		}
		else
			refreshMap();
	}
	
	
	/**
	 * Update frame properties.
	 */
	private void frameCreate()
	{
		_container = new GamePanel(this, getContext());            //Create a grid panel.
		if(_time > 0)
			_userPanel = new Toppanel(_time, getContext(),this);
		else
			_userPanel = new Toppanel(getContext(),this);
		_userPanel.setText(String.format("USER: %d\nPWC: %s", (!_turn ? 1: 2), (!_aiopen ? "true" : "false")));
		_rightPanel = new RightPannel(getContext(), this);                            //Create _rightPanel
		_rightPanel.update(_container.toString());
		
		
		
		
		
		
		up_special.addRule(ALIGN_PARENT_RIGHT);
		up_special.addRule(ALIGN_PARENT_TOP);
		up_special.addRule(ALIGN_PARENT_LEFT);
		addView(_userPanel, up_special);                                  //add Properties to north
		
		
		
		cp_special.addRule(ALIGN_PARENT_LEFT);
		cp_special.addRule(ALIGN_PARENT_BOTTOM);
		cp_special.addRule(BELOW, _userPanel.getId());
		cp_special.addRule(LEFT_OF, _rightPanel.getId());
		cp_special.setMargins(0, 120, 240, 0);
		addView(_container, cp_special);                               //add game to center
		
		
		rp_special.addRule(ALIGN_PARENT_RIGHT);
		rp_special.addRule(BELOW, _userPanel.getId());
		rp_special.addRule(RIGHT_OF, _container.getId());
		rp_special.setMargins(0,120,0,0);
		addView(_rightPanel, rp_special);                                //add _rightPanel to east
	}//end of frameCreate
	
	
	
	public void frameDelete()
	{
		detachAllViewsFromParent();
		if(_time != 0)_userPanel.stop_timer();
		settings_update();
	}
	
	
	
	private void settings_update()
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("Please give in 5 to 40 integer for size.");
		
		final LinearLayout vertical_set = new LinearLayout(getContext());
		vertical_set.setOrientation(LinearLayout.VERTICAL);
		builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ConnectFour._size = Integer.valueOf(((TextView) vertical_set.getChildAt(0)).getText().toString());
				makevisible();
			}
		});
		
		
		
		
		{
			TextView num_board = new TextView(getContext());
			LinearLayout horisontal_set = new LinearLayout(getContext());
			horisontal_set.setOrientation(LinearLayout.HORIZONTAL);
			num_board.setText("0");
			
			vertical_set.addView(num_board, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			
			
			Button display_keyboard = new Button(getContext());
			display_keyboard.setText("DEL");
			display_keyboard.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					((TextView) vertical_set.getChildAt(0)).setText("0");
					((TextView) vertical_set.getChildAt(0)).setBackgroundColor(Color.WHITE);
				}
			});
			
			horisontal_set.addView(display_keyboard, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			
			
			vertical_set.addView(horisontal_set, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			horisontal_set = new LinearLayout(getContext());
			horisontal_set.setOrientation(LinearLayout.HORIZONTAL);
			
			
			for (int i = 0; i < 10; ++i) {
				display_keyboard = new Button(getContext());
				if (i < 9) {
					display_keyboard.setTag(i + 1);
					display_keyboard.setText(String.valueOf(i + 1));
					display_keyboard.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (Integer.valueOf(((TextView) vertical_set.getChildAt(0)).getText().toString()) < 40)
								((TextView) vertical_set.getChildAt(0)).setText(String.format("%d", Integer.valueOf(((TextView) vertical_set.getChildAt(0)).getText().toString()) * 10 + (Integer) view.getTag()));
						}
					});
					horisontal_set.addView(display_keyboard, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					if (horisontal_set.getChildCount() == 3)
					{
						vertical_set.addView(horisontal_set, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
						horisontal_set = new LinearLayout(getContext());
						horisontal_set.setOrientation(LinearLayout.HORIZONTAL);
					}
				} else {
					display_keyboard.setTag(0);
					display_keyboard.setText(String.valueOf(0));
					display_keyboard.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (Integer.valueOf(((TextView) vertical_set.getChildAt(0)).getText().toString()) < 40)
								if (Integer.valueOf(((TextView) vertical_set.getChildAt(0)).getText().toString()) != 0)
									((TextView) vertical_set.getChildAt(0)).setText(String.format("%d", Integer.valueOf(((TextView) vertical_set.getChildAt(0)).getText().toString()) * 10 + (Integer) view.getTag()));
						}
					});
					vertical_set.addView(display_keyboard, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				}
			}
			
			{
				final RadioGroup ai_ask = new RadioGroup(getContext());
				ai_ask.setOrientation(LinearLayout.HORIZONTAL);
				TextView quest = new TextView(getContext());
				quest.setText("Do you want to play with AI?");
				ai_ask.addView(quest, new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				RadioButton yes = new RadioButton(getContext());
				yes.setText("Yes");
				ai_ask.addView(yes);
				RadioButton no = new RadioButton(getContext());
				no.setText("No");
				ai_ask.addView(no);
				ai_ask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						if (radioGroup.getChildAt(1).getId() == i)
							_aiopen = false;
						else
							_aiopen = true;
					}
				});
				vertical_set.addView(ai_ask);
			}
			{
				final RadioGroup timer_ask = new RadioGroup(getContext());
				timer_ask.setOrientation(LinearLayout.HORIZONTAL);
				TextView quest = new TextView(getContext());
				quest.setText("Select the timer count");
				timer_ask.addView(quest, new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				for(int i = 0; i < 5; ++i)
				{
					RadioButton temp = new RadioButton(getContext());
					temp.setText(String.format("%d", i * 5));
					temp.setTag(String.format("%d", i * 5));
					timer_ask.addView(temp);
				}
				timer_ask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i)
					{
						_time = new Integer(((RadioButton) radioGroup.findViewById(i)).getText().toString());
					}
				});
				vertical_set.addView(timer_ask);
			}
		}
		
		
		
		
		builder.setView(vertical_set);
		
		
		final AlertDialog hider = builder.create();
		((TextView) vertical_set.getChildAt(0)).addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			
			}
			
			@Override
			public void afterTextChanged(Editable editable) {
				if(Integer.valueOf(editable.toString()) > 4 && Integer.valueOf(editable.toString()) < 41)
				{
					hider.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(VISIBLE);
					((TextView) vertical_set.getChildAt(0)).setBackgroundColor(Color.WHITE);
				}
				else
				{
					hider.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(INVISIBLE);
					((TextView) vertical_set.getChildAt(0)).setBackgroundColor(Color.RED);
				}
			}
		});
		hider.show();
		hider.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(INVISIBLE);
		
	}
	
	
	private  void makevisible()
	{
		frameCreate();
		_container.setVisibility(VISIBLE);
		_rightPanel.setVisibility(VISIBLE);
		_userPanel.setVisibility(VISIBLE);
	}
	
	/**
	 * Takes the Logic not of the parent._turn.
	 */
	public void switchTurn()
	{
		_turn = !_turn;
		if(_time != 0)
			_userPanel.refresh_timer();
		_userPanel.setText(String.format("USER: %d\nPWC: %s", (!_turn ? 1: 2), (!_aiopen ? "true" : "false")));
	}//end of switchTurn.
	
	
	
	
	public void updateLog(String input)
	{
		_rightPanel.update(input);
	}
	
	public void attachLog(String input)
	{
		_rightPanel.attach(input);
	}
}
