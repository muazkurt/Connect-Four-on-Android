package com.example.ms.atry;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.security.InvalidParameterException;
import java.util.Random;

/**
 * Created by MSİ on 14.02.2018.
 */

public class ConnectFour extends RelativeLayout
{
	
	
	/**
	 * Game' s size property.
	 */
	public int _size;                           //
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
	
	
	
	
	
	
	
	
	
	
	/**
	 * Game properties container.
	 * The header panel to show who to move.
	 */
	private TextView _userPanel;
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
		_size = size_input;
		_turn = false;
		_aiopen = ai_info;
		frameCreate();                              //Updates the game frame properties.
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
		if(_size > 0)
		{
			--_size;
			_container.setCoordinateY(_container.history[_container._size][0]);
			_container.setCoordinateX(_container.history[_container._size][1]);
			_container.currentgameCell(_container.history[_container._size][0], _container.history[_size][1]).setCell(0);
			_container.history[_container._size][0] = _container.history[_container._size][1] = 0;
			switchTurn();
			attachLog(String.format("User %d made undo.", (!_turn ? 1 : 2)));
		}
	}
	
	
	

	/**
	 * Update frame properties.
	 */
	private void frameCreate()
	{
		_container = new GamePanel(this, getContext());            //Create a grid panel.
		_userPanel = new TextView(getContext());
		_userPanel.setText(String.format("USER: %d\nPWC: %s", (!_turn ? 1: 2), (!_aiopen ? "true" : "false")));
		_rightPanel = new RightPannel(getContext(), this);                            //Create _rightPanel
		_rightPanel.update(_container.toString());
		
		
		
		
		up_special.addRule(ALIGN_PARENT_LEFT);
		up_special.addRule(ALIGN_PARENT_RIGHT);
		up_special.addRule(ALIGN_PARENT_TOP);
		
		
		
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
	
	
	
	
	
	
	
	
	/**
	 * Takes the Logic not of the parent._turn.
	 */
	public void switchTurn()
	{
		_turn = !_turn;
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
