package com.hyston.games.game1;

public class Gun extends BaseWeapon 
{
	void attack(float x, float y, float angle)
	{
		GameManager.getInstance().ballistics.addBullet(x, y, angle, 0.1f);
	}
}
