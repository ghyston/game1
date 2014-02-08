package com.hyston.games.game1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;


public class glSprite
{
	private 
	FloatBuffer vfb; //vertex buffer
	FloatBuffer tfb; //texture buffer
	float width;
	float height;
	int resource_id;
	int[] texture = new int[1];
	public boolean texture_loaded = false; //NOT A GOOD SOLUTION!
	
	glSprite(float width, float height, int resource_id)
	{
		this.height = height;
		this.width = width;
		this.resource_id = resource_id;
	}
	
	void init()
	{
		float vertex_coords[] = 
			{
				-width , -height ,
				-width ,  height ,
				 width , -height ,
				 width ,  height 
			};
		
		//vertex
		ByteBuffer vbb = ByteBuffer.allocateDirect(4 * 2 * 4); //4 points * 2 coords per point * 4 bytes per float
		vbb.order(ByteOrder.nativeOrder());
		vfb = vbb.asFloatBuffer();
		vfb.clear();
		vfb.put(vertex_coords);
		vfb.position(0);
			    
			    // buffer holding the texture coordinates
		float texture_coords[] = 
		{
			0.0f, 0.0f,		
			0.0f, 1.0f,
			1.0f, 0.0f,		
			1.0f, 1.0f
		};
			    
				//texture
		ByteBuffer tbb = ByteBuffer.allocateDirect(4 * 8);
		tbb.order(ByteOrder.nativeOrder());	    
		tfb = tbb.asFloatBuffer();
		tfb.clear();
		tfb.put(texture_coords);	    
		tfb.position(0);
		
		if(!texture_loaded)
			texture_loaded = loadGLTexture(resource_id);
	}
	
	
	void Draw(float x, float y, float angle)
	{
		GameManager gm = GameManager.getInstance();
		GL10 gl = gm.gl;
		
		if(!texture_loaded)
			texture_loaded = loadGLTexture(resource_id);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0.0f);
		gl.glRotatef(angle * 57.2958f, 0.0f, 0.0f, 1.0f);
		
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
	    gl.glEnable(GL10.GL_TEXTURE_2D);	    
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);	    
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
	    
	    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, tfb);		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vfb);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_BLEND);
		gl.glPopMatrix();
	}
	
	//TODO: add checking existance resource!
	public boolean loadGLTexture(int resourse_id)
	{	
		GameManager gm = GameManager.getInstance();
		GL10 gl = gm.gl;
		
		if(gl == null)
			return false;
		
	    // loading texture
	    Bitmap bitmap = BitmapFactory.decodeResource(gm.appContext.getResources(), resourse_id);
	    if(bitmap == null)
	    {
	    	Log.d("glSprite::loadGLTexture", "bitmap not loaded");
	    	return false;
	    }
	    
	    //bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
	    
	    // generate one texture pointer
	    
	    
	    gl.glGenTextures(1, texture, 0);
	    
	    // ...and bind it to our array
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
	    
	    // create nearest filtered texture
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);	 

	    // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
	    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	    
	    // Clean up
	    bitmap.recycle();
	    return true;
	}
	
	

}
