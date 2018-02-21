package com.example.ms.atry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import java.net.ConnectException;
import java.util.Random;

/**
 * Created by MSİ on 15.02.2018.
 */

public class GamePanel extends ScrollView
{
	private final ConnectFour parent;
	
	public Cell [][] _map;
	private GridLayout holder;
	
	public int [][] history;
	public int _size = 0;
	
	
	public GamePanel(ConnectFour input, Context context)
	{
		super(context);
		history = new int [input._size * input._size][Y_X];
		parent = input;
		HorizontalScrollView temp = new HorizontalScrollView(context);
		holder = new GridLayout(context);
		holder.setRowCount(parent._size);
		holder.setColumnCount(parent._size);
		createArea();
		temp.addView(holder);
		this.addView(temp);
	}
	
	/**
	 * Current map' s constant.
	 */
	private final int Y_X = 2;                  //
	/**
	 * Last moved position info.
	 * 0 index = Y position.
	 * 1 index = X position.
	 */
	private int [] coordinate = new int [Y_X];  //
	
	
	
	/**
	 * Geter for last moved vertical position.
	 * @return Last moved Vertical Position.
	 */
	private int getCoordinateY()
	{
		return coordinate[0];
	}//end of getCoordinateY.
	/**
	 * Getter for last moved horisontal position.
	 * @return Last moved Horisontal position.
	 */
	private int getCoordinateX()
	{
		return coordinate[1];
	}// end of getCoordinateX.
	
	
	/**
	 * Setter for next moving vertical position.
	 * @param input moving Vertical Position.
	 */
	public void setCoordinateY(int input)
	{
		coordinate[0] = input;
	}//end of setCoordinateY.
	
	
	/**
	 * Setter for next moving horisontal position.
	 * @param input moving Horisontal position.
	 */
	public void setCoordinateX(int input)
	{
		coordinate[1] = input;
	}//end of setCoordinateX.
	
	
	/**
	 * Getterf for the given index's Cell information.
	 * @param y Vertical index.
	 * @param x Horisontal index
	 * @return game fields (y,x) index.
	 */
	public Cell currentgameCell(int y, int x)
	{
		return _map[y][x];
	}//end of currentgameCell.
	
	
	/**
	 * @return current Turn.
	 */
	public boolean currentTurn() { return parent._turn; }
	
	
	/**
	 * @return current game's AI property.
	 */
	public boolean currentAI() { return parent._aiopen; }
	
	
	public void update_history()
	{
		history[_size][0] = getCoordinateY();
		history[_size][1] = getCoordinateX();
		++this._size;
	}
	
	
	
	/**
	 * Empty all Cells and frame componnent's index.
	 * Give the next turn to 1st user.
	 */
	public void refreshMap()
	{
		for(int i = 0; i < parent._size; ++i)
			for(int j = 0; j < parent._size; ++j)
				currentgameCell(i, j).setCell(0);
		parent._turn = false;
	}//end of refreshMap
	
	
	
	
	
	
	
	/**
	 * Creates cell objects to play game on.
	 * Need to know parent._size property.
	 * Creates Cells and add them into _container.
	 */
	private void createArea()
	{
		parent._turn = false;
		_map = new Cell [parent._size][];                                          //Create Cell Buttons.
		for(int i = 0; i < parent._size; ++i)
		{
			_map[i] = new Cell [parent._size];
			for(int j = 0; j < parent._size; ++j)
			{
				_map[i][j] = new Cell(i, j, 0, getContext());
				_map[i][j].setOnClickListener( new OnClickListener(){
					
					@Override
					public void onClick(View event)
					{
						if(!WinSituation() && !GameDraw())                  //Check game finished? if not move
						{
							setCoordinateX(new Integer((String)event.getTag()));
							int Y = PositionY();                            //Declare next move.
							if(Y > -1)                                      //If playable move.
							{
								setCoordinateY(Y);
								playUser();                                 //Move and update turn
								if(!WinSituation() && !GameDraw())          //Check game finished? if not move
								{
									if(!currentAI())                //If pwp wait next user to make action
										playPC();                          //If pwc make pc to move.
								}
								else parent.refreshMap();                          //Game finished: REfresh map
							}
						}
						else parent.refreshMap();                                  //Game finished: Refresh Map.
					}
				});
				holder.addView(_map[i][j]);                                 //Add buttons to game panel.
			}
		}
	}//end of createArea
	
	
	
	
	
	/**
	 * Takes next moved position and searches over it Vertical way.
	 * @return -1 for no such position to move, an unsigned int less than parent._size othervise
	 */
	public int  PositionY()
	{
		if(currentgameCell(0, getCoordinateX()).getInside() < 1)
		{
			int i = parent._size - 1;
			for(; currentgameCell(i, getCoordinateX()).getInside() > 0 && i > 0; --i);
			if(currentgameCell(i, getCoordinateX()).getInside() == 0)
				return i;
		}
		return -1;
	}//end of PositionY
	
	/**
	 * Takes next move position and updates it by parent._turn.
	 */
	public void playUser()
	{
		if(currentTurn())
			currentgameCell(getCoordinateY(),getCoordinateX()).setCell(2);
		else
			currentgameCell(getCoordinateY(),getCoordinateX()).setCell(1);
		update_history();
		parent.switchTurn();
		parent.attachLog(this.toString());
		return;
	}//end of playUser
	
	/**
	 * Checks all possible finishes from last moved position,
	 * if there is not, looks if it can finish?
	 * if not then play randomly.
	 */
	private void playPC()
	{
		int i = 0;
		boolean PositionFound = false;
		while(!PositionFound)
		{
			switch(i)
			{
				//Watch out for Up to down.
				case 0:
					if(Up_Down() > 2 && getCoordinateY() > 0 &&
							currentgameCell(getCoordinateX(), getCoordinateY() - 1).getInside() == 0)
					{
						PositionFound = true;
						setCoordinateY(getCoordinateY() - 1);
					}
					
					
					break;
				//	Watch out for right down + left up and if it is possible to win for user 1,
				//		then breake it by putting right down .coordinate.
				case 1:
					if(getCoordinateY() == parent._size - 1 || getCoordinateX() == parent._size - 1);
					else
					{
						int right_temp = Right_Down();
						if(right_temp + Left_Up() - 1 > 2)
						{
							if((getCoordinateY() + right_temp < parent._size) && (getCoordinateX() + right_temp < parent._size))
							{
								setCoordinateX(getCoordinateX() + right_temp);
								if(PositionY() == (getCoordinateY() + right_temp))
								{
									setCoordinateY(getCoordinateY() + right_temp);
									PositionFound = true;
								}
								else
									setCoordinateX(getCoordinateX() - right_temp);
							}
						}
					}
					
					
					break;
				//	Watch out for right up + left down and if it is possible to win for user 1,
				//		then breake it by putting right up .coordinate.
				case 2:
					if(getCoordinateY() == 0 || getCoordinateX() == parent._size - 1);
					else
					{
						int right_temp = Right_Up();
						if(right_temp + Left_Down() - 1 > 2)
						{
							if((getCoordinateY() - right_temp >= 0) && getCoordinateX() + right_temp < parent._size)
							{
								setCoordinateX(getCoordinateX() + right_temp);
								if(PositionY() == (getCoordinateY() - right_temp))
								{
									setCoordinateY(getCoordinateY() - right_temp);
									PositionFound = true;
								}
								else
									setCoordinateX(getCoordinateX() - right_temp);
							}
						}
					}
					
					
					break;
				//	Watch out for left down + right up and if it is possible to win for user 1,
				//		then breake it by putting left down .coordinate.
				case 3:
					if(getCoordinateY() == parent._size - 1 || getCoordinateX() == 0);
					else
					{
						int left_temp = Left_Down();
						if(left_temp + Right_Up() - 1 > 2)
						{
							if((getCoordinateY() + left_temp < parent._size) && (getCoordinateX() - left_temp >= 0))
							{
								setCoordinateX(getCoordinateX() - left_temp);
								if(PositionY() == (getCoordinateY() + left_temp))
								{
									setCoordinateY(getCoordinateY() + left_temp);
									PositionFound = true;
								}
								else
									setCoordinateX(getCoordinateX() + left_temp);
							}
						}
					}
					
					
					break;
				//	Watch out for left up + right down and if it is possible to win for user 1,
				//		then breake it by putting left up .coordinate.
				case 4:
					if(getCoordinateY() == 0 || getCoordinateX() == 0);
					else
					{
						int left_temp = Left_Up();
						if(left_temp + Right_Down() - 1 > 2)
						{
							if((getCoordinateY() - left_temp >= 0) && (getCoordinateX() - left_temp >= 0))
							{
								setCoordinateX(getCoordinateX() - left_temp);
								if(PositionY() == (getCoordinateY() - left_temp))
								{
									setCoordinateY(getCoordinateY() - left_temp);
									PositionFound = true;
								}
								else
									setCoordinateX(getCoordinateX() + left_temp);
							}
						}
					}
					
					
					break;
				
				//	Watch out for right + left  and if it is possible to win for user 1,
				//		then breake it by putting right .coordinate.
				case 5:
					if(getCoordinateX() == parent._size - 1);
					else
					{
						int right_temp = To_Right();
						if(right_temp + To_Left() - 1 > 2)
						{
							if(getCoordinateX() + right_temp < parent._size)
							{
								setCoordinateX(getCoordinateX() + right_temp);
								if(getCoordinateY() == PositionY())
								{
									PositionFound = true;
								}
								else
									setCoordinateX(getCoordinateX() - right_temp);
							}
						}
					}
					
					
					break;
				
				//	Watch out for left + right and if it is possible to win for user 1,
				//		then breake it by putting left .coordinate.
				case 6:
					if(getCoordinateX() == 0);
					else
					{
						int left_temp = To_Left();
						if(left_temp + To_Right() - 1 > 2)
						{
							if(getCoordinateX() - left_temp >= 0)
							{
								setCoordinateX(getCoordinateX() - left_temp);
								if(getCoordinateY() == PositionY())
								{
									PositionFound = true;
								}
								else
									setCoordinateX(getCoordinateX() + left_temp);
							}
						}
					}
					
					
					break;
				
				//	Search that if there is a possible stack .coordinate in map for User 2 (AI) side.
				case 7:
					for(setCoordinateX(0), setCoordinateY(PositionY());
					    !PositionFound && getCoordinateX() < parent._size - 1
							    && getCoordinateY() >= 0;
					    setCoordinateX(getCoordinateX() + 1), setCoordinateY(PositionY()))
					{
						if(getCoordinateY() + 1 < parent._size)
						{
							setCoordinateY(getCoordinateY() + 1);
							if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
								if(Up_Down() > 1)
									PositionFound = true;
							setCoordinateY(getCoordinateY() - 1);
							// The way like '\' if i can check right down
							if(!PositionFound && getCoordinateX() + 1 < parent._size)
							{
								setCoordinateY(getCoordinateY() + 1);
								setCoordinateX(getCoordinateX() + 1);
								int right_temp = 0;
								if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
									right_temp = Right_Down();
								setCoordinateX(getCoordinateX() - 1);
								setCoordinateY(getCoordinateY() - 1);
								if(getCoordinateY() - 1 >= 0 && getCoordinateX() - 1 >= 0)
								{
									setCoordinateY(getCoordinateY() - 1);
									setCoordinateX(getCoordinateX() - 1);
									if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
										right_temp += Left_Up();
									setCoordinateX(getCoordinateX() + 1);
									setCoordinateY(getCoordinateY() + 1);
								}
								if(right_temp > 1)
									PositionFound = true;
							}
							// The way like '/' if i can check left down
							if(!PositionFound && getCoordinateX() - 1 >= 0)
							{
								setCoordinateY(getCoordinateY() + 1);
								setCoordinateX(getCoordinateX() - 1);
								int left_temp = 0;
								if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
									left_temp = Left_Down();
								setCoordinateX(getCoordinateX() + 1);
								setCoordinateY(getCoordinateY() - 1);
								if(getCoordinateY() - 1 >= 0 && getCoordinateX() + 1 < parent._size)
								{
									setCoordinateY(getCoordinateY() - 1);
									setCoordinateX(getCoordinateX() + 1);
									if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
										left_temp += Right_Up();
									setCoordinateX(getCoordinateX() - 1);
									setCoordinateY(getCoordinateY() + 1);
								}
								if(left_temp > 1)
									PositionFound = true;
							}
							
						}
						
						if(!PositionFound && getCoordinateY() - 1 >= 0)
						{
							if(getCoordinateX() + 1 < parent._size)
							{
								setCoordinateY(getCoordinateY() - 1);
								setCoordinateX(getCoordinateX() + 1);
								int right_temp = 0;
								if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
									right_temp = Right_Up();
								setCoordinateX(getCoordinateX() - 1);
								setCoordinateY(getCoordinateY() + 1);
								if(getCoordinateY() + 1 < parent._size && getCoordinateX() - 1 >= 0)
								{
									setCoordinateY(getCoordinateY() + 1);
									setCoordinateX(getCoordinateX() - 1);
									if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
										right_temp += Left_Down();
									setCoordinateX(getCoordinateX() + 1);
									setCoordinateY(getCoordinateY() - 1);
								}
								if(right_temp > 1)
									PositionFound = true;
							}
							if(getCoordinateX() - 1 >= 0)
							{
								setCoordinateY(getCoordinateY() - 1);
								setCoordinateX(getCoordinateX() - 1);
								int left_temp = 0;
								if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
									left_temp = Left_Up();
								setCoordinateX(getCoordinateX() + 1);
								setCoordinateY(getCoordinateY() + 1);
								if(getCoordinateY() + 1 < parent._size && getCoordinateX() + 1 < parent._size)
								{
									setCoordinateY(getCoordinateY() + 1);
									setCoordinateX(getCoordinateX() + 1);
									if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
										left_temp += Right_Down();
									setCoordinateX(getCoordinateX() - 1);
									setCoordinateY(getCoordinateY() - 1);
								}
								if(left_temp > 1)
									PositionFound = true;
							}
							
						}
						
						if(!PositionFound && getCoordinateX() + 1 < parent._size)
						{
							setCoordinateX(getCoordinateX() + 1);
							int right_temp = 0;
							
							
							if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
								right_temp = To_Right();
							setCoordinateX(getCoordinateX() - 1);
							if(getCoordinateX() - 1 >= 0)
							{
								
								setCoordinateX(getCoordinateX() - 1);
								if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
									right_temp += To_Left();
								setCoordinateX(getCoordinateX() + 1);
								
							}
							if(right_temp > 1)
								PositionFound = true;
							
							
							
						}
						if(!PositionFound && getCoordinateX() - 1 >= 0)
						{
							setCoordinateX(getCoordinateX() - 1);
							int left_temp = 0;
							if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
								left_temp = To_Left();
							setCoordinateX(getCoordinateX() + 1);
							if(getCoordinateX() + 1 < parent._size)
							{
								setCoordinateX(getCoordinateX() + 1);
								if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() == 2)
									left_temp = To_Right();
								setCoordinateX(getCoordinateX() - 1);
							}
							if(left_temp > 1)
								PositionFound = true;
						}
					}
					
					break;
				//Playing randomise.
				case 8:
					int temp;
					Random random_gen = new Random();
					temp =  random_gen.nextInt(parent._size);
					if(temp < 0)
						temp *= -1;
					setCoordinateX(temp);
					setCoordinateY(PositionY());
					if(getCoordinateY() < 0)
						--i;
					else
						PositionFound = true;
					break;
				default:
					break;
			}
			if(PositionFound)
				if(getCoordinateY() < 0 ||
						getCoordinateX() > parent._size ||
						getCoordinateX() < 0 ||
						getCoordinateY() > parent._size)
					PositionFound = false;
			++i;
		}
		currentgameCell(getCoordinateY(),getCoordinateX()).setCell(2);
		update_history();
		parent.attachLog(this.toString());
		parent.switchTurn();
	}//end of playPC
	
	
	/**
	 * Watches last moved position and searches vertically.
	 * @return Count of Stack.
	 */
	private int  Up_Down()
	{
		int i = 1;
		boolean Legal = true;
		while(Legal && getCoordinateY() + i < parent._size && currentgameCell(getCoordinateY(), getCoordinateX()).getInside() != -1)
		{
			if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() != currentgameCell(getCoordinateY() + i, getCoordinateX()).getInside())
				Legal = false;
			else
				++i;
		}
		return i;
	}//end of Up_Down
	
	/**
	 * Watches from last moved position as Right+Down way.
	 * @return Count of stack
	 */
	private int  Right_Down()
	{
		int i = 1;
		if(getCoordinateY() < parent._size - 1 || getCoordinateX() < parent._size - 1)
		{
			boolean Legal = true;
			while(Legal && getCoordinateY() + i < parent._size && getCoordinateX() + i < parent._size)
			{
				if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() != currentgameCell(getCoordinateY() + i, getCoordinateX() + i).getInside())
					Legal = false;
				else
					++i;
			}
		}
		return i;
	}//end of Right_Down
	
	/**
	 * Watches from last moved position as Right+UP way.
	 * @return Count of stack
	 */
	private int  Right_Up()
	{
		int i = 1;
		if(getCoordinateY() > 0 || getCoordinateX() < parent._size - 1)
		{
			boolean Legal = true;
			while(Legal && getCoordinateY() - i >= 0 && getCoordinateX() + i < parent._size)
			{
				if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() != currentgameCell(getCoordinateY() - i, getCoordinateX() + i).getInside())
					Legal = false;
				else
					++i;
			}
		}
		return i;
	}//end of Right_Up
	
	/**
	 * Watches from last moved position as Left+Down way.
	 * @return Count of stack
	 */
	private int  Left_Down()
	{
		int i = 1;
		if(getCoordinateY() < parent._size - 1 || getCoordinateX() > 0)
		{
			boolean Legal = true;
			while(Legal && getCoordinateY() + i < parent._size && getCoordinateX() - i >= 0)
			{
				if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() != currentgameCell(getCoordinateY() + i, getCoordinateX() - i).getInside())
					Legal = false;
				else
					++i;
			}
		}
		return i;
	}//end of Left_Down
	
	/**
	 * Watches from last moved position as Left+UP way.
	 * @return Count of stack
	 */
	private int  Left_Up()
	{
		int i = 1;
		if(getCoordinateY() > 0 || getCoordinateX() > 0)
		{
			boolean Legal = true;
			while(Legal && getCoordinateY() - i >= 0 && getCoordinateX() - i >= 0)
			{
				if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() != currentgameCell(getCoordinateY() - i, getCoordinateX() - i).getInside())
					Legal = false;
				else
					++i;
			}
		}
		return i;
	}//end of Left_Up
	
	/**
	 * Watches from last moved position as right way.
	 * @return Count of stack
	 */
	private int  To_Right()
	{
		int i = 1;
		if(getCoordinateX() <= parent._size - 1)
		{
			boolean Legal = true;
			while(Legal && getCoordinateX() + i < parent._size)
			{
				if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() != currentgameCell(getCoordinateY(), getCoordinateX() + i).getInside())
					Legal = false;
				else
					++i;
			}
		}
		return i;
	}//end of To_Right
	
	/**
	 * Watches from last moved position as left way.
	 * @return Count of stack
	 */
	private int  To_Left()
	{
		int i = 1;
		if(getCoordinateX() >= 1)
		{
			boolean Legal = true;
			while(Legal && getCoordinateX() - i >= 0)
			{
				if(currentgameCell(getCoordinateY(), getCoordinateX()).getInside() != currentgameCell(getCoordinateY(), getCoordinateX() - i).getInside())
					Legal = false;
				else
					++i;
			}
		}
		return i;
	}//end of To_Left
	
	/**
	 * Checks is there a stack that more than 3 size.
	 * If so updates the cells of game.
	 * @return true if somebody won.
	 */
	@SuppressLint("DefaultLocale")
	private boolean  WinSituation()
	{
		int filler = 0;
		boolean win = false;
		filler = currentgameCell(getCoordinateY(), getCoordinateX()).getInside();
		if(filler != 0)
		{
			int     y = 0,
					x = 0;
			if((y = Up_Down()) >= 4)
			{
				for(y -= 1; y > 0; --y) currentgameCell(getCoordinateY() + y, getCoordinateX()).setCell(filler + 2);
				win = true;
			}
			else
			{
				x = Left_Up();
				y = Right_Down();
				if(x + y - 1 >= 4)
				{
					for(y -= 1; y > 0; --y) currentgameCell(getCoordinateY() + y, getCoordinateX() + y).setCell(filler + 2);
					for(x -= 1; x > 0; --x) currentgameCell(getCoordinateY() - x, getCoordinateX() - x).setCell(filler + 2);
					win = true;
				}
				else
				{
					x = Left_Down();
					y = Right_Up();
					if(x + y - 1 >= 4)
					{
						for(y -= 1; y > 0; --y) currentgameCell(getCoordinateY() - y, getCoordinateX() + y).setCell(filler + 2);
						for(x -= 1; x > 0; --x) currentgameCell(getCoordinateY() + x, getCoordinateX() - x).setCell(filler + 2);
						win = true;
					}
					else
					{
						x = To_Right();
						y = To_Left();
						if(x + y - 1 >= 4)
						{
							for(y -= 1; y > 0; --y) currentgameCell(getCoordinateY(), getCoordinateX() - y).setCell(filler + 2);
							for(x -= 1; x > 0; --x) currentgameCell(getCoordinateY(), getCoordinateX() + x).setCell(filler + 2);
							win = true;
						}
					}
				}
			}
			if(win) currentgameCell(getCoordinateY(), getCoordinateX()).setCell(filler + 2);
		}
		if(win) parent.updateLog(String.format("User %d %s", (parent._turn ? 1 : 2), "won\n"));
		return win;
	}//end of WinSituation
	
	/**
	 * Checks all top positions, if there is no empty place
	 * then there is no place to move. Game finished as draw.
	 * @return true if game is draw.
	 */
	private boolean GameDraw()
	{
		boolean noplace = true;
		for(int i = 0; i < parent._size && noplace; ++i)
			if(currentgameCell(0, i).getInside() == 0)
				noplace = false;
		if(noplace)
			parent.updateLog( "Game finished as draw\n" );
		return noplace;
	}//end of GameDraw
	
	
	/**
	 * Checks if two objects are same or not.
	 * @param other the object that will be chacked.
	 * @return if both are equal, true.
	 */
	public boolean equals(GamePanel other)
	{
		boolean FLAG = true;
		if(parent._size == other.parent._size)
			for(int i = 0; i < parent._size && FLAG; ++i)
				for(int j = 0; j < parent._size && FLAG; ++j)
					if(_map[i][j] != other._map[i][j])
						FLAG = false;
					else
						FLAG = false;
		return FLAG;
	}//end of equals
	
	
	/**
	 * fill a string with '-' and return it for line breaker.
	 * @return filled head of the stringly game.
	 */
	private String printHead()
	{
		String returString = new String();
		for(int i = 0; i < parent._size; ++i)
			returString += "- ";
		returString += "\n";
		return returString;
	}//end of printHead
	
	/**
	 * fill a string with Cell's indexes toString method.
	 * @return filled string for body of the stringly game.
	 */
	private String printBody()
	{
		String returnString = new String();
		int won = 0;
		for(int i = 0; i < parent._size; ++i)
		{
			for(int j = 0; j < parent._size; ++j)
			{
				returnString += currentgameCell(i, j);
				if(currentgameCell(i, j).getInside() == 3)
					won = 1;
				else if(currentgameCell(i, j).getInside() == 4)
					won = 2;
			}
			returnString += "\n";
		}
		if(won > 0)
			returnString += "User " + won + " won!";
		return returnString;
	}//end of printBody
	
	/**
	 * Fills array with: head and body.
	 * @return filled string for the stringly game.
	 */
	public String toString()
	{
		String returnString = new String();
		returnString += printHead();
		returnString += printBody();
		return returnString;
	}//end of toString
	
	
	
}
