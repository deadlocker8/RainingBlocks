package blocks;

import logic.Block;
import logic.Board;
import logic.Stone;

/**
 * Klasse, die den Block in Form eines I repräsentiert
 * (erbt von Block)
 * @author Robert
 *
 */
public class Block_I extends Block
{
	/**
	 * Konstruktor
	 * @param color String - Farbe des Blocks (Hexcode)
	 * @param rotation int - Rotation des Blocks (0 = 0°, 1 = 90°, 2 = 180°, 3 = 270°) 
	 */
	public Block_I(String color, int rotation)
	{
		super(color, rotation);		
		center = 4;		
		if(rotation == 0 || rotation == 2)
		{
			points = new int[] {center-1, center, center+1, center+2 };
		}
		else
		{
			points = new int[] {center, center+10, center+20, center+30 };
		}	
	}

	/**
	 * rotiert den Block nach Links
	 *  @param board Board - aktuelles Spielfeld
	 *  @return Board - aktualisiertes Spielfeld
	 */
	@Override
	public Board rotateLeft(Board board)
	{
		//Da der I-Block aus 4 einzelnen Steinen nebeneinander besteht besitzt er keine eindeutige Mitte
		//Für die Mitte wurde der zweite Block von Links gewählt
		//aus diesem Grund muss nicht nur geprüft werden ob er mit dem Rand kollidiert (collidesWithBorder())
		//sondern auch ob die Mitte in Spalte 8 des Spielfeldes liegt
		//wenn ja, dnan darf der Block nicht gedreht werden, da er sonst rechts aus dem Spielfeld gedreht werden würde
		if(center % 10 != 8)
		{
			if(!collidesWithBorder(board))
			{
				Stone stone1 = board.get(points[0]);		
				Stone stone2 = board.get(points[1]);
				Stone stone3 = board.get(points[2]);
				Stone stone4 = board.get(points[3]);	
				
				int[] old = points;
				
				if(rotation == 0 || rotation == 2)
				{
					rotation = 1;
					points = new int[] {center, center+10, center+20, center+30 };
				}
				else
				{
					rotation = 0;
					points = new int[] {center-1, center, center+1, center+2 };
				}					
				
				board.clearCell(old[0]);
				board.clearCell(old[1]);
				board.clearCell(old[2]);
				board.clearCell(old[3]);	
				
				board.set(points[0], stone1);
				board.set(points[1], stone2);
				board.set(points[2], stone3);
				board.set(points[3], stone4);
			}
		}
		return board;
	}

	/**
	 * rotiert den Block nach rechts
	 *  @param board Board - aktuelles Spielfeld
	 *  @return Board - aktualisiertes Spielfeld
	 */
	@Override	
	public Board rotateRight(Board board)
	{
		//Erklärung siehe: rotateLeft
		if(center % 10 != 8)
		{
			if(!collidesWithBorder(board))
			{
				Stone stone1 = board.get(points[0]);		
				Stone stone2 = board.get(points[1]);
				Stone stone3 = board.get(points[2]);
				Stone stone4 = board.get(points[3]);	
				
				int[] old = points;
				
				if(rotation == 0 || rotation == 2)
				{
					rotation = 1;
					points = new int[] {center, center+10, center+20, center+30 };
				}
				else
				{
					rotation = 0;
					points = new int[] {center-1, center, center+1, center+2 };
				}				
				
				board.clearCell(old[0]);
				board.clearCell(old[1]);
				board.clearCell(old[2]);
				board.clearCell(old[3]);	
				
				board.set(points[0], stone1);
				board.set(points[1], stone2);
				board.set(points[2], stone3);
				board.set(points[3], stone4);
			}
		}
		return board;
	}
}