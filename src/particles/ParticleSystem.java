package particles;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Klasse zur Erstellung eines Partikelsystems mit verschiedenen Emittern
 * @author Robert
 *
 */
public class ParticleSystem
{
	private Canvas canvas;
	private AnimationTimer timer;
	private GraphicsContext g;
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private ArrayList<Emitter> emitters = new ArrayList<Emitter>();
	private boolean allEmitted;
	
	/**
	 * Konstruktor
	 * @param width double - Breite des Canvas
	 * @param height dozble - Höhe des Canvas
	 */
	public ParticleSystem(double width, double height)
	{
		canvas = new Canvas(width, height);		
		g = canvas.getGraphicsContext2D();	
		
		double centerX = width / 2;
		double centerY = height / 2;		
		
		emitters.add(new Emitter(centerX, centerY, 300, Color.rgb(230, 40, 45))); 	//rot
		emitters.add(new Emitter(centerX - centerX / 2, centerY + centerY / 2, 300, Color.rgb(40, 230, 45))); 	//grün
		emitters.add(new Emitter(centerX - centerX * 0.6, centerY - centerX / 5, 300, Color.rgb(40, 45, 230)));	//blau
		emitters.add(new Emitter(centerX + centerX / 2, centerY - centerY / 2, 300, Color.rgb(230, 230, 45)));	//gelb
		emitters.add(new Emitter(centerX + centerX / 2, centerY + centerY / 3, 300, Color.rgb(230, 45, 230)));	//pink		
		allEmitted = false;		
	}
	
	/**
	 * Gibt das Canvas zurück
	 * @return Canvas
	 */
	public Canvas getCanvas()
	{
		return canvas;
	}	
	
	/**
	 * Updatemethode
	 * --> bereinigt das Canvas
	 * --> rendert jeden Partikel neu
	 * --> stoppt den AnimationTimer, falls alle Partikel emittiert wurden und wieder gestorben sind
	 */
	private void onUpdate()
	{			
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());		
		
		for(Iterator<Particle> iterator = particles.iterator(); iterator.hasNext();)
		{
			Particle p = iterator.next();
			p.update();
			if(p.isAlive())
			{
				p.render(g);
			}
			else
			{
				iterator.remove();
			}
		}
		
		if(allEmitted && particles.size() == 0)
		{
			timer.stop();
		}
	}
	
	/**
	 * startet den AnimationTimer 
	 * --> 60 FPS
	 * --> gibt die aktuellen FPS aus
	 * --> startet die verschiedenen Emitter und ruft die onUpdate-Methode auf
	 */
	public void start() 
	{
		timer = new AnimationTimer()
		{
			private  int frames; 
			private long lastTime = System.nanoTime();
			
			@Override
			public void handle(long now)
			{					
				onUpdate();	
				long currenttime = System.nanoTime();

				if(currenttime > lastTime + 100000000 )
                {	 
					if(!emitters.get(0).hasEmitted())
					{
						particles.addAll(emitters.get(0).emit());
					}
                }
				
				if(currenttime > lastTime + 300000000 )
                {						
					if(!emitters.get(1).hasEmitted())
					{
						particles.addAll(emitters.get(1).emit());
					}
                }
				
				if(currenttime > lastTime + 500000000 )
                {	 
					if(!emitters.get(2).hasEmitted())
					{
						particles.addAll(emitters.get(2).emit());
					}
					if(!emitters.get(3).hasEmitted())
					{
						particles.addAll(emitters.get(3).emit());
					}
                }
				
				if(currenttime > lastTime + 700000000 )
                {	 
					if(!emitters.get(4).hasEmitted())
					{
						particles.addAll(emitters.get(4).emit());
						allEmitted = true;
					}							
                }
				
				//zeigt FPS an
				frames++;						
                if (currenttime > lastTime + 1000000000)
                {
                    System.out.println("Current FPS: " + frames);  
                    
                    frames = 0;
                    lastTime = currenttime;		                   
                }   
			}
		};	
		timer.start();	
	}
	
	/**
	 * stoppt den AnimationTimer
	 */
	public void stop()
	{
		if(timer != null)
		{
			timer.stop();
		}
	}	
}