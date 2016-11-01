package particles;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Paint;

/**
 * Repräsentiert ein Partikel
 * @author Robert
 *
 */
public class Particle
{
	private double x;
	private double y;
	private Point2D velocity;
	private double radius;
	private double lifeTime = 1.0;
	private double decay;
	private Paint color;
	private BlendMode blendMode;
	private double gravity;
	
	/**
	 * Konstruktor
	 * @param x double - X-Koordinate des Partikels
	 * @param ydouble - Y-Koordinate des Partikels
	 * @param velocity Point2D - Richtungsvektor des Partikels
	 * @param radius double - Radius des Partikels
	 * @param expireTime double - Lebenszeit des Partikels in Sekunden
	 * @param color Paint - Farbe des Partikels
	 * @param blendMode BlendMode - BlendMode des Partikels (Art, wie es auf das Canvas gezeichnet wird)
	 * @param gravity double - Gravitation, die den Partikel beeinflusst
	 */
	public Particle(double x, double y, Point2D velocity, double radius, double expireTime, Paint color, BlendMode blendMode, double gravity)
	{		
		this.x = x;
		this.y = y;
		this.velocity = velocity;
		this.radius = radius;
		this.lifeTime = Math.random();
		//Framerate (60 FPS) durch Lebenszeit
		this.decay = 0.016 / expireTime;
		this.color = color;
		this.blendMode = blendMode;
		this.gravity = gravity;
	}	
	
	/**
	 * Aktualisiert die Position des Partikels
	 * (bewegt den Partikel entsprechend der Velocity)
	 */
	public void update()
	{
		x += velocity.getX();
		y += velocity.getY() - gravity;
		
		lifeTime -= decay;
	}	
	
	/**
	 * Zeichnet den Partikel in das Canvas
	 * @param g GraphicsContext - Zeichenobjekt des Canvas
	 */
	public void render(GraphicsContext g)
	{
		g.setGlobalAlpha(lifeTime);
		g.setGlobalBlendMode(blendMode);
		g.setFill(color);
		g.fillOval(x, y, radius, radius);
	}
	
	/**
	 * gibt an, ob der Partikel noch am Leben ist
	 * @return boolean - ist Partikel am Leben
	 */
	public boolean isAlive()
	{
		if(lifeTime > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}