package application;
	
import achievements.Achievement.Status;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * Hauptklasse der Anwendung
 * @author Robert
 *
 */
public class Main extends Application
{
	@Override
	public void start(Stage stage)
	{
		try 
		{
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("application/MainGUI.fxml"));
			Parent root = (Parent)loader.load();			
				    
			Scene scene = new Scene(root,800,600);					
					
			Controller controller = ((Controller)loader.getController());
			controller.setStage(stage);
			controller.init();
			
			stage.getIcons().add(new Image("/images/icon.png"));   	
			
			stage.setTitle("RainingBlocks");		
			stage.setScene(scene);
			stage.setMinWidth(850);
			stage.setMinHeight(650);
			
			//erlaubt das Vergößern bzw Verkleinern des Fensters nur in einem bestimmten Seitenverhältnis
			stage.widthProperty().addListener(new ChangeListener<Number>() {
			    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
			    	stage.setHeight(newSceneWidth.doubleValue() / 1.33);
			    }
			});
			
			//erlaubt das Vergößern bzw Verkleinern des Fensters nur in einem bestimmten Seitenverhältnis
			stage.heightProperty().addListener(new ChangeListener<Number>() {
			    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
			    	stage.setWidth(newSceneWidth.doubleValue() * 1.33);
			    }
			});			
			
			//prüft beim Schließen, ob noch Threads laufen und beendet diese
			stage.setOnCloseRequest(new EventHandler<WindowEvent>()
			{				
				@Override
				public void handle(WindowEvent event)
				{
					Controller controller = loader.getController();
					
					if(controller.threadLevel != null)
					{
		        		if(controller.threadLevel.isAlive())
		        		{
		        			controller.threadLevel.interrupt();
		        		}
					}
					
					//Setzt die Achievements "Erreiche Level 5" bzw. Level 10 zurück
					//sonst wäre es möglich bis Level 3 zu spielen, das Game zu schließen, bis Level 2 zu spielen und so das Achievement freizuschalten
					
					try
					{
						controller.handler.loadAchievements();
						if(controller.handler.getAchievements().get(1).getStatus() == Status.LOCKED)
						{						
							controller.handler.resetAchievement(1);
						}
						if(controller.handler.getAchievements().get(2).getStatus() == Status.LOCKED)
						{
							controller.handler.resetAchievement(2);
						}
						
						try
						{
							controller.handler.saveAndLoad();
						}
						catch(Exception e)
						{
							
						}	
					}
					catch(Exception e)
					{
						
					}					
				}
			});
			
			stage.show();		
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{		
		launch(args);
	}	
}