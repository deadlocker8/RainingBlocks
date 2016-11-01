package logic;

import java.util.ArrayList;

/**
 * Verwaltet die Level
 * @author Robert
 *
 */
public class LevelHandler
{
	private ArrayList<Level> levels = new ArrayList<Level>();
	
	/**
	 * generiert die Level
	 */
	public LevelHandler()
	{
		levels.add(new Level(0, "Level 1", 100, 500, 0.5));
		levels.add(new Level(1, "Level 2", 100, 800, 0.5));
		levels.add(new Level(2, "Level 3", 100, 1000, 0.5));
	
		levels.add(new Level(3, "Level 4", 100, 800, 0.3));
		levels.add(new Level(4, "Level 5", 100, 1000, 0.3));
		levels.add(new Level(5, "Level 6", 100, 1500, 0.3));
		
		levels.add(new Level(6, "Level 7", 100, 500, 0.2));
		levels.add(new Level(7, "Level 8", 100, 800, 0.2));
		levels.add(new Level(8, "Level 9", 100, 1000, 0.2));
		levels.add(new Level(9, "Level 10", 100, 1000, 0.15));
		
		//BonusLevel "Sammle soviele Punkte wie möglich!"
		levels.add(new Level(-1, "Bonuslevel", 10000, 0, 0.4));		
	}
	
	/**
	 * gibt das Level mit der angegebenen ID zurück
	 * @param level int - ID
	 * @return Level - Level
	 */
	public Level getLevel(int level)
	{
		return levels.get(level);
	}	
}