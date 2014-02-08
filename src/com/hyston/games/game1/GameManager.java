package com.hyston.games.game1;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import android.content.*;

public class GameManager
{
	//This is singletone-things
	private static GameManager instance = new GameManager();
	
	public static GameManager getInstance()
	{
		return instance;
	}	
	
	
	public
	Camera camera;
	Grid grid;
	Player player;
	Controller controller;
	Scene scene;
	Ballistics ballistics;
	GameTimer gTimer;
	GameGUI gui;
	GlPainter glPainter;
	Context appContext;
	GL10 gl = null;
	
	ArrayList<Zombie> zombies = new ArrayList<Zombie>();
	ArrayList<Scene.Waypoint> waypoints = new ArrayList<Scene.Waypoint>(); //TODO: why here?? It is scene-specific!
	//Solder solder; //TODO: this is just a test
	//TODO: may be create class scene-loader?
	
	GameManager()
	{
		//onInit()?		
	}
	
	public void init(Context context)
	{
		camera = new Camera(0.0f, 0.0f, 1.0f); //TODO: init this from scene!
		grid = new Grid();					
		scene = new Scene();
		scene.init();
		player = new Player(2.5f, 1.5f); //TODO: init this from scene!
		gui = new GameGUI();
		//gui.init();
		//controller = new Controller();
		ballistics = new Ballistics();
		gTimer = new GameTimer();
		glPainter = new GlPainter();
		gTimer.start();
		appContext = context;
		//solder = new Solder(0.4f, 0.4f);
		
		gTimer.addEvent(new GameTimer.GameEvent(3000, this) //just 4 fun
		{
			public void run()
			{
				gm.addZombie(0.0f, 0.0f);
			}
		});
	}
	
	public void drawAll(GL10 gl)
	{
		CollisionDetector.gl = gl;
		this.gl = gl;
		grid.Draw(gl);
		scene.Draw(gl);
		for(Zombie z : zombies)
		{
			z.Draw(gl);
		}
		player.Draw(gl);	
		
		//solder.Draw(gl);
		ballistics.Draw(gl);
	//	controller.Draw(gl); 
		//gui.Draw();//interface should draw at last (here calls glOrtho)
		glPainter.drawGui(gl);
	}
	
	
	private void draw(Creature c)
	{
		
	}
	
	public void moveAll()
	{
		//TODO: make all movement (player, enemies, doors etc) here!!
		ballistics.move();
		gTimer.idle();
		
		for(Zombie z : zombies)
		{
			z.move();
		}
	}
	
	public void addZombie(float x, float y)
	{
		this.zombies.add(new Zombie(x,y));
	}
	
	//TODO: remove this fucking shit ASAP!!!!
	public Scene.Waypoint getWaypointById(int id)
	{
		Scene.Waypoint result = null;
		for(Scene.Waypoint wp : waypoints)
		{
			if(wp.id == id)
			{
				result = wp;
				break;
			}
		}
		return result;
		
	}
	
	//TODO: move it somewhere else!
	public void sensorOnRotate(float x, float y)
	{
		if(x < -0.3 || x > 0.3 || y < -0.3 || y > 0.3)
		{
			//gManager.player.lookAngle = (float) Math.atan2(-x, y);
			player.moveAngle = (float) Math.atan2(-y, -x);
			player.setMoving(true);
		}
		else
			player.setMoving(false);
	}

}
