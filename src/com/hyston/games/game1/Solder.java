package com.hyston.games.game1;

import javax.microedition.khronos.opengles.GL10;

//TODO: this class is just on the begining of long way..
public class Solder extends Creature
{
	glSprite sprite;
	
	Solder(float x, float y)
	{
		super(x,y, 0.1f, 0.0f, 0.0f, 0.001f, R.drawable.solder);
		this.color = new Geometry.Color(0.1f, 0.1f, 0.5f);
		this.calcCollision = false;
		this.lastTimeMoving = System.currentTimeMillis();
		
		//sprite = new glSprite(this.radius, this.radius);
		//sprite.init();
		
	}
	
	public void Draw(GL10 gl)
	{
		super.Draw(gl);
		//sprite.Draw(x, y, lookAngle);
		
	}

}
