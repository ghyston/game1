package com.hyston.games.game1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;
import java.util.ArrayList;


public class Scene
{
	private int valuesCounterMax = 4; // just look-point
	
	FloatBuffer rectBuffer;
	ShortBuffer rectindexBuffer;
	
	GameManager gameManager;
	
	String sceneTxt = 
			"rect 1.5 0.0 0.0 1.0 1.0 2.0;" +
			"rect 0.5 0.0 1.0 1.0 0.2 1.0;"; //rect: center(x,y), vec(x,y), width, height
	
	String scene1 = 
			"rect 1.0 0.0 0.0 0.2 1.0 1.6;" + 		//01
			"rect 1.7 0.9 0.2 0.0 0.2 1.2;" + 		//02
			"rect 1.7 1.5 0.2 0.0 0.2 1.2;" + 		//03
			"rect 1.2 1.8 0.0 0.2 0.2 0.4;" + 		//04
			"rect 0.4 1.9 -0.2 0.0 0.2 1.4;" + 		//05
			"rect -0.2 1.65 0.0 -0.2 0.2 0.3;" + 	//06
			"rect -0.5 1.9 -0.2 0.0 0.2 0.4;" + 	//07
			"rect -0.8 1.25 0.0 -0.2 0.2 1.5;" + 	//08
			"rect -0.7 0.4 0.2 0.0 0.2 0.4;" + 		//09
			"rect -0.2 0.9 0.0 0.2 0.2 0.4;" + 		//10
			"rect -0.8 0.1 0.0 -0.2 0.2 0.4;" + 	//11
			"rect -0.7 -0.2 0.2 0.0 0.2 0.4;" + 	//12
			"rect 0.1 -0.7 0.2 0.0 0.2 0.8;" + 		//13
			"rect -0.38 0.57 0.4 0.4 0.2 0.6;" + 	//14
			"rect -0.32 -0.45 -0.49 0.55 0.2 0.75;" + //15
			"wapo 1 2.2 1.2 1 3;" + //waypoints: id, x,y, count_nearest, (id nearest)..
			"wapo 2 0.9 1.6 4 13 6 5 3;" + 
			"wapo 3 0.9 1.2 5 2 13 6 5 4;" + 
			"wapo 4 0.9 0.9 4 3 5 6 13;" +
			"wapo 5 0.3 0.9 6 2 3 4 6 7 8;" +
			"wapo 6 0.1 1.3 7 11 13 2 3 4 5 7;" +
			"wapo 7 0.1 0.6 4 5 6 8 9;" +
			"wapo 8 0.3 -0.4 3 5 7 9;" +
			"wapo 9 -0.5 0.1 2 7 8;" +
			"wapo 10 -0.5 0.9 1 11 ;" +
			"wapo 11 -0.5 1.3 3 6 10 12;" +
			"wapo 12 -0.5 1.6 1 11;" +
			"wapo 13 0.1 1.6 4 2 3 4 6;" +
			"zomb 0.0 1.0;"; //zombie: x,y;
			 
	
	
	Geometry.Rectangle[] rects;
	int rectsCount = 0;
	
	FloatBuffer lineBuffer;
	
	Scene()
	{
		this.gameManager = GameManager.getInstance();
	}
	
	public void init()
	{
		loadSceneFromFile();
		calculateBuffers();		
	}
	
	public void Draw(GL10 gl)
	{
		if(this.rectsCount > 0)
		{
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
			gl.glColor4f(0.5f, 0.52f, 0.5f, 0.5f);
			gl.glVertexPointer(2, GL10.GL_FLOAT, 0, this.rectBuffer);
			gl.glDrawElements(GL10.GL_TRIANGLES, rectsCount * 6, GL10.GL_UNSIGNED_SHORT, this.rectindexBuffer);	
		
			//gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
			//gl.glVertexPointer(2, GL10.GL_FLOAT, 0, this.rectBuffer);
			//gl.glDrawElements(GL10.GL_LINES, rectsCount * 6, GL10.GL_UNSIGNED_SHORT, this.rectindexBuffer);
		}

	}	
	
	private void loadSceneFromFile()
	{
		this.rectsCount = 0;
		
		//first looking for count of objects
		String[] objects = scene1.split(";");
		for(int i=0; i< objects.length; i++)
		{			
			String[] parameters = objects[i].split(" ");			
			if(parameters[0].equals("rect"))			
				this.rectsCount++;
		}
		
		//alloc memory
		rects = new Geometry.Rectangle[rectsCount];
		int rects_count = 0;
		
		//actual load
		for(int i=0; i< objects.length; i++)
		{			
			String[] parameters = objects[i].split(" ");			
			if(parameters[0].equals("rect"))			
			{
				float x = (new Float(parameters[1])).floatValue();
				float y = (new Float(parameters[2])).floatValue();
				float vec_x_x = (new Float(parameters[4])).floatValue();
				float vec_x_y = -(new Float(parameters[3])).floatValue();
				float vec_y_x = (new Float(parameters[3])).floatValue();
				float vec_y_y = (new Float(parameters[4])).floatValue();
				float width = (new Float(parameters[5])).floatValue();
				float height = (new Float(parameters[6])).floatValue();
				
				rects[rects_count++] = new Geometry.Rectangle(x,y,
						new Geometry.Vector(vec_x_x, vec_x_y), 
						new Geometry.Vector(vec_y_x, vec_y_y),
						width, height);				
			}
			else if(parameters[0].equals("zomb"))
			{
				float x = (new Float(parameters[1])).floatValue();
				float y = (new Float(parameters[2])).floatValue();
				this.gameManager.addZombie(x,y);
			}
			else if(parameters[0].equals("wapo"))
			{
				int id = (new Integer(parameters[1])).intValue();
				float x = (new Float(parameters[2])).floatValue();
				float y = (new Float(parameters[3])).floatValue();				
				Waypoint wp = new Waypoint(x,y,id);
				int nearest_count = (new Integer(parameters[4])).intValue();
				for(int i1 = 0; i1<nearest_count; i1++)
				{
					Integer temp_id = new Integer(parameters[5 + i1]);
					wp.waypoints.add(temp_id);
				}		
				this.gameManager.waypoints.add(wp);
			}
		}
	}
	
	
	private void calculateBuffers()
	{
		//Rectangle buffer
	    float[] rectCoords = new float[2 * 4 * rectsCount]; //2 coords, 4 points
	    short[] rectIndexs = new short[6 * rectsCount];
	    for(int i=0; i<rectsCount; i++)
	    {
	    	
	    	float vec_x_length = rects[i].vec_x.length();
	    	float vec_y_length = rects[i].vec_y.length();
	    	
	    	Geometry.Vector half_x = new Geometry.Vector(rects[i].vec_x.x * rects[i].width * 0.5f / vec_x_length, rects[i].vec_x.y * rects[i].width  * 0.5f / vec_x_length);
	    	Geometry.Vector half_y = new Geometry.Vector(rects[i].vec_y.x * rects[i].height * 0.5f / vec_y_length, rects[i].vec_y.y * rects[i].height  * 0.5f / vec_x_length);
	    	
	    	rectCoords[i*8] 	= rects[i].x + half_x.x + half_y.x; 	rectCoords[i*8 + 1] = rects[i].y + half_x.y + half_y.y;
	    	rectCoords[i*8 + 2] = rects[i].x - half_x.x + half_y.x; 	rectCoords[i*8 + 3] = rects[i].y - half_x.y + half_y.y;
	    	rectCoords[i*8 + 4] = rects[i].x - half_x.x - half_y.x; 	rectCoords[i*8 + 5] = rects[i].y - half_x.y - half_y.y;
	    	rectCoords[i*8 + 6] = rects[i].x + half_x.x - half_y.x; 	rectCoords[i*8 + 7] = rects[i].y + half_x.y - half_y.y;
	    	
	    	rectIndexs[i*6] = (short)(i*4 + 3);
	    	rectIndexs[i*6 + 1] = (short)(i*4 + 2);
	    	rectIndexs[i*6 + 2] = (short)(i*4 + 1);
	    	rectIndexs[i*6 + 3] = (short)(i*4 + 1);
	    	rectIndexs[i*6 + 4] = (short)(i*4 + 3);
	    	rectIndexs[i*6 + 5] = (short)(i*4);
	    }
	    
	    ByteBuffer rectbb = ByteBuffer.allocateDirect(rectsCount * 2 * 4 * 4);
	    rectbb.order(ByteOrder.nativeOrder());
	    rectBuffer = rectbb.asFloatBuffer();		    
	    rectBuffer.put(rectCoords);
	    rectBuffer.position(0);
	    
	    ByteBuffer rectIndexbb = ByteBuffer.allocateDirect(rectsCount * 6 * 2);
	    rectIndexbb.order(ByteOrder.nativeOrder());
	    rectindexBuffer = rectIndexbb.asShortBuffer();	    
	    rectindexBuffer.put(rectIndexs);
	    rectindexBuffer.position(0);		
	}
	
	public class Waypoint
	{
		float x;
		float y;
		int id;
		ArrayList<Integer> waypoints;
		
		Waypoint(float x, float y, int id)
		{
			this.x = x;
			this.y = y;
			this.id = id;
			waypoints = new ArrayList<Integer>(); 
		}
		
	}

}
