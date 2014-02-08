package com.hyston.games.game1;

import android.util.Log;

class LookController extends BaseController
{
	int texture_resourse_id = R.drawable.move_ctrl;
	
	LookController(float x, float y, float width, float height)
	{
		super(x,y,width, height, R.drawable.move_ctrl);
		texture_resourse_id = R.drawable.move_ctrl;
	}
	
	//TODO: Code review here
	public void onTouch(float x, float y)
	{
		super.onTouch(x,y);
		gm.player.lookAngle = -(float) Math.atan2(y - this.y, x - this.x);
		Log.d("LookController::onTouch()", "player_angle: " + gm.player.lookAngle);
	}
	
	public void onMove(float x, float y)
	{
		this.onTouch(x, y);
	}
}
