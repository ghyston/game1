package com.hyston.games.game1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Grid
{
	private int valuesCounterMax = 42; // count of lines
			
	FloatBuffer xBuffer;
	float[] coordsGrid;
			
	Grid()
	{
		coordsGrid = new float[valuesCounterMax *4];
		int counter = 0;
		for(float ix = -5; ix<=5; ix+=0.5)
		{
			coordsGrid[counter++] = ix;
			coordsGrid[counter++] = -5;
			coordsGrid[counter++] = ix;
			coordsGrid[counter++] = 5;
			
		}
		
		for(float iy = -5; iy<=5; iy+=0.5)
		{
			coordsGrid[counter++] = -5;
			coordsGrid[counter++] = iy;
			coordsGrid[counter++] = 5;
			coordsGrid[counter++] = iy;
		}		
		
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(valuesCounterMax * 4 * 4); //I HATE JAVA FOR THAT! number_lines * 4 coords per line * 4 bytes
		vbb.order(ByteOrder.nativeOrder());
		xBuffer = vbb.asFloatBuffer();
			    
		xBuffer.put(coordsGrid);
		xBuffer.position(0);			
	}
			
	public void Draw(GL10 gl)
	{
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				
		gl.glColor4f(0.95f, 0.95f, 0.95f, 1.0f);	
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, this.xBuffer);
		gl.glDrawArrays(GL10.GL_LINES, 0, valuesCounterMax * 2);
	}

}
