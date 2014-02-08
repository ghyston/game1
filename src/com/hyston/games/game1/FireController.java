package com.hyston.games.game1;

class FireController extends BaseController
{			
	FireController(float x, float y, float width, float height)
	{
		super(x,y,width, height, R.drawable.fire_ctrl);
	}
	
	public void onTouch(float x, float y)
	{
		super.onTouch(x,y);
		gm.player.attack();
	}
	
	public void onMove(float x, float y)
	{
		
	}
}