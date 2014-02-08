package com.hyston.games.game1;


import java.util.ListIterator;
import javax.microedition.khronos.opengles.GL10;


public class GlPainter 
{
	GameManager gm;
	
	//private int[] textures = new int[2]; //texture array. TODO: make it java-pro-ArrayHashListMap<int>-style
	//private int texture_count = 0; 
	
	GlPainter()
	{
		gm = GameManager.getInstance();
	}	
	
	
	
	public void drawGui(GL10 gl)
	{		
		int count = gm.gui.controllers.size();	
		if(count <= 0)
			return;
	    
	    gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
	    gl.glOrthof(0.0f, gm.gui.screenWidth, gm.gui.screenHeight, 0.0f, -1.0f, 1.0f);
	    
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    
		//gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glColor4f(0.2f, 0.2f, 1.0f, 0.5f);	
		
		ListIterator<BaseController> iter = gm.gui.controllers.listIterator();
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		while(iter.hasNext())
		{			
			BaseController bc = iter.next();
			//gl.glColor4f(bc.color_red, 0.2f, 1.0f, 0.5f);
			/*float[] coords = {bc.x - bc.size, bc.y + bc.size,
    				bc.x - bc.size, bc.y - bc.size,
    				bc.x + bc.size, bc.y + bc.size,
    				bc.x + bc.size, bc.y - bc.size};
			
			if(bc.texture_id == -1) //lazy loading is not really good
				bc.texture_id = loadGLTexture(gl, bc.texture_resourse_id);
			drawRect(coords, bc.texture_id, gl);*/
			
			
			bc.sprite.Draw(bc.x, bc.y, 0.0f);
			
		}
		gl.glDisable(GL10.GL_BLEND);
		
		//gl.glColor4f(red, 0.0f, 0.0f, 0.5f);
		//gl.glVertexPointer(2, GL10.GL_FLOAT, 0, fBuffer);
		//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
	}
	
/*	private void drawRect(float [] coords, int texture_id, GL10 gl)
	{	
		//vertex
		ByteBuffer vbb = ByteBuffer.allocateDirect(4 * 2 * 4); //4 points * 2 coords per point * 4 bytes per float
	    vbb.order(ByteOrder.nativeOrder());
	    FloatBuffer vfb = vbb.asFloatBuffer();
	    vfb.clear();
	    vfb.put(coords);
	    vfb.position(0);
	    
	    // buffer holding the texture coordinates
		float texture_coords[] = 
			{
				0.0f, 0.0f,		
				1.0f, 0.0f,		
				0.0f, 1.0f,		
				1.0f, 1.0f		
			};
	    
		//texture
	    ByteBuffer tbb = ByteBuffer.allocateDirect(4 * 8);
	    tbb.order(ByteOrder.nativeOrder());	    
	    FloatBuffer tfb = tbb.asFloatBuffer();
	    tfb.clear();
	    tfb.put(texture_coords);	    
	    tfb.position(0);
	    
	    
	    //actual draw
	    gl.glEnable(GL10.GL_TEXTURE_2D);
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[texture_id]);
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

	    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, tfb);
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vfb);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);

	}

	 

	public int loadGLTexture(GL10 gl, int resourse_id)
	{	
	    // loading texture
	    Bitmap bitmap = BitmapFactory.decodeResource(gm.appContext.getResources(), resourse_id);	 
	    bitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
	    
	    // generate one texture pointer
	    gl.glGenTextures(1, textures, texture_count);
	    // ...and bind it to our array
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[texture_count]);
	    
	    // create nearest filtered texture
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);	 

	    // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
	    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	    gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	    
	    // Clean up
	    bitmap.recycle();
	    texture_count++;
	    return (texture_count - 1);
	}*/


}
