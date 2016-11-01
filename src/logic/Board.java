package logic;

import java.util.ArrayList;

/**
 * Repr�sentiert das Spielfeld
 * (10 Spalten und 18 Zeilen)
 * (besteht aus Spielsteinen (Stone))
 * @author Robert
 *
 */
public class Board
{
	private static ArrayList<Stone> board;
	
	/**
	 * Konstruktor
	 */
	public Board()
	{
		board = new ArrayList<Stone>();						
		
		clear();
	}
	
	/**
	 * setzt jeden Spielstein des Spielfelds zur�ck
	 * (wei�e Farbe, nicht fest)
	 */
	public void clear()
	{
		for(int i = 0; i < 18; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				board.add(new Stone("#FFFFFF", false));		
			}			
		}
	}
	
	/**
	 * setzt den Spielstein an der angegebenen Position zur�ck
	 * (wei�e Farbe, nicht fest)
	 * @param index int - Position
	 */
	public void clearCell(int index)
	{			
		board.set(index, new Stone("#FFFFFF", false));			
	}
	
	/**
	 * gibt das Spielfeld zur�ck
	 * @return ArrayList<Stone> - Spielfeld
	 */
	public ArrayList<Stone> getBoard()
	{
		return board;
	}	
	
	/**
	 * gibt den Spielstein an der angegebenen Position
	 * @param index int - Position
	 * @return Stone - Spielstein
	 */
	public Stone get(int index)
	{
		return board.get(index);
	}
	
	/**
	 * set den Spielstein an der angegebenen Position
	 * @param index int - Position
	 * @return Stone - Spielstein
	 */
	public void set(int index, Stone stone)
	{
		board.set(index, stone);
	}
	
	/**
	 * gibt die Gr��e des Spielfelds zur�ck
	 * @return int - Gr��e (Anzahl der Spielsteine)
	 */
	public int getSize()
	{
		return board.size();
	}
		
	/**
	 * toString()-Implementierung
	 * (f�r Debugging)
	 */
	@Override
	public String toString()
	{
		String result = "";
		for(int i = 0; i < 18; i++)
		{			
			String line = "";
			for(int j = 0 + i*10 ; j < 10 + i*10; j++)
			{
				line += " " + board.get(j).getColor() + board.get(j).isFixed();
			}
			result += line + "\n";			
		}
		return result;
	}	
	
	/**
	 * pr�ft, ob eine Zeile komplett mit festen Steinen gef�llt ist
	 * @param row int- Zeilennummer
	 * @return boolean - ist gef�llt
	 */
	public boolean isRowFilled(int row)
	{
		int counter = 0;
		for(int i = 0 + row * 10; i < 10 + row * 10; i++)
		{		
			if(board.get(i).isFixed())
			{
				counter++;
			}
		}
		
		if(counter == 10)
		{
			return true;
		}
		else
		{
			return false;
		}		
	}	
	
	/**
	 * pr�ft, ob eine Spalte komplett mit festen Steinen gef�llt ist
	 * @param column int- Spaltennummer
	 * @return boolean - ist gef�llt
	 */
	public boolean isColumnFilledUp(int column)
	{
		int counter = 0;		
		for(int j = column; j < column + 180; j += 10)
		{			
			if(board.get(j).isFixed())
			{
				counter++;
			}
		}		
		
		if(counter == 18)
		{
			return true;
		}
		else
		{
			return false;
		}		
	}
	
	/**
	 * pr�ft, ob das Spiel verloren ist
	 * @return boolean - verloren
	 */
	public boolean isGameOver()
	{
		for(int i = 0; i < 10; i++)
		{
			if(isColumnFilledUp(i))
			{
				return true;
			}			
		}
		return false;
	}	
	
	public int deleteLines()
	{
		int lineCount = 0;		
		for(int i = 0; i <= 17; i++)
		{
			if(isRowFilled(i))
			{				
				lineCount++;	
				//wartet 250ms vor dem L�schen
				try
				{
					Thread.sleep(250);
				}
				catch(InterruptedException e)
				{					
				}		
				deleteLine(i);					
			}
		}
		return lineCount;
	}	
	
	/**
	 * l�scht alle Spielsteine in der angegebenen Zeile und l�sst alle Zeilen dar�ber um eine Zeile nach unten r�cken
	 * @param row int - Zeilennummer
	 */
	public void deleteLine(int row)
	{
		//Zeile l�schen und andere Zeilen nachr�cken
		for(int j = row - 1; j >= 0; j--)
		{			
			for(int i = 0 + j * 10; i < 10 + j * 10; i++)
			{
				Stone stone = board.get(i);				
				board.set(i + 10, stone);				
				clearCell(i);						
			}
		}
		
		//entstandene Leerzeilen mit wei�en Spielsteinen f�llen
		for(int k = 0; k < 10; k++)
		{
			Stone stone = new Stone("#FFFFFF", false);
			board.set(k, stone);
		}		
	}
}