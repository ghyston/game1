package com.hyston.games.game1;

public abstract class BaseWeapon 
{
	float atomic_damage;
	float reloading_time;
	float damage_per_second; //??
	float hit_count;
	float radius;
	//Ballistics::Bullet bullet_type; //TODO: make it HitObject TODO: merge it with bllistics
	
	
	abstract void attack(float x, float y, float angle);
	
}
