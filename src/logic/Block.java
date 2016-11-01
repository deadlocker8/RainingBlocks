package logic;

import java.util.Arrays;

/**
 * Repräsentiert einen Block
 * (Eigenschaften und Methoden, die für alle Blocktypen gleich sind)
 * (2 abstrakte Methoden, die von den jeweiligen Blocktypen überschrieben werden
 * --> siehe package "blocks")
 * @author Robert
 *
 */
public abstract class Block
{
	protected String color;
	protected int center;	
	protected int rotation;
	protected int[] points;	
	
	/**
	 * Konstruktor
	 * @param color String - Farbe des Blocks (Hexcode)
	 * @param rotation int - Rotation des Blocks (0 = 0°, 1 = 90°, 2 = 180°, 3 = 270°) 
	 */
	public Block(String color, int rotation)
	{
		this.color = color;
		this.rotation = rotation;			
	}	

	/**
	 * gibt die aktuellen Positionen der einzelnen Spielsteine des Blocks im Spielfeld zurück
	 * @return int[] - Positionen der Spielsteine
	 */
	public int[] getPoints()
	{
		return points;
	}	
	
	/**
	 * gibt die Farbe des Blocks zurück
	 * @return String - Farbe (Hexcode)
	 */
	public String getColor()
	{
		return color;
	}	
 
	/**
	 * toString()-Implementierung
	 * (für Debugging)
	 */
	@Override
	public String toString()
	{
		return Arrays.toString(points);
	}
	
	/**
	 * fügt den Block in das gegebene Board ein
	 * @param board Board - aktuelles Spielfeld
	 * @return Board - aktualisiertes Spielfeld
	 */
	public Board insertBlock(Board board)
	{
		Stone stone1 = board.get(points[0]);
		stone1.setColor(color);
		board.set(points[0], stone1);
		
		Stone stone2 = board.get(points[1]);
		stone2.setColor(color);		
		board.set(points[1], stone2);
		
		Stone stone3 = board.get(points[2]);
		stone3.setColor(color);		
		board.set(points[2], stone3);
		
		Stone stone4 = board.get(points[3]);
		stone4.setColor(color);	
		board.set(points[3], stone4);
		
		return board;
	}	
	
	/**
	 * prüft, ob der Block als fest gilt
	 * @param board Board - aktuelles Spielfeld
	 * @return boolean - ist fest (unbeweglich)
	 */
	public boolean isFixed(Board board)
	{
		//wenn einer der einzelnen Spielsteine des Blocks unbeweglich ist, dann ist der ganze Block unbeweglich
		if(board.get(points[0]).isFixed() || board.get(points[1]).isFixed() || board.get(points[2]).isFixed() || board.get(points[3]).isFixed())
		{			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * prüft, ob der Block eingefügt werden kann
	 * @param board Board - aktuelles Board
	 * @return boolean - kann eingefügt werden
	 */
	public boolean canInsert(Board board)
	{			
	 	if(board.get(points[0]).isFixed() || board.get(points[1]).isFixed() || board.get(points[2]).isFixed() || board.get(points[3]).isFixed())
		{		 		
			return false;
		}
		else
		{
			return true;
		}		
	}
	
	/**
	 * prüft, ob einer der Spielsteine des Blocks an den linken oder rechten Rand des Spielfeldes stößt
	 * @param board Board - aktuelles Board
	 * @return boolean - stößt an einen der beiden Ränder
	 */
	public boolean collidesWithBorder(Board board)
	{
		if(collidesWithLeftBorder(board) || collidesWithRightBorder(board))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * prüft, ob einer der Spielsteine des Blocks an den linken Rand des Spielfeldes stößt
	 * @param board Board - aktuelles Board
	 * @return boolean - stößt an den linken Rand
	 */
	public boolean collidesWithLeftBorder(Board board)
	{
		//Zentrum des Blocks liegt in Spalte 0
		if(center % 10 == 0)
		{			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * prüft, ob einer der Spielsteine des Blocks an den rechten Rand des Spielfeldes stößt
	 * @param board Board - aktuelles Board
	 * @return boolean - stößt an den rechten Rand
	 */
	public boolean collidesWithRightBorder(Board board)
	{
		//Zentrum des Blocks liegt in Spalte 9
		if(center % 10 == 9)
		{			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * prüft, ob der Block um eine Einheit nach links bewegt werden kann
	 * @param board Board - aktuelles Board
	 * @return boolean - kann nach links bewegt werden
	 */
	public boolean canMoveLeft(Board board)
	{	
		//Block stößt nicht bereits an den linken Rand
		if(!collidesWithLeftBorder(board))
		{
			//links vom Block befinden sich feste Steine
			if(board.get(points[0] - 1).isFixed() || board.get(points[1] - 1).isFixed() || board.get(points[2] - 1).isFixed() || board.get(points[3] - 1).isFixed())
			{
				return false;
			}
			//nach dem Bewegen des Blocks befindet sich keiner seiner Spielsteine außerhalb des linken Spielfeldrandes
			else if(((points[0] % 10) - 1) >= 0)			
			{	
				if(((points[1] % 10) - 1) >= 0)			
				{
					if(((points[2] % 10) - 1) >= 0)			
					{
						if(((points[3] % 10) - 1) >= 0)			
						{
							return true;
						}
					}
				}
			}	
			return false;	
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * prüft, ob der Block um eine Einheit nach rechts bewegt werden kann
	 * @param board Board - aktuelles Board
	 * @return boolean - kann nach rechts bewegt werden
	 */
	public boolean canMoveRight(Board board)
	{		
		//Block stößt nicht bereits an den rechten Rand
		if(!collidesWithRightBorder(board))
		{
			//rechts vom Block befinden sich feste Steine
			if(board.get(points[0] + 1).isFixed() || board.get(points[1] + 1).isFixed() || board.get(points[2] + 1).isFixed() || board.get(points[3] + 1).isFixed())
			{					
				return false;
			}	
			//nach dem Bewegen des Blocks befindet sich keiner seiner Spielsteine außerhalb des rechten Spielfeldrandes
			else if(((points[0] % 10) + 1) <= 9)			
			{	
				if(((points[1] % 10) + 1) <= 9)			
				{
					if(((points[2] % 10) + 1) <= 9)			
					{
						if(((points[3] % 10) + 1) <= 9)			
						{
							return true;
						}
					}
				}
			}						
			return false;	
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * prüft, ob der Block um eine Einheit nach unten bewegt werden kann
	 * @param board Board - aktuelles Board
	 * @return boolean - kann nach unten bewegt werden
	 */
	public boolean canMoveDown(Board board)
	{		
		//kein Spielstein des Blocks ist bereits in der untersten Zeile
		//UNDe
		//eine Zeile unter jedem Spielstein des Blocks befindet sich kein fester Stein		
		if(((points[0]/10) + 1) < 18 && !board.get(points[0] + 10).isFixed())			
		{					
			if(((points[1]/10) + 1) < 18 && !board.get(points[1] + 10).isFixed())		
			{				
				if(((points[2]/10) + 1) < 18 && !board.get(points[2] + 10).isFixed())		
				{					
					if(((points[3]/10) + 1) < 18 && !board.get(points[3] + 10).isFixed())		
					{
						return true;
					}
				}				
			}
		}
		return false;
	}
	
	/**
	 * bewegt den Block um eine Einheit nach links
	 * @param board Board - aktuelles Board
	 * @return Board - aktualisiertes Board
	 */
	public Board moveLeft(Board board)
	{
		Stone stone1 = board.get(points[0]);		
		Stone stone2 = board.get(points[1]);
		Stone stone3 = board.get(points[2]);
		Stone stone4 = board.get(points[3]);	
		
		if(canMoveLeft(board))
		{
			center = center - 1;						
			
			board.clearCell(points[0]);				
			points[0] -= 1;
			board.set(points[0], stone1);
			
			board.clearCell(points[1]);					
			points[1] -= 1;
			board.set(points[1], stone2);
			
			board.clearCell(points[2]);						
			points[2] -= 1;
			board.set(points[2], stone3);						
			
			board.clearCell(points[3]);			
			points[3] -= 1;
			board.set(points[3], stone4);		
		}
		
		return board;
	}

	/**
	 * bewegt den Block um eine Einheit nach rechts
	 * @param board Board - aktuelles Board
	 * @return Board - aktualisiertes Board
	 */
	public Board moveRight(Board board)
	{
		Stone stone1 = board.get(points[0]);		
		Stone stone2 = board.get(points[1]);
		Stone stone3 = board.get(points[2]);
		Stone stone4 = board.get(points[3]);	
			
		if(canMoveRight(board))
		{
			center = center + 1;						
			
			board.clearCell(points[3]);								
			points[3] += 1;
			board.set(points[3], stone4);
			
			board.clearCell(points[2]);						
			points[2] += 1;
			board.set(points[2], stone3);
			
			board.clearCell(points[1]);						
			points[1] += 1;
			board.set(points[1], stone2);
			
			board.clearCell(points[0]);						
			points[0] += 1;
			board.set(points[0], stone1);	
		}
		
		return board;		
	}
	
	/**
	 * bewegt den Block um eine Einheit nach unten
	 * @param board Board - aktuelles Board
	 * @return Board - aktualisiertes Board
	 */
	public Board moveDown(Board board)
	{
		Stone stone1 = board.get(points[0]);		
		Stone stone2 = board.get(points[1]);
		Stone stone3 = board.get(points[2]);
		Stone stone4 = board.get(points[3]);	
		
		if(canMoveDown(board))
		{
			center = center + 10;						
			
			board.clearCell(points[3]);						
			points[3] += 10;
			board.set(points[3], stone4);	
			
			board.clearCell(points[2]);													
			points[2] += 10;
			board.set(points[2], stone3);
			
			board.clearCell(points[1]);				
			points[1] += 10;
			board.set(points[1], stone2);											
			
			board.clearCell(points[0]);					
			points[0] += 10;					
			board.set(points[0], stone1);
			
			//falls einer der Spielsteine des Blocks die unterste Zeile erreicht wird er als fest (unbeweglich) markiert
			if(points[3] / 10 == 17 || points[2] / 10 == 17 || points[1] / 10 == 17 || points[0] / 10 == 17)
			{
				board.get(points[0]).setFixed();
				board.get(points[1]).setFixed();
				board.get(points[2]).setFixed();
				board.get(points[3]).setFixed();
			}
		}
		else
		{
			//falls der Block nicht bewegt werden kann wird er als fest (unbeweglich) markiert
			board.get(points[0]).setFixed();
			board.get(points[1]).setFixed();
			board.get(points[2]).setFixed();
			board.get(points[3]).setFixed();
		}
		return board;
	}
	
	//abstrakte Methode (wird überschrieben von den einzelnen Blocktypen)
	public abstract Board rotateLeft(Board board);
	
	//abstrakte Methode (wird überschrieben von den einzelnen Blocktypen)
	public abstract Board rotateRight(Board board);	
}