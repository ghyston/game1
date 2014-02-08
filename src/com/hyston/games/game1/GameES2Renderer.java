package com.hyston.games.game1;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView.Renderer;
/*import android.opengl.GLES20;
import android.util.FloatMath;
import java.lang.Math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;*/


public class GameES2Renderer implements Renderer {	
	
	int screenWidth;
	int screenHeight;
	float ratio = 1.0f;
	GameManager gManager;
	
		
	
	public GameES2Renderer()
	{
		this.gManager = GameManager.getInstance();
	}

	//@Override
	public void onDrawFrame(GL10 gl)
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
	    if(ratio > 1) //TODO: remove this dirty hack!
	    	gl.glOrthof(this.gManager.camera.x - 1.0f, this.gManager.camera.x + 1.0f, this.gManager.camera.y - ratio, this.gManager.camera.y + ratio, -1.0f, 1.0f);
	    else
	    	gl.glOrthof(this.gManager.camera.x - 1/ratio, this.gManager.camera.x + 1/ratio, this.gManager.camera.y - 1.0f, this.gManager.camera.y + 1.0f, -1.0f, 1.0f);
	    
		gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    
	    gManager.moveAll();
	    gManager.drawAll(gl);	    
	}

	//@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		gl.glViewport(0, 0, width, height);	
	    ratio = (float) height / width;
	    //gManager.controller.setWeightAndHeight(width, height);
	    gManager.gui.setWeightAndHeight(width, height);
	}

	//@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig arg1)
	{
		 gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	}
	
}
