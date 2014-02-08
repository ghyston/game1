package com.hyston.games.game1;

import android.util.Log;

public class WeaponController extends BaseController
{	
	//TODO: painfully hardcode, remove it! Make players bag with all weapon items etc
	int current_weapon;  //0 - pistol, 1 - shotgun
	
	WeaponController(float x, float y, float width, float height)
	{
		super(x,y,width, height,R.drawable.pistol);
		current_weapon = 0;
	}
	
	public void onTouch(float x, float y)
	{
		
		super.onTouch(x,y);		
		current_weapon = (current_weapon == 0) ? 1 : 0;
		chooseWeapon();
	}
	
	private void chooseWeapon()
	{
		if(current_weapon == 0)
		{
			//TODO: completely wrong. texture id should be keeped on controller OR in sprite
			sprite.resource_id = R.drawable.pistol;
			gm.player.weapon = new Gun();			
		}
		else if(current_weapon == 1)
		{
			sprite.resource_id = R.drawable.shotgun;
			gm.player.weapon = new Shotgun();			
		}
		sprite.texture_loaded = false;
	}
}
