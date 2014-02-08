package com.hyston.games.game1;

import javax.microedition.khronos.opengles.GL10;

abstract class BaseController
{
	protected boolean isTouch = false;
	protected float x;
	protected float y;
	protected float height;
	protected float width;
	protected GameManager gm;
	//protected int texture_id = -1;
	protected int texture_resourse_id;
	protected glSprite sprite;
	
	
	public void onTouch(float x, float y)
	{
		this.isTouch = true;
	}
	
	public void onMove(float x, float y)
	{
		this.isTouch = true;		
	}
	
	public void onRelease()
	{
		this.isTouch = false;
	}
	
	//not really beautifully
	BaseController()
	{
		this.x = 0.0f;
		this.y = 0.0f;
		this.height = 0.0f;
		this.width = 0.0f;
		this.gm = GameManager.getInstance();
		sprite = new glSprite(width, height, texture_resourse_id);
		sprite.init();
	}
	
	BaseController(float x, float y, float width, float height, int resource_id)
	{
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.gm = GameManager.getInstance();
		this.texture_resourse_id = resource_id;
		sprite = new glSprite(width, height, texture_resourse_id);
		sprite.init();
	}
	
	//TODO: Do we really need that?
	public void loadSprite(GL10 gl)
	{
		sprite = new glSprite(width, height, texture_resourse_id);
		
		//texture_id = gm.glPainter.loadGLTexture(gl, texture_resourse_id);
	}
}