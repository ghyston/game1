package com.hyston.games.game1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;
import javax.microedition.khronos.opengles.GL10;

public class Zombie extends Creature
{
	int idNextWaypoint = 0;
	Random random = new Random();
	
	
	Zombie(float x, float y)
	{
		super(x,y, 0.1f, 0.0f, 0.0f, 0.0004f, R.drawable.zombie);
		this.color = new Geometry.Color(0.5f, 0.5f, 0.1f);
		this.calcCollision = true; //TODO: on moving by waypoints this param can be false
		this.lastTimeMoving = System.currentTimeMillis();
		switchState(CREATURE_IDLE); 
	}
	
	//TODO: one state now only standby
	public void switchState(int newState)
	{
		this.currentState = newState;
		switch(newState)
		{
			case CREATURE_IDLE:
				//this.calcCollision = false;
				if(idNextWaypoint == 0)
					idNextWaypoint = searchNearestWaypoint();
			break;
			
			case CREATURE_DEAD:
				//this.color = new Geometry.Color(0.5f, 0.0f, 0.0f);
				//TODO: this is wholy hardcode, but just 4 fun!
				this.texture_resource_id = R.drawable.zombie_dead;
				this.sprite.resource_id = texture_resource_id ; 
				this.sprite.texture_loaded = false;
				this.sprite.init();
				
				this.speed = 0.0f; //TODO: make it some way not to calc move()
			break;
			
			case CREATURE_ATTACK:
				//this.calcCollision = true;
				break;				
		}
		
	}
	
	private int searchNearestWaypoint()
	{
		float dest = 1000.0f;
		int id = 0;
		
		for(Scene.Waypoint w: gManager.waypoints)
		{
			float newDest = (new Geometry.Vector(w.x, this.x)).length();
			if(dest > newDest)
			{
				dest = newDest;
				id = w.id;
			}			
		}		
		return id;		
	}
	
	public void Draw(GL10 gl)
	{	
		super.Draw(gl);
	}
	
	//TODO: make it for different behavior on different states on different enemies on different planets
	public void move()
	{		
		this.calcState();
		
		switch(this.currentState)
		{
			case CREATURE_IDLE:
			
			Scene.Waypoint wp = gManager.getWaypointById(idNextWaypoint);//gManager.waypoints.get(idNextWaypoint);
			if(CollisionDetector.isCircleNearCircle(
				new Geometry.Circle(this.x, this.y, this.radius), 
				new Geometry.Circle(wp.x, wp.y, 0.001f)))
			{
				int next = random.nextInt(wp.waypoints.size());
				idNextWaypoint = wp.waypoints.get(next);
				wp = gManager.getWaypointById(idNextWaypoint);//gManager.waypoints.get(idNextWaypoint);			
			}
				
			if(idNextWaypoint != 0)
			{
				this.moveAngle = (float) Math.atan2(wp.y - this.y, wp.x - this.x);
				this.lookAngle = this.moveAngle; 
			}
			break;
			
			case CREATURE_DEAD:
				//zombie is always dead.. philosophic problem :'(
			break;
			
			case CREATURE_ATTACK:
				this.moveAngle = (float) Math.atan2(gManager.player.y - this.y, gManager.player.x - this.x);
				this.lookAngle = this.moveAngle;
			break;				
			
		}
		/*if(!CollisionDetector.isCircleNearCircle(
				new Geometry.Circle(this.x, this.y, this.radius), 
				new Geometry.Circle(gManager.player.x, gManager.player.y, gManager.player.radius)))
		{
			super.move();
		}*/
		super.move();
	}
	
	private void calcState()
	{
		//move it to Creature class?
		boolean playerVisible = CollisionDetector.getVisibilityPlayer(
			new Geometry.Vector(this.x, this.y),
			new Geometry.Vector(gManager.player.x, gManager.player.y),
			gManager.scene);
		
	//	boolean playerVisible = false;
		
		if(this.currentState == CREATURE_IDLE && playerVisible)
			switchState(CREATURE_ATTACK);
		else if(this.currentState == CREATURE_ATTACK && !playerVisible)
			switchState(CREATURE_IDLE);
				
		/*if(playerVisible)
			switchState(CREATURE_ATTACK);
		else
			switchState(CREATURE_IDLE);*/
		
	}
	
}
