package particles;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;

/**
 * Klasse zur Erzeugung von Partikeln aus einem bestimmten Punkt mit zufälliger Richtung und Geschwindigkeit
 * @author Robert
 *
 */
public class Emitter
{
	private double x;
	private double y;
	private int numberOfParticles;
	private Paint color;
	private boolean hasEmitted;	
	
	/**
	 * Konstruktor
	 * @param x double - X-Koordiante des Spawnpunktes
	 * @param y double - Y-Koordinate des Spawnpunktes
	 * @param numberOfParticles int - Anzahl der zu erzeugenden Partikel
	 * @param color Paint - Farbe der Partikel
	 */
	public Emitter(double x, double y, int numberOfParticles, Paint color)
	{
		this.x = x;
		this.y = y;
		this.numberOfParticles = numberOfParticles;
		this.color = color;
		hasEmitted = false;
	}
	
	/**
	 * Generiert alle Partikel und fügt sie der ArrayList hinzu
	 * @return ArrayList<Particle> - Partikel
	 */	
	public ArrayList<Particle> emit()
	{
		ArrayList<Particle> particles = new ArrayList<Particle>();
		
		for(int i = 0; i < numberOfParticles; i++)
		{
			// -0.5 damit sowohl positive als auch negative Wete erzeugt werden
			// Math.random() erzeugt nur Werte zwischen 0.0. und 1.0
			double xVelocity = (Math.random() -0.5) * 2;
			double xVariance = Math.random() * 3;
			double yVelocity = (Math.random() -0.5) * 2;
			double yVariance =  Math.random() * 3;			
			
			Particle p = new Particle(x, y, new Point2D(xVelocity * xVariance, yVelocity * yVariance), 3.5, 2.0, color, BlendMode.SRC_OVER, -0.8);
			particles.add(p);
			
			hasEmitted = true;
		}
		
		return particles;
	}
	
	/**
	 * gibt an, ob bereits alle Partikel emittiert wurden
	 * @return boolean - wurde emittiert
	 */
	public boolean hasEmitted()
	{
		return hasEmitted;
	}	
}