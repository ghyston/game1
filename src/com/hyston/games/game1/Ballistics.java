package com.hyston.games.game1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import android.util.FloatMath;
import java.util.Vector;
import android.util.Log;
import java.util.Iterator;

public class Ballistics
{
	Vector<Bullet> bullets = new Vector<Bullet>();
	FloatBuffer xBuffer;
	public static final int MAX_BULLETS_COUNT = 256;
	GameManager gm;
	Bullet newBullet = null;
	
	Ballistics()
	{
		ByteBuffer vbb = ByteBuffer.allocateDirect(2 * 4 * MAX_BULLETS_COUNT); //This shit still can look much better!
	    vbb.order(ByteOrder.nativeOrder());
	    xBuffer = vbb.asFloatBuffer();
	    this.gm = GameManager.getInstance();
	}
	
	private class Bullet
	{
		float x;
		float y;
		float moveAngle;
		float speed = 0.1f;
		
		Bullet(float x, float y,float moveAngle, float speed)
		{
			this.x = x;
			this.y = y;
			this.moveAngle = moveAngle;			
			this.speed = speed;
		}
		
		public boolean move(GameManager gm)
		{
			//TODO: this work strange, fix that
			//this.speed *= 0.99;
			//if(this.speed < 0.01f)
			//				return false;
			Geometry.Vector move = new Geometry.Vector(FloatMath.cos(moveAngle) * speed, FloatMath.sin(moveAngle) * speed);
			//add collision here
			for(Zombie z : gm.zombies)
			{
				if(z.currentState != Creature.CREATURE_DEAD && CollisionDetector.bulletAndCircle(new Geometry.Vector(this.x, this.y), move, new Geometry.Circle(z.x, z.y, z.radius)))
				{	
					//this is to detect hit
					z.health -= 30;
					if(z.health < 0)
					{
						z.switchState(Creature.CREATURE_DEAD);
					}
					return false;
				}
			}
			
			this.x += move.x;
			this.y += move.y;
			
			return !CollisionDetector.getBulletSceneCollision(new Geometry.Vector (this.x, this.y), gm.scene);
		}
	}
	
	public void addBullet(float x, float y, float moveAngle, float speed)
	{			
		if(bullets.size() < MAX_BULLETS_COUNT)
			bullets.add(new Bullet(x,y,moveAngle, speed));
		//bullets.add(new Bullet(x,y,moveAngle));
			
			
		
		//newBullet = new Bullet(x,y,moveAngle);
	}
	
	public void move()
	{
		/*if(newBullet != null) //TODO: newBullet is not thread-secure! 
		{
			bullets.add(newBullet);
			newBullet = null;
		}*/
			
		Iterator<Bullet> bulletIterator = bullets.iterator();
		while(bulletIterator.hasNext())
		{
			Bullet b = (Bullet)bulletIterator.next();
			if(!b.move(this.gm))
			{
				synchronized(bullets)
				{
					bulletIterator.remove();
				}
			}
			
		}
		/*for(Bullet b : bullets)
		{
			if(!b.move())
				bullets.removeElement(b);
		}*/
	}
	
	public void Draw(GL10 gl)
	{
		this.xBuffer.clear();		
		
		float[] coords = new float[bullets.size() * 2] ;
		int temp = 0;
		
		for(Bullet b : bullets)
		{
			coords[temp*2] = b.x;
			coords[temp*2 + 1] = b.y;
			temp++;
		}
				  
		this.xBuffer.put(coords);
		this.xBuffer.position(0);	
		
		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
		//gl.glColor4f(0.7f, 0.1f, 0.1f, 1.0f);		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPointSize(5.0f);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, this.xBuffer);
		gl.glDrawArrays(GL10.GL_POINTS, 0, bullets.size());
			
	}

}
