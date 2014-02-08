package com.hyston.games.game1;

import java.util.Random;

public class Shotgun extends BaseWeapon 
{
	public void attack(float x, float y, float angle)
	{
		Random random = new Random();
		for(int i=0; i < 5; i++)
		{
			float dAngle = (random.nextFloat() - 0.5f);
			float dSpeed = 0.1f + (random.nextFloat() / 5.0f);
			GameManager.getInstance().ballistics.addBullet(x, y, angle + dAngle, dSpeed);			
		}
		
	}

}
