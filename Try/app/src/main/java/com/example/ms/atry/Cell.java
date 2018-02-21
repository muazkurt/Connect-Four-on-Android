package com.example.ms.atry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

import static com.example.ms.atry.R.attr.buttonStyleSmall;

/**
 * Class Cell @extends Button
 * Created by MSİ on 11.02.2018.
 * @author Muaz Kurt - 151044062
 * @version 1.0.0
 * @since 05.01.2017
 */

@SuppressLint("AppCompatCustomView")
public class Cell extends Button
{
	//Where am i: X
	private int _positionX;
	//Where am i: Y
	private int _positionY;
	//Am i filled, if so who filled me.
	private int _UserInformation;
	
	//Cell(0, 0, user)
	public Cell(int user, Context context)
	{
		this(0, 0, user, context);
	}//end of Cell
	
	//Cell(Y, X, 0)
	public Cell(int Y, int X, Context context)
	{
		this(Y, X, 0, context);
	}//end of Cell
	
	/**
	 * Creates button, then update its porperties.
	 * @param Y Y position.
	 * @param X X position.
	 * @param info user info 0, 1, 2, 3, 4.
	 */
	public Cell(int Y, int X, int info, Context context)
	{
		super(context);
		setTag("" + X);
		setCell(Y, X);
		setCell(info);
	}//end of Cell
	
	/**
	 * Checks two cell's info are same.
	 * @param other Cell will be checked
	 * @return true if both same.
	 */
	protected boolean equals(Cell other)
	{
		return (getInside() == other.getInside());
	}//end of equals
	
	
	/**
	 * Set cell's Y and X as input.
	 * inputs cant be less than 0
	 * @param Y Y position.
	 * @param X X position.
	 */
	private void setCell(int Y, int X)
	{
		if(Y >= 0 && X >= 0)
		{
			_positionX = X;
			_positionY = Y;
		}
		else
			System.err.println("Error while setting Cell Row/Column");
	}//end of setCell
	
	/**
	 * setter for Cell info.
	 * 0: empty cell
	 * 1, 2: users
	 * 3: user 1 won
	 * 4: user 2 won
	 * @param info user information, can be: {0, 1, 2, 3, 4}
	 */
	public void setCell(int info)
	{
		if(info == 0)
			_UserInformation = info;
		else if(info > 0 && info < 5)
		{
			if(_UserInformation == 0)
				_UserInformation = info;
			else if((_UserInformation == 1 && info == 3) || (_UserInformation == 2 && info == 4))
				_UserInformation = info;
			else
				System.err.println("This cell is filled.");
		}
		else
			_UserInformation = info;
		switch(_UserInformation)
		{
			case 0:
				setBackgroundColor(Color.WHITE);
				break;
			case 1:
				setBackgroundColor(Color.RED);
				break;
			case 2:
				setBackgroundColor(Color.BLACK);
				break;
			case 3:
				setBackgroundColor(Color.GREEN);
				break;
			case 4:
				setBackgroundColor(Color.GREEN);
				break;
			default:
				break;
		}
		return;
	}//end of setCell
	
	/**
	 * Get X position.
	 * @return X position.
	 */
	public int getRow()
	{
		return _positionX;
	}//end of getRow
	
	/**
	 * Get Y position.
	 * @return Y position.
	 */
	public int getColumn()
	{
		return _positionY;
	}//end of getColumn
	
	
	/**
	 * Get User information.
	 * @return User information.
	 */
	public int getInside()
	{
		return _UserInformation;
	}//end of getInside
	
	
	/**
	 * Returns it's info as a string.
	 * Return string contents are all integers like _userInformation
	 * @return Filled String by user info
	 */
	public String toString()
	{
		String input;
		switch(getInside())
		{
			case 0:
				input = " ";
				break;
			case 1:
				input = "x";
				break;
			case 2:
				input = "o";
				break;
			case 3:
				input = "X";
				break;
			case 4:
				input = "O";
				break;
			default:
				input = " ";
				break;
		}
		return input;
	}//end of toString
}