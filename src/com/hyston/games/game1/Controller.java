package com.hyston.games.game1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;
import android.view.MotionEvent;

import javax.microedition.khronos.opengles.GL10;


//TODO: REMOVE THIS WHOLE SHIT!!
public class Controller
{
	FloatBuffer xBuffer; //4 moveController	
	FloatBuffer fBuffer;// 4 fireController	
	FloatBuffer gBuffer; // 4 goto line
	
	boolean mcTouched = false; //TODO: make separate class
	boolean gcTouched = false;
	
	int screenWidth;
	int screenHeight;
	GameManager gManager;
	List<MotionEvent> touches; //List of current touches. First: make it to move controller
	
	float red = 0.0f; //todo: this is 4 fireController
	float gotoX = 0.0f;
	float gotoY = 0.0f;
	float size = 100f; 	
	
	public static final int MOTIONTYPE_MOVE = 1;
	public static final int MOTIONTYPE_FIRE = 2;
	public static final int MOTIONTYPE_GOTO = 3;
	
	Controller() //TODO: make it circle, not so ugly! :)
	{
		
		this.gManager = GameManager.getInstance();
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(4 * 2 * 4); //This shit still can look much better!
	    vbb.order(ByteOrder.nativeOrder());
	    xBuffer = vbb.asFloatBuffer();
	    
	    ByteBuffer vbb1 = ByteBuffer.allocateDirect(4 * 2 * 4); //ALL THIS SHIT MUST BE RE-WRITED!
	    vbb1.order(ByteOrder.nativeOrder());
	    fBuffer = vbb1.asFloatBuffer();
	    
	    ByteBuffer vbb2 = ByteBuffer.allocateDirect(2 * 2 * 4); //I have said, ALLLL!!!! >-(
	    vbb2.order(ByteOrder.nativeOrder());
	    gBuffer = vbb2.asFloatBuffer();
	    
	    setSize();			
	}
	
	
	/*public void setWeightAndHeight(int screenWidth, int screenHeight)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.size = (float)screenWidth / 4.5f;
		setSize();
	}*/
	
	public void Draw(GL10 gl)
	{	
		gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
	    gl.glOrthof(0.0f, screenWidth, 0.0f, screenHeight, -1.0f, 1.0f);
	    
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColor4f(0.2f, 0.2f, 1.0f, 0.5f);	
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, this.xBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glColor4f(red, 0.0f, 0.0f, 0.5f);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, this.fBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	public void DrawLine(GL10 gl)
	{
		this.gBuffer.clear();			
		float[] gotoLine = {this.gotoX, this.gotoY,gManager.player.x, gManager.player.y}; //memory leak?
		this.gBuffer.put(gotoLine);
		this.gBuffer.position(0);	
		
		gl.glColor4f(1.0f, 0.1f, 0.1f, 1.0f);		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, this.gBuffer);
		gl.glDrawArrays(GL10.GL_LINES, 0, 2);		
	}
	
	private void setSize()
	{
		float[] coords = {0.0f, size,
				  0.0f, 0.0f,
				  size, size,
				  size, 0.0f};
		xBuffer.clear();
		xBuffer.put(coords);		  
		xBuffer.position(0);
		
		float[] fireCoords = {
				screenWidth - size, size,
				screenWidth - size, 0.0f,				
				screenWidth, size,
				screenWidth, 0.0f};
		fBuffer.clear();
		fBuffer.put(fireCoords);		  
		fBuffer.position(0);
	}
	
	public int getControllerType(float x, float y)
	{
		if(y > screenHeight - size)
		{
			if(x < size)
				return MOTIONTYPE_MOVE;
			if(x > screenWidth - size)
				return MOTIONTYPE_FIRE;
		}
		return MOTIONTYPE_GOTO;		
	}
	
	
	public void setFire(boolean isFire)
	{
		red = isFire ? 1.0f : 0.0f;
		if(isFire)
		{
			//gManager.player.Fire();
		}
	}
	
	
	public void mcOnTouch(float x, float y)
	{
		mcTouched = true;
		float centerX = size/2;
		float centerY = size/2;			
		float touchX = x;
		float touchY = y - screenHeight + size;
		
		gManager.player.lookAngle = -(float) Math.atan2(touchY - centerY, touchX - centerX);
		if(!gcTouched)
		{
			//gManager.player.moveAngle = gManager.player.lookAngle;
			//gManager.player.setMoving(true);
		}
	}
	
	public void sensorOnRotate(float x, float y)
	{
		if(x < -0.3 || x > 0.3 || y < -0.3 || y > 0.3)
		{
			//gManager.player.lookAngle = (float) Math.atan2(-x, y);
			gManager.player.moveAngle = (float) Math.atan2(-x, y);
			gManager.player.setMoving(true);
		}
		else
			gManager.player.setMoving(false);
	}
	
	public void mcOnRelease()
	{
		mcTouched = false;
		if(!gcTouched)
		{
			gManager.player.setMoving(false);
		}
		
	}
	
	public void gcOnTouch(float x, float y)
	{
		if(!gcTouched)
		{
			gcTouched = true;
			this.gotoX = gManager.player.x + x*2.0f/screenWidth - 1.0f;
			this.gotoY = gManager.player.y - y*2.0f/screenHeight + 1.0f;
		
			gManager.player.moveAngle = (float) Math.atan2(this.gotoY - gManager.player.y, this.gotoX - gManager.player.x);
			gManager.player.setMoving(true);
		}
		else
			gcOnRelease();
	}
	
	public void gcOnRelease()
	{
		gcTouched = false;
		if(!mcTouched)
			gManager.player.setMoving(false);
	}
	
	/*TODO:
	 * I think not to refact it now, but keep it 4 future, 
	 * when I will already define all necessary controllers.
	 * Also, it will fix bug when goto_controller is released and
	 * move_controller -  not, but mc doesn't moving.
	 */
	 
	

}
