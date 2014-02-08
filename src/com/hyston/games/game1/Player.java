package com.hyston.games.game1;


import javax.microedition.khronos.opengles.GL10;

public class Player extends Creature 
{
	public	boolean isMoving = false;
	
	
	Player(float x, float y)
	{
		super(x,y, 0.1f, 0.0f, 0.0f, 0.0005f, R.drawable.player);
		this.color = new Geometry.Color(0.7f, 0.1f, 0.1f);
		
	    lastTimeMoving = System.currentTimeMillis();
	    weapon = new Gun();
	}
	
	
	public void setMoving(boolean isMoving)
	{
		
		if(isMoving && !this.isMoving) //TODO: not really good, think about global loops
			lastTimeMoving = System.currentTimeMillis();
		this.isMoving = isMoving;
	}
	
	
	public void Draw(GL10 gl)
	{	
		super.Draw(gl);
		
		if(isMoving)
		{
			this.move();
			gManager.camera.x = this.x;
			gManager.camera.y = this.y;			
		}	
	}
	
	
	/*public void Fire()
	{
		gManager.ballistics.addBullet(this.x, this.y, this.lookAngle);
	}*/
}
