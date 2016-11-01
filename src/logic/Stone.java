package logic;

/**
 * repräsentiert einen Spielstein 
 * (Spielfeld (Board) besteht aus Spielsteinen)
 * (jeder Block besteht aus 4 Spielsteinen)
 * @author Robert
 *
 */
public class Stone
{
	private String color;	
	private boolean fixed;	
	
	/**
	 * Konstruktor
	 * @param color String - Farbe des Spielsteins (Hexcode)
	 * @param fixed boolean - ist der Stein noch beweglich oder gilt er als fest(fixed)
	 */
	public Stone(String color, boolean fixed)
	{
		this.color = color;		
		this.fixed = fixed;
	}

	/**
	 * gibt die Farbe zurück
	 * @return String - Farbe
	 */
	public String getColor()
	{
		return color;
	}

	/**
	 * gibt zurück, ob der Stein als fest gilt
	 * @return boolean - ist fest (unbeweglich)
	 */
	public boolean isFixed()
	{
		return fixed;
	}
	
	/**
	 * setzt die Farbe des Spielsteins
	 * @param color String- Farbe (Hexcode)
	 */
	public void setColor(String color)
	{
		this.color = color;
	}

	/**
	 * setzt den Spielstein auf den Status "fixed"
	 */
	public void setFixed()
	{
		this.fixed = true;
	}	
	
	/**
	 * toString()-Implementierung
	 * (für Debugging)
	 */
	@Override
	public String toString()
	{
		return "Color: " + color + " isFixed: " + fixed;
	}
}