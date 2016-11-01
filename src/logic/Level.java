package logic;

/**
 * Repr�sentiert ein Level
 * @author Robert
 *
 */
public class Level
{
	private int ID;
	private String name;
	private int rowPoints; //Punkte, die eine aufgel�ste Tetrisreihe wert ist
	private int levelPoints; //ben�tigte Punkte um das Level zu beenden
	private double speed; //Zeitspanne in Sekunden bis der aktuelle Block eine Reihe nach unten bewegt wird
	
	/**
	 * Konstruktor
	 * @param ID int - ID
	 * @param name String - Name
	 * @param rowPoints - int - Punkte, die eine aufgel�ste Tetrisreihe wert ist
	 * @param levelPoints int - ben�tigte Punkte um das Level zu beenden
	 * @param speed double - Zeitspanne in Sekunden bis der aktuelle Block eine Reihe nach unten bewegt wird
	 */
	public Level(int ID, String name, int rowPoints, int levelPoints, double speed)
	{
		this.ID = ID;
		this.name = name;
		this.rowPoints = rowPoints;
		this.levelPoints = levelPoints;
		this.speed = speed;		
	}

	/**
	 * gibt die ID zur�ck
	 * @return int - ID
	 */
	public int getID()
	{
		return ID;
	}
	
	/**
	 * gibt den Namen zur�ck
	 * @return String - Name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * gibt die rowPoints zur�ck
	 * @return int - Punkte, die eine aufgel�ste Tetrisreihe wert ist
	 */
	public int getRowPoints()
	{
		return rowPoints;
	}

	/**
	 * gibt die levelPoints zur�ck
	 * @return int - ben�tigte Punkte um das Level zu beenden
	 */
	public int getLevelPoints()
	{
		return levelPoints;
	}
	
	/**
	 * gibt die Geschwindigkeit zur�ck
	 * @return Zeitspanne in Sekunden bis der aktuelle Block eine Reihe nach unten bewegt wird
	 */
	public double getSpeed()
	{
		return speed;
	}

	/**
	 * toString()-Implementierung
	 * (f�r Debugging)
	 */
	@Override
	public String toString()
	{
		return "ID: " + ID + " name: " + name + " - rowPoints: " + rowPoints + " - levelPoints: " + levelPoints + " - speed: " + speed;
	}
}