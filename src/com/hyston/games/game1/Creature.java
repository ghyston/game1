package com.hyston.games.game1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

public class Creature
{
	float x;
	float y;
	float radius = 0.1f; //player radius
	float moveAngle = 0.0f;
	float lookAngle = 0.0f;
	float speed = 0.0005f;
	int health = 100;
	boolean calcCollision = true;
	long lastTimeMoving;
	GameManager gManager;
	FloatBuffer xBuffer;
	Geometry.Color color;
	glSprite sprite;
	int texture_resource_id;
	BaseWeapon weapon = null;
	
	public static final int CREATURE_IDLE = 0;
	public static final int CREATURE_ATTACK = 1;
	public static final int CREATURE_DEAD = 2;
	
	int currentState = 0;

	
	Creature(float x, float y,	float radius, float moveAngle, float lookAngle,	float speed, int texture_resource_id)
	{
		this.texture_resource_id = texture_resource_id;
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.moveAngle = moveAngle;
		this.lookAngle = lookAngle;
		this.speed = speed;
		this.gManager = GameManager.getInstance();
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(3 * 2 * 4); //This shit still can look much better!
	    vbb.order(ByteOrder.nativeOrder());
	    xBuffer = vbb.asFloatBuffer();
	    sprite = new glSprite(this.radius, this.radius, texture_resource_id);
		sprite.init();
	}
	
	//TODO: refact this!
	protected void move()
	{
		float stepLong = speed * (System.currentTimeMillis() - lastTimeMoving); //TODO: make it by GameTimer::FloatTimer
		lastTimeMoving = System.currentTimeMillis();
		float newX = this.x + FloatMath.cos(moveAngle) * stepLong;
		float newY = this.y + FloatMath.sin(moveAngle) * stepLong;
		
		if(calcCollision)
		{
			Geometry.Vector newVec = CollisionDetector.getPlayerSceneCollision( //TODO: why player???
				new Geometry.Vector(this.x, this.y),
				new Geometry.Vector(newX, newY),
				gManager.scene);
			newX += newVec.x;
			newY += newVec.y;
		}
		
		this.x = newX;
		this.y = newY;

	}
	
	//TODO: remove GL10!
	public void Draw(GL10 gl)
	{
		sprite.Draw(x, y, lookAngle);
	}
	
	public void attack()
	{
		if(weapon != null)
		{
			weapon.attack(x, y, lookAngle);
		}
	}


}