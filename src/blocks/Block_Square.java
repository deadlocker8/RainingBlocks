package blocks;

import logic.Block;
import logic.Board;

/**
 * Klasse, die den Block in Form eines Quadrates repräsentiert
 * (erbt von Block)
 * @author Robert
 *
 */
public class Block_Square extends Block
{
	/**
	 * Konstruktor
	 * @param color String - Farbe des Blocks (Hexcode)
	 * @param rotation int - Rotation des Blocks (da es sich um ein Quadrat handelt, spielt die Rotation keine Rolle)
	 */
	public Block_Square(String color, int rotation)
	{
		super(color, rotation);		
		center = 4;	
		points = new int[] {center, center+1 , center+10, center+11 };
	}

	/**
	 * rotiert den Block nach Links
	 * da es sich um ein Quadrat handelt, verändert die Rotation das Spielfeld nicht 
	 * @param board Board - aktuelles Spielfeld	 *
	 */
	@Override
	public Board rotateLeft(Board board)
	{
		return board;
	}

	/**
	 * rotiert den Block nach Links
	 * da es sich um ein Quadrat handelt, verändert die Rotation das Spielfeld nicht 
	 * @param board Board - aktuelles Spielfeld
	 */
	@Override
	public Board rotateRight(Board board)
	{		
		return board;
	}
}