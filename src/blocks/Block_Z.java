package blocks;

import logic.Block;
import logic.Board;
import logic.Stone;

/**
 * Klasse, die den Block in Form eines Z repräsentiert
 * (erbt von Block)
 * @author Robert
 *
 */
public class Block_Z extends Block
{
	public Block_Z(String color, int rotation)
	{
		super(color, rotation);		
		center = 14;		
		if(rotation == 0 || rotation == 2)
		{
			points = new int[] {center-1, center, center+10, center+11 };
		}
		else
		{
			points = new int[] {center-10, center-1, center, center+9 };
		}		
	}

	/**
	 * rotiert den Block nach links
	 *  @param board Board - aktuelles Spielfeld
	 *  @return Board - aktualisiertes Spielfeld
	 */
	@Override
	public Board rotateLeft(Board board)
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
				points = new int[] {center-10, center-1, center, center+9 };
				rotation = 1;								
			}
			else
			{
				rotation = 0;
				points = new int[] {center-1, center, center+10, center+11};								
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
		if(!collidesWithBorder(board))
		{
			Stone stone1 = board.get(points[0]);		
			Stone stone2 = board.get(points[1]);
			Stone stone3 = board.get(points[2]);
			Stone stone4 = board.get(points[3]);	
			
			int[] old = points;
			
			if(rotation == 0 || rotation == 2)
			{
				points = new int[] {center-9, center, center+1, center+10 };
				rotation = 1;								
			}
			else
			{
				rotation = 0;
				points = new int[] {center-11, center-10, center, center+1 };								
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
		return board;
	}
}