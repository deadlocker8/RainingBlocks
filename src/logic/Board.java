package logic;

import java.util.ArrayList;

/**
 * Repräsentiert das Spielfeld
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
	 * setzt jeden Spielstein des Spielfelds zurück
	 * (weiße Farbe, nicht fest)
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
	 * setzt den Spielstein an der angegebenen Position zurück
	 * (weiße Farbe, nicht fest)
	 * @param index int - Position
	 */
	public void clearCell(int index)
	{			
		board.set(index, new Stone("#FFFFFF", false));			
	}
	
	/**
	 * gibt das Spielfeld zurück
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
	 * gibt die Größe des Spielfelds zurück
	 * @return int - Größe (Anzahl der Spielsteine)
	 */
	public int getSize()
	{
		return board.size();
	}
		
	/**
	 * toString()-Implementierung
	 * (für Debugging)
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
	 * prüft, ob eine Zeile komplett mit festen Steinen gefüllt ist
	 * @param row int- Zeilennummer
	 * @return boolean - ist gefüllt
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
	 * prüft, ob eine Spalte komplett mit festen Steinen gefüllt ist
	 * @param column int- Spaltennummer
	 * @return boolean - ist gefüllt
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
	 * prüft, ob das Spiel verloren ist
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
				//wartet 250ms vor dem Löschen
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
	 * löscht alle Spielsteine in der angegebenen Zeile und lässt alle Zeilen darüber um eine Zeile nach unten rücken
	 * @param row int - Zeilennummer
	 */
	public void deleteLine(int row)
	{
		//Zeile löschen und andere Zeilen nachrücken
		for(int j = row - 1; j >= 0; j--)
		{			
			for(int i = 0 + j * 10; i < 10 + j * 10; i++)
			{
				Stone stone = board.get(i);				
				board.set(i + 10, stone);				
				clearCell(i);						
			}
		}
		
		//entstandene Leerzeilen mit weißen Spielsteinen füllen
		for(int k = 0; k < 10; k++)
		{
			Stone stone = new Stone("#FFFFFF", false);
			board.set(k, stone);
		}		
	}
}