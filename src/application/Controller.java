package application;

import java.io.File;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.Block;
import logic.Board;
import logic.Level;
import logic.LevelHandler;
import logic.Stone;
import particles.ParticleSystem;
import tools.PathUtils;
import achievements.Achievement;
import achievements.Achievement.Status;
import achievements.AchievementHandler;
import blocks.Block_I;
import blocks.Block_L_Left;
import blocks.Block_L_Right;
import blocks.Block_S;
import blocks.Block_Square;
import blocks.Block_T;
import blocks.Block_Z;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;

/**
 * Controllerklasse zur Steuerung des GUI und des Spiels
 * @author Robert
 *
 */
public class Controller
{
	@FXML private TextField textFieldPreis;
	@FXML private AnchorPane anchorPaneLevel;
	@FXML private AnchorPane anchorPaneMain;
	@FXML private ProgressBar progressBar;
	@FXML private Label labelProgress;
	@FXML private Label labelLevel;
	@FXML private GridPane grid;
	@FXML private VBox vbox;
	@FXML private HBox hboxLogo;
	@FXML private ImageView logo;
	@FXML private StackPane stackPane;
	@FXML private Button buttonAchievements;

	public Stage stage;
	private Board board;
	private Block block;
	private int points;
	private Level level;
	public Thread threadLevel;
	private boolean running;
	private Button buttonLeft;
	private Button buttonRight;
	private Button buttonDown;
	private Button buttonRotateLeft;
	private Button buttonRotateRight;
	private Button buttonStart;
	private LevelHandler levelHandler = new LevelHandler();
	public AchievementHandler handler;
	private int logoCounter;
	private Image icon = new Image("/images/icon.png");
	private String savePath = PathUtils.getOSindependentPath() + "/Deadlocker/RainingBlocks/";

	private static ResourceBundle bundle = ResourceBundle.getBundle("application/", Locale.GERMANY);

	/**
	 * bekommt von der Mainklasse die Stage übergeben
	 * @param s Stage
	 */
	public void setStage(Stage s)
	{
		stage = s;
	}

	/**
	 * startet das Spiel
	 * (Gameloop)
	 */
	private void startGame()
	{
		board = new Board();	
		setGrid();

		if(threadLevel != null)
		{
			if(threadLevel.isAlive())
			{
				threadLevel.interrupt();
			}
		}

		points = 0;

		if(level == null)
		{
			level = levelHandler.getLevel(0);
		}		

		labelLevel.setText(level.getName());
		
		//Gameloop
		threadLevel = new Thread()
		{
			private long lastFrame = 0;
			private final double moveThreshhold = level.getSpeed();
			private double moveDelta = 0;
			boolean canInsert = true;

			@Override
			public void run()
			{				
				running = true;

				Platform.runLater(() -> {
					createNewBlock();
				});

				while(true)
				{					
					long currentTime = System.currentTimeMillis();
					double dt = (currentTime - lastFrame) / 1000D;
					lastFrame = currentTime;

					if(block != null)
					{
						moveDelta += dt;
						if(moveDelta > moveThreshhold)
						{
							if(moveDelta >= moveThreshhold * 2)
							{
								moveDelta = moveThreshhold;
							}
							moveDelta -= moveThreshhold;
							//Block nach unten bewegen und GUI aktualisieren
							block.moveDown(board);
							Platform.runLater(() -> {
								setGrid();
							});

							//falls der Blok sich nicht mehr bewegen lässt
							if(block.isFixed(board))
							{		
								//prüfe, ob es Zeilen gibt, die vollständig sind und lösche diese
								int linesDeleted = board.deleteLines();
								int gainedPoints = linesDeleted * level.getRowPoints();
								setPoints(gainedPoints);
								Platform.runLater(() -> {
									setGrid();																
								});
								setProgress();								
								
								//inkrementiere die entsprechenden Achievements
								if(gainedPoints > 0)
								{
									Platform.runLater(() -> {
										try
										{										
											handler.incrementAchievement(5, gainedPoints);
											handler.incrementAchievement(6, gainedPoints);		
											if(level.getID() == -1)
											{
												handler.incrementAchievement(7, gainedPoints);
											}
											handler.checkAllIncrementalAchievements();	
											handler.saveAndLoad();
										}
										catch(Exception e)
										{									
										}
									});
								}

								//falls es nicht das Bonuslevel ist
								if(level.getID() >= 0)
								{
									//und die erreichten Punkte größer gleich der benötigten Punkte für das Level ist
									if(points >= level.getLevelPoints())
									{
										//gewinnt der Spieler das aktuelle Level
										running = false;										
										Platform.runLater(() -> {
											win();											
										});
										break;
									}
								}
								
								//erzeugt einen neuen Block und prüft ob er eingefügt werden kann
								Platform.runLater(() -> {
									canInsert = createNewBlock();									
								});
							}
							//wenn der Block nicht fest ist
							else
							{
								//prüfe, ob bereits gewonnen ist 
								//(wichtig für die Cheatfunktion, da sonst erst geprüft wird, wenn der Block sich nicht mehr bewegen lässt) 
								if(level.getID() >= 0)
								{
									if(points >= level.getLevelPoints())
									{
										running = false;										
										Platform.runLater(() -> {
											win();											
										});
										break;
									}
								}
							}
							
							//falls der neugenerierte Block nicht eingefügt werden kann
							//oder eine Spalte des Spielfeldes gefüllt ist (GameOver)
							if(!canInsert || board.isGameOver())
							{
								running = false;
								//fürs Bonuslevel:
								if(level.getID() == - 1)
								{		
									Platform.runLater(() -> {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Level geschafft!!");
										alert.setHeaderText("");
										alert.setContentText("Du hast das " + level.getName() + " mit " + points + " Punkten abgeschlossen.");
										alert.initOwner(stage);
										Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
										dialogStage.getIcons().add(icon);

										//Möglichkeit wieder bei Level 1 zu beginnen
										ButtonType buttonTypeOne = new ButtonType("Level 1 neustarten");
										ButtonType buttonTypeTwo = new ButtonType("OK");

										alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

										Optional<ButtonType> result = alert.showAndWait();
										if(result.get() == buttonTypeOne)
										{									
											try
											{
												handler.incrementAchievement(3, 1);												
												handler.checkAllIncrementalAchievements();										
												handler.saveAndLoad();
											}
											catch(Exception e)
											{									
											}
											
											level = levelHandler.getLevel(0);										

											points = 0;
											setProgress();
											labelLevel.setText(level.getName());
											board = new Board();
											setGrid();
											buttonStart.setDisable(false);	
										}
										else
										{
											try
											{
												handler.incrementAchievement(3, 1);												
												handler.checkAllIncrementalAchievements();										
												handler.saveAndLoad();
											}
											catch(Exception e)
											{									
											}
											alert.close();
										}										
										
									});
								}
								else
								{	
									//für alle anderen Level:
									Platform.runLater(() -> {
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Verloren!");
										alert.setHeaderText("");
										alert.setContentText("Du hast " + level.getName() + " nicht geschafft! Du hast " + points + " von " + level.getLevelPoints() + " Punkten erreicht.");
										alert.initOwner(stage);
										Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
										dialogStage.getIcons().add(icon);
										alert.showAndWait();
										buttonStart.setDisable(false);
										try
										{
											handler.incrementAchievement(3, 1);																				
											handler.checkAllIncrementalAchievements();										
											handler.saveAndLoad();
										}
										catch(Exception e)
										{									
										}
									});
								}
								break;
							}
							//wenn der Block eingefügt werden kann
							else
							{		
								//wird er eingefügt
								Platform.runLater(()->{									
									insertBlock();
								});								
							}
						}
					}

					//Gameloop schläft ca. 15ms bis zum nächsten Update
					try
					{
						Thread.sleep(15);
					}
					catch(InterruptedException e)
					{
						break;
					}
				}
			}
		};
		threadLevel.start();
	}

	/**
	 * aktualisiert die GridPane, die das Spielfeld abbildet
	 */
	private synchronized void setGrid()
	{
		grid.setHgap(2);
		grid.setVgap(2);

		grid.getChildren().removeIf(new Predicate<Node>()
		{
			@Override
			public boolean test(Node t)
			{
				if(t instanceof Rectangle)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		});

		for(int i = 0; i < board.getSize(); i++)
		{
			Stone current = board.get(i);
			Rectangle rect = new Rectangle();
			rect.widthProperty().bind(grid.widthProperty().subtract(18).divide(10));
			rect.heightProperty().bind(grid.heightProperty().subtract(34).divide(18));
			rect.setFill(Color.web(current.getColor()));

			int x = i / 10;

			grid.add(rect, i % 10, x);
			GridPane.setHgrow(rect, Priority.ALWAYS);
			GridPane.setVgrow(rect, Priority.ALWAYS);
		}

		grid.setMinWidth(200);
		grid.setMinHeight(350);

		AnchorPane.setLeftAnchor(grid, 25.0);
		AnchorPane.setRightAnchor(grid, 25.0);
		AnchorPane.setTopAnchor(grid, 25.0);
		AnchorPane.setBottomAnchor(grid, 25.0);
	}

	/**
	 * dreht den aktuellen Block um eine Einheit nach links
	 * @param event ActionEvent
	 */
	@FXML
	private void left(ActionEvent event)
	{
		board = block.moveLeft(board);
		setGrid();
	}

	/**
	 * dreht den aktuellen Block um eine Einheit nach rechts
	 * @param event ActionEvent
	 */
	@FXML
	private void right(ActionEvent event)
	{
		board = block.moveRight(board);
		setGrid();
	}

	/**
	 * bewegt den aktuellen Block um ein Feld nach unten
	 * @param event ActionEvent
	 */
	@FXML
	private void down(ActionEvent event)
	{
		board = block.moveDown(board);
		setGrid();
	}

	/**
	 * dreht den aktuellen Block um 90° nach links
	 * @param event ActionEvent
	 */
	@FXML
	private void rotateLeft(ActionEvent event)
	{
		board = block.rotateLeft(board);
		setGrid();
	}

	/**
	 * dreht den aktuellen Block um 90° nach rechts
	 * @param event ActionEvent
	 */
	@FXML
	private void rotateRight(ActionEvent event)
	{
		board = block.rotateRight(board);
		setGrid();
	}

	/**
	 * setzt den Punktestand
	 * @param newPoints int - Punktestand
	 */
	private void setPoints(int newPoints)
	{
		points += newPoints;
	}

	/**
	 * generiert einen neuen Block zufälligen Typs, zufälliger Farbe und zufälliger Rotation
	 * @return boolean - Block kann in das Spielfeld eingefügt werden (ausreichend Platz vorhanden)
	 */
	private boolean createNewBlock()
	{
		int randomColor = (int)(Math.random() * 7);
		String color = "";
		switch(randomColor)
		{
			case 0:
				color = "#FF0000";
				break;
			case 1:
				color = "#00FF00";
				break;
			case 2:
				color = "#0000FF";
				break;
			case 3:
				color = "#FFFF00";
				break;
			case 4:
				color = "#00FFFF";
				break;
			case 5:
				color = "#AA00FF";
				break;
			case 6:
				color = "#FFA500";
				break;
		}

		int rotation = (int)(Math.random() * 4);

		int randomBlock = (int)(Math.random() * 7);
		switch(randomBlock)
		{
			case 0:
				block = new Block_T(color, rotation);
				break;
			case 1:
				block = new Block_L_Left(color, rotation);
				break;
			case 2:
				block = new Block_L_Right(color, rotation);
				break;
			case 3:
				block = new Block_I(color, rotation);
				break;
			case 4:
				block = new Block_Square(color, rotation);
				break;
			case 5:
				block = new Block_S(color, rotation);
				break;
			case 6:
				block = new Block_Z(color, rotation);
				break;
		}
				
		if(block.canInsert(board))
		{		
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * fügt den zuvor mit createNewBlock() erzeugten Block in das Spielfeld ein
	 */
	private void insertBlock()
	{
		board = block.insertBlock(board);	
		setGrid();
	}

	/**
	 * setzt den Wert des Fortschrittsbalkens
	 */
	private void setProgress()
	{
		if(level.getID() == - 1)
		{
			Platform.runLater(() -> {
				labelProgress.setText("" + points);
				progressBar.setDisable(true);
			});
		}
		else
		{
			Platform.runLater(() -> {
				progressBar.setDisable(false);
				labelProgress.setText(points + " / " + level.getLevelPoints());
				progressBar.setProgress((double)points / level.getLevelPoints());
			});
		}
	}

	/**
	 * Cheatfunktionalität 
	 * (aufgerufen, wenn Taste "W" gedrückt wird)
	 */
	private void cheat()
	{
		points = level.getLevelPoints();
	}

	/**
	 * öffnet eine neue Stage mit einer Übersicht über die Achievements
	 */
	@FXML
	private void buttonAchievements()
	{ 	
		Stage newStage = new Stage();
		newStage.setMinHeight(250);
		newStage.setMinWidth(250);

		AnchorPane root = new AnchorPane();

		try
		{
			handler.loadAchievements();
		}
		catch(Exception e)
		{
		}

		AnchorPane list = handler.getAchievementList();
		AnchorPane summary = handler.getSummary();

		root.getChildren().add(summary);
		root.getChildren().add(list);
		
		newStage.setResizable(false);

		AnchorPane.setTopAnchor(summary, 50.0);
		AnchorPane.setLeftAnchor(summary, 25.0);
		AnchorPane.setRightAnchor(summary, 50.0);

		AnchorPane.setTopAnchor(list, 180.0);
		AnchorPane.setLeftAnchor(list, 25.0);
		AnchorPane.setRightAnchor(list, 25.0);
		AnchorPane.setBottomAnchor(list, 25.0);

		root.setStyle("-fx-background-color: #3F3F3F;");

		Scene scene = new Scene(root, 800, 600);
		newStage.setScene(scene);

		newStage.setTitle("Achievements");
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.getIcons().add(new Image("/images/icon.png"));
		newStage.setResizable(true);
		newStage.show();
	}

	/**
	 * erzeugt die Datei in der die Achievements gespeichert werden und legt die Achievements neu an
	 */
	private void createAchievements()
	{		
		AchievementHandler handler = new AchievementHandler(stage);
		handler.setPath(savePath + "achievements.save");
		handler.addAchievement(new Achievement("Anfänger", "Gewinne Level 1", null, null, Status.LOCKED));
		handler.addAchievement(new Achievement("Fortgeschrittener", "Beende Level 5", null, null, Status.LOCKED, 0, 5, 0));
		handler.addAchievement(new Achievement("Experte", "Beende Level 10", null, null, Status.LOCKED, 0, 10, 0));
		handler.addAchievement(new Achievement("Süchtig", "Spiele 50 Spiele", null, null, Status.LOCKED, 0, 50, 0));
		handler.addAchievement(new Achievement("Gewinnertyp", "Gewinne 25 Spiele", null, null, Status.LOCKED, 0, 25, 0));
		handler.addAchievement(new Achievement("Punktesammler", "Sammle 10000 Punkte", null, null, Status.LOCKED, 0, 10000, 0));
		handler.addAchievement(new Achievement("Schatzjäger", "Sammle 50000 Punkte", null, null, Status.LOCKED, 0, 50000, 0));
		handler.addAchievement(new Achievement("BONUS!", "Sammle 150000 Punkte im Bonuslevel", null, null, Status.LOCKED, 0, 150000, 0));
		handler.addAchievement(new Achievement("Cheater", "Du hast die geheime Cheattaste gefunden!", null, null, Status.HIDDEN));
		handler.addAchievement(new Achievement("Unmöglich", "Klicke 5 mal auf das Logo", null, null, Status.HIDDEN));

		try
		{
			handler.saveAchievements();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Wird aufgerufen, wenn ein Level erfolgreich beendet wurde
	 * (Zeigt Dialogfenster, erzeugt Partikelanimation)
	 */
	private void win()
	{
		try
		{
			switch(level.getID())
			{
				case 0:
					handler.unlockAchievement(0);
					handler.incrementAchievement(1, 1);
					handler.incrementAchievement(2, 1);	
					break;
				case 1:
					handler.incrementAchievement(1, 1);
					handler.incrementAchievement(2, 1);														
					break;
				case 2:
					handler.incrementAchievement(1, 1);
					handler.incrementAchievement(2, 1);
					break;
				case 3:
					handler.incrementAchievement(1, 1);
					handler.incrementAchievement(2, 1);
					break;
				case 4:
					handler.incrementAchievement(1, 1);
					handler.incrementAchievement(2, 1);
					break;
				case 5:													
					handler.incrementAchievement(2, 1);
					break;
				case 6:														
					handler.incrementAchievement(2, 1);
					break;
				case 7:														
					handler.incrementAchievement(2, 1);
					break;
				case 8:													
					handler.incrementAchievement(2, 1);
					break;
				case 9:														
					handler.incrementAchievement(2, 1);
					break;																								
				default:
					break;
			}
			
			handler.incrementAchievement(3, 1);
			handler.incrementAchievement(4, 1);											
			handler.checkAllIncrementalAchievements();												
			handler.saveAndLoad();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
													
		//Zeigt Partikelsystem
		if(stackPane.getChildren().size() > 1)
		{
			stackPane.getChildren().remove(1);
		}
		ParticleSystem system = new ParticleSystem(stackPane.getWidth()-75, stackPane.getHeight());
		stackPane.getChildren().add(system.getCanvas());
		system.start();									
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Level geschafft!");
		alert.setHeaderText("");
		alert.setContentText("Du hast " + level.getName() + " erfolgreich beendet!");
		alert.initOwner(stage);
		Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(icon);
		alert.showAndWait();
		
		level = levelHandler.getLevel(level.getID() + 1);
		if(level.getID() == -1)
		{
			progressBar.setVisible(false);
		}

		points = 0;
		setProgress();
		labelLevel.setText(level.getName());
		board = new Board();
		setGrid();
		buttonStart.setDisable(false);	
	}

	/**
	 * Initialisierungsmethode
	 */
	public void init()
	{		
		PathUtils.checkFolder(new File(savePath));		
		
		logo.setImage(new Image("/images/icon.png"));	
		hboxLogo.setStyle("-fx-border-color: #333333; -fx-border-radius: 10; -fx-border-width: 3;");
		hboxLogo.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if(logoCounter < 5)
				{
					logoCounter++;
					if(logoCounter == 5)
					{
						try
						{
							handler.unlockAchievement(9);
							handler.saveAndLoad();
						}
						catch(Exception e)
						{							
						}
					}
				}
				else
				{					
				}
			}			
		});
		
		HBox lineOne = new HBox();

		FontIcon iconRotateLeft = new FontIcon(FontIconType.UNDO);
		iconRotateLeft.setSize(25);
		iconRotateLeft.setColor(Color.web("#333333"));

		FontIcon iconStart = new FontIcon(FontIconType.PLAY);
		iconStart.setSize(25);
		iconStart.setColor(Color.web("#333333"));

		FontIcon iconRotateRight = new FontIcon(FontIconType.REPEAT);
		iconRotateRight.setSize(25);
		iconRotateRight.setColor(Color.web("#333333"));

		buttonRotateLeft = new Button("", iconRotateLeft);
		buttonRotateLeft.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if(running)
				{
					board = block.rotateLeft(board);
					setGrid();
				}
			}
		});

		buttonStart = new Button("", iconStart);
		buttonStart.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				buttonStart.setDisable(true);
				startGame();
			}
		});

		buttonRotateRight = new Button("", iconRotateRight);
		buttonRotateRight.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if(running)
				{
					board = block.rotateRight(board);
					setGrid();
				}
			}
		});

		lineOne.getChildren().add(buttonRotateLeft);
		lineOne.getChildren().add(buttonStart);
		lineOne.getChildren().add(buttonRotateRight);
		lineOne.setAlignment(Pos.CENTER);
		HBox.setMargin(buttonRotateLeft, new Insets(5));
		HBox.setMargin(buttonStart, new Insets(5));
		HBox.setMargin(buttonRotateRight, new Insets(5));

		HBox lineTwo = new HBox();

		FontIcon iconLeft = new FontIcon(FontIconType.ARROW_LEFT);
		iconLeft.setSize(25);
		iconLeft.setColor(Color.web("#333333"));

		FontIcon iconDown = new FontIcon(FontIconType.ARROW_DOWN);
		iconDown.setSize(25);
		iconDown.setColor(Color.web("#333333"));

		FontIcon iconRight = new FontIcon(FontIconType.ARROW_RIGHT);
		iconRight.setSize(25);
		iconRight.setColor(Color.web("#333333"));

		buttonLeft = new Button("", iconLeft);
		buttonLeft.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if(running)
				{
					board = block.moveLeft(board);
					setGrid();
				}
			}
		});

		buttonDown = new Button("", iconDown);
		buttonDown.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if(running)
				{
					board = block.moveDown(board);
					setGrid();
				}
			}
		});

		buttonRight = new Button("", iconRight);
		buttonRight.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				if(running)
				{
					board = block.moveRight(board);
					setGrid();
				}
			}
		});

		lineTwo.getChildren().add(buttonLeft);
		lineTwo.getChildren().add(buttonDown);
		lineTwo.getChildren().add(buttonRight);
		lineTwo.setAlignment(Pos.CENTER);
		HBox.setMargin(buttonLeft, new Insets(5));
		HBox.setMargin(buttonDown, new Insets(5));
		HBox.setMargin(buttonRight, new Insets(5));

		vbox.getChildren().add(lineOne);
		vbox.getChildren().add(lineTwo);

		//reagiert auf die Betätigung der Tasten und lässt den Spieler so das Spiel steuern
		anchorPaneMain.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent keyEvent)
			{
				if(running)
				{
					String keyCode = keyEvent.getCode().toString();

					switch(keyCode)
					{
						case "UP":
							if(running)
							{
								block.rotateLeft(board);
								setGrid();
							}
							break;
						case "DOWN":
							if(running)
							{
								block.rotateRight(board);
								setGrid();
							}
							break;
						case "RIGHT":
							if(running)
							{
								block.moveRight(board);
								setGrid();
							}
							break;
						case "LEFT":
							if(running)
							{
								block.moveLeft(board);
								setGrid();
							}
							break;
						case "CONTROL":
							if(running)
							{
								block.moveDown(board);
								setGrid();
							}
							break;
						//Cheattaste zum direkten Gewinnen eines Levels (außer des Bonuslevels)
						case "W":							
							try
							{	handler.unlockAchievement(8);
								handler.saveAndLoad();
							}
							catch(Exception e)
							{								
							}
							cheat();
							break;
						default:
							break;
					}
				}
				
				//DEBUG Clear Achievements				
				String keyCode = keyEvent.getCode().toString();
				if(keyCode == "C")
				{
					createAchievements();
				}
			}
		});

		grid.getColumnConstraints().clear();
		double xPercentage = 1.0 / 10;
		for(int i = 0; i < 10; i++)
		{
			ColumnConstraints c = new ColumnConstraints();
			c.setPercentWidth(xPercentage * 100);
			grid.getColumnConstraints().add(c);
		}

		grid.getRowConstraints().clear();
		double yPercentage = 1.0 / 18;
		for(int i = 0; i < 18; i++)
		{
			RowConstraints c = new RowConstraints();
			c.setPercentHeight(yPercentage * 100);
			grid.getRowConstraints().add(c);
		}

		buttonLeft.setFocusTraversable(false);
		buttonRight.setFocusTraversable(false);
		buttonDown.setFocusTraversable(false);
		buttonRotateLeft.setFocusTraversable(false);
		buttonRotateRight.setFocusTraversable(false);
		
		handler = new AchievementHandler(stage);
		handler.setPath(savePath + "achievements.save");
		try
		{
			handler.loadAchievements();
		}
		catch(Exception e)
		{
			//falls die Datei nicht existiert, wird versucht die neu zu erzeugen
			createAchievements();	
			try
			{
				handler.loadAchievements();
			}
			catch(Exception ex)
			{				
			}
		}
	}
	
	/**
	 * öffnet eine neue Stage, die eine Kurzanleitung der Steuerung beinhaltet
	 */
	@FXML
	private void controls()
	{
		Stage newStage = new Stage();		

		AnchorPane root = new AnchorPane();
		ImageView controlsView = new ImageView();
		controlsView.setImage(new Image("/images/controls.png"));	
		controlsView.setFitWidth(500);
		controlsView.setFitHeight(300);
		
		root.getChildren().add(controlsView);
		
		Scene scene = new Scene(root, 490, 290);
		newStage.setScene(scene);

		newStage.setTitle("Steuerung");
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.getIcons().add(new Image("/images/icon.png"));
		newStage.setResizable(false);
		newStage.show();
	}

	/**
	 * zeigt ein Dialogfenster an mit Informationen über den Autor sowie externe Quellen
	 */
	@FXML
	private void about()
	{	
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About " + bundle.getString("app.name"));
		alert.setHeaderText(bundle.getString("app.name"));
		alert.setContentText("Version:     " + bundle.getString("version.name") + "\r\nDate:         " + bundle.getString("version.date") + "\r\nAuthor:      Robert Goldmann\r\n\r\nexternal sources: Freepik, fontAwesome, FreeSound.org");
		Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(icon);
		alert.getDialogPane().setPrefHeight(250.0);
		alert.showAndWait();
	}
}