package com.hyston.games.game1;

import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.util.Log;

import javax.microedition.khronos.opengles.GL10;

import com.hyston.games.game1.Geometry.Vector;

//Right now I have a vague idea, why do we need this 
public final class CollisionDetector
{
	static public GL10 gl;
	public static final int COLOR_RED = 1;
	public static final int COLOR_GREEN = 2;
	public static final int COLOR_BLUE = 3;
	public static final int COLOR_YELLOW = 4;
	public static final int COLOR_BLACK = 5;
	public static final int COLOR_WHITE = 6;
	
	
	private static Geometry.Vector circleAndAABBByVector(Geometry.Circle c, Geometry.Rectangle r, Geometry.Vector v)
	{
		Geometry.Vector result = new Geometry.Vector(0.0f, 0.0f);
		
		//step1. Calc projections rectangle and vector to oX & oY
	/*	Geometry.LineSegment rProjX = new Geometry.LineSegment(r.x - r.half_x.x, r.x + r.half_x.x);
		Geometry.LineSegment rProjY = new Geometry.LineSegment(r.y - r.half_y.y, r.y + r.half_y.y);
		//Geometry.LineSegment vProjX = new Geometry.LineSegment(c.x - c.rad, c.x + v.x + c.rad);
		//Geometry.LineSegment vProjY = new Geometry.LineSegment(c.y - c.rad, c.y + v.y + c.rad);
		Geometry.LineSegment vProjX = new Geometry.LineSegment(c.x, c.x + v.x);
		Geometry.LineSegment vProjY = new Geometry.LineSegment(c.y, c.y + v.y);
		
		//step2. Get projections intersection
		Geometry.LineSegment intersectX = lineIntersected(vProjX, rProjX);
		Geometry.LineSegment intersectY = lineIntersected(vProjY, rProjY);
		
		if(intersectX == null || intersectY == null)
			return null;
		
		//result = new Geometry.Vector(-intersectX.vecLength(), -intersectY.vecLength()); //it works, but player stucked
		result = Geometry.vectorProjection(new Geometry.Vector(-intersectX.vecLength(), -intersectY.vecLength()), v);*/
		//result.invert();
		
		
		/*if(intersectX.length() < intersectY.length())
		{
			float factor = v.y / v.x;
			result.x = -intersectX.vecLength();
			result.y = -factor * intersectY.vecLength();			
		}
		else
		{
			float factor = v.x / v.y;
			result.y = -intersectY.vecLength();
			result.x = -factor * intersectX.vecLength();			
		}*/
			
			
		
				
		/*if(intersectX.length() > intersectY.length())
		{
			result.x = -intersectX.vecLength();
			result.y = 0.0f;			
		}
		else
		{
			result.y = -intersectY.vecLength();
			result.x = 0.0f;
		}*/
		
		return result;
	}
	
	//to comments see: http://users.livejournal.com/_winnie/152327.html
	private static Geometry.Vector twoSegmentsIntersect(Geometry.Vector start1, Geometry.Vector end1, Geometry.Vector start2, Geometry.Vector end2)
	{
		//DrawLine(end1.x, end1.y, new Geometry.Vector(start1.x - end1.x, start1.y - end1.y), COLOR_BLUE);
		
		
		Geometry.Vector result = null;
		Geometry.Vector dir1 = new Geometry.Vector(end1.x - start1.x, end1.y - start1.y);
		Geometry.Vector dir2 = new Geometry.Vector(end2.x - start2.x, end2.y - start2.y);
		
		float a1 = -dir1.y;
        float b1 = +dir1.x;
        float d1 = -(a1*start1.x + b1*start1.y);

        float a2 = -dir2.y;
        float b2 = +dir2.x;
        float d2 = -(a2*start2.x + b2*start2.y);

        //����������� ����� ��������, ��� ��������� � ����� ������������� ���
        float seg1_line2_start = a2*start1.x + b2*start1.y + d2;
        float seg1_line2_end = a2*end1.x + b2*end1.y + d2;

        float seg2_line1_start = a1*start2.x + b1*start2.y + d1;
        float seg2_line1_end = a1*end2.x + b1*end2.y + d1;

        //���� ����� ������ ������� ����� ���� ����, ������ �� � ����� ������������� � ����������� ���.
        if (seg1_line2_start * seg1_line2_end >= 0 || seg2_line1_start * seg2_line1_end >= 0) 
            return null;
        
        float temp = (seg1_line2_start - seg1_line2_end); //while debugging may be here is the bug
        if(temp == 0.0f) temp = 0.00001f;

        float u = seg1_line2_start / temp;
        result = new Geometry.Vector(start1.x + u*dir1.x,start1.y + u*dir1.y); //TODO: check it!!
        
        //DrawLine(result.x, result.y, new Geometry.Vector(start1.x - result.x, start1.y - result.y), COLOR_BLUE);
		
		return result;
	}
	
	
	//4 sample, is enemy seeing player. This code doesn't work at all, move it to trash
	private static boolean lookAndRect(Geometry.Vector eye, Geometry.Vector obj, Geometry.Rectangle r)
	{		
		
		
		boolean result = false;	
		
		//calculate rect coordinates
		float vec_x_length = r.vec_x.length();
    	float vec_y_length = r.vec_y.length();
    	
    	Geometry.Vector half_x = new Geometry.Vector(r.vec_x.x * r.width * 0.5f / vec_x_length, r.vec_x.y * r.width  * 0.5f / vec_x_length);
    	Geometry.Vector half_y = new Geometry.Vector(r.vec_y.x * r.height * 0.5f / vec_y_length, r.vec_y.y * r.height  * 0.5f / vec_x_length);
    	
    	
    	Geometry.Vector[] rectCoordinates = new Geometry.Vector[4];
    	rectCoordinates[0] = new Geometry.Vector(r.x + half_x.x + half_y.x, r.y + half_x.y + half_y.y);
    	rectCoordinates[1] = new Geometry.Vector(r.x - half_x.x + half_y.x, r.y - half_x.y + half_y.y);
    	rectCoordinates[2] = new Geometry.Vector(r.x - half_x.x - half_y.x, r.y - half_x.y - half_y.y);
    	rectCoordinates[3] = new Geometry.Vector(r.x + half_x.x - half_y.x, r.y + half_x.y - half_y.y);
    	
    	
    	//TODO: optimize! Check only one side be cos (rect_center->rectCoord, rect_cent->eye) > 0
    	result = (twoSegmentsIntersect(eye, obj, rectCoordinates[0], rectCoordinates[1]) != null);
    	result = result || (twoSegmentsIntersect(eye, obj, rectCoordinates[1], rectCoordinates[2]) != null);
    	result = result || (twoSegmentsIntersect(eye, obj, rectCoordinates[2], rectCoordinates[3]) != null);
    	result = result || (twoSegmentsIntersect(eye, obj, rectCoordinates[3], rectCoordinates[0]) != null);
    	
    	/*int temp = 0;
    	for(int i=0; i<4; i++)
    	{
    		temp = (i+1 >)
    		result = result || (twoSegmentsIntersect(eye, obj, rectCoordinates[i], rectCoordinates[2]) != null);
    		
    	}*/
    	
    		
    	
		return result;		
	}
	
	//Vector-names: see notepad, ballistics note
	//TODO: optimize!
	public static boolean bulletAndCircle(Geometry.Vector from, Geometry.Vector path, Geometry.Circle c)
	{
		boolean result = false;
		
		Geometry.Vector AC = new Geometry.Vector(c.x - from.x, c.y - from.y);
		Geometry.Vector AB = new Geometry.Vector(path);
		Geometry.Vector BA = (new Geometry.Vector(path)).invert();
		Geometry.Vector BC = new Geometry.Vector(c.x - from.x - path.x, c.y - from.y - path.y);
		
		float scAC_AB = Geometry.vectorScalarMultiple(AC, AB);
		float scBC_BA = Geometry.vectorScalarMultiple(BC, BA);
		
		if(Geometry.vectorScalarMultiple(AC, AB) > 0 && 
				Geometry.vectorScalarMultiple(BC, BA) > 0)
		{
			Geometry.Vector AP = Geometry.vectorProjection(AC, AB);
			Geometry.Vector CP = new Geometry.Vector(AP.x - AC.x, AP.y - AC.y);
			if(CP.length() < c.rad)
				result = true;
			else
				result = false;			
		}
		else
			result = false;
		
		return result;
	}
	
	public static boolean bulletAndRect(Geometry.Vector bullet, Geometry.Rectangle r)
	{
		
		Geometry.Vector bulletTorCent = new Geometry.Vector(r.x - bullet.x, r.y - bullet.y);
		Geometry.Vector bulletTorCentProjX = Geometry.vectorProjection(bulletTorCent, r.vec_x);
		Geometry.Vector bulletTorCentProjY = Geometry.vectorProjection(bulletTorCent, r.vec_y);
		float bullet_proj_x = bulletTorCentProjX.length();
		float bullet_proj_y = bulletTorCentProjY.length();
		if(bullet_proj_x < (r.width/2.0f) && bullet_proj_y < (r.height/2.0f))		
			return true;		
		else		
			return false;
	}
	
	//TODO: optimize! And make aabb separate!
	private static Geometry.Vector circleAndRectByVector(Geometry.Circle c, Geometry.Rectangle r, Geometry.Vector v)
	{
		Geometry.Vector result = new Geometry.Vector(0.0f, 0.0f);
		
		Geometry.Vector circTorCent = new Geometry.Vector(r.x - c.x, r.y - c.y);
		
		Geometry.Vector circTorCentProjX = Geometry.vectorProjection(circTorCent, r.vec_x).invert(); //TODO: invert earlier!		
		Geometry.Vector circTorCentProjY = Geometry.vectorProjection(circTorCent, r.vec_y).invert();
		
		//calculate additions to circle radius. To see datails -> Red Book
		float x1 = v.x * c.rad / (float)Math.sqrt((double)(v.x * v.x + v.y * v.y));
		float y1 = x1 * v.y / v.x;
		
		Geometry.Vector radVecProjX = new Geometry.Vector(r.vec_x);
		Geometry.Vector radVecProjY = new Geometry.Vector(r.vec_y);
		radVecProjX.setLength(c.rad);
		radVecProjY.setLength(c.rad);
		
		Geometry.Vector moveVect = new Geometry.Vector(v.x , v.y );
		
		float startPointX = circTorCentProjX.length() * (getAligement(circTorCentProjX, r.vec_x) ? 1.0f : -1.0f); //not right. сонаправленность смотреть нужно
		float startPointY = circTorCentProjY.length() * (getAligement(circTorCentProjY, r.vec_y) ? 1.0f : -1.0f);
		Geometry.Vector moveProjX = Geometry.vectorProjection(moveVect, r.vec_x);
		Geometry.Vector moveProjY = Geometry.vectorProjection(moveVect, r.vec_y);
		
		float endPointX = moveProjX.length() * (getAligement(moveProjX, r.vec_x) ? 1.0f : -1.0f);
		float endPointY = moveProjY.length() * (getAligement(moveProjY, r.vec_y) ? 1.0f : -1.0f);
		
		Geometry.LineSegment rProjX = new Geometry.LineSegment(-r.width / 2.0f, r.width / 2.0f);
		Geometry.LineSegment rProjY = new Geometry.LineSegment(-r.height / 2.0f, r.height / 2.0f);		
		Geometry.LineSegment vProjX = new Geometry.LineSegment(startPointX, startPointX + endPointX + c.rad * (getAligement(moveProjX, r.vec_x) ? 1.0f : -1.0f));
		Geometry.LineSegment vProjY = new Geometry.LineSegment(startPointY, startPointY + endPointY + c.rad * (getAligement(moveProjY, r.vec_y) ? 1.0f : -1.0f));
		
		Geometry.LineSegment intersectX = lineIntersected(vProjX, rProjX);
		Geometry.LineSegment intersectY = lineIntersected(vProjY, rProjY);
		
		if(intersectX == null || intersectY == null)
			return null;
		
		if(intersectX.length() < intersectY.length())
		{			
			result = new Geometry.Vector(r.vec_x);
			result.setLength(-intersectX.vecLength());
		}
		else
		{			
			result = new Geometry.Vector(r.vec_y);
			result.setLength(-intersectY.vecLength());
		}			
		
		return result;
	}
	
	public static boolean isCircleNearCircle(Geometry.Circle c1, Geometry.Circle c2)
	{
		float dist = (new Geometry.Vector(c2.x - c1.x, c2.y - c1.y)).length();
		return (dist < (c1.rad + c2.rad));
	}
	
	
	private static boolean getAligement(Geometry.Vector v1, Geometry.Vector v2)
	{
		float cos = v1.x * v2.x + v1.y * v2.y;
		return (cos > 0);
	}
	
	
	//A should be player-projection
	private static Geometry.LineSegment lineIntersected(Geometry.LineSegment A, Geometry.LineSegment B)
	{
		float minA = Math.min(A.x, A.y);
		float minB = Math.min(B.x, B.y);
		float maxA = Math.max(A.x, A.y);
		float maxB = Math.max(B.x, B.y);
		if(minA > maxB || maxA < minB)
			return null; //Doesn't intersect
		
		if(maxA > minB && minA < minB) //A is left from B
			return new Geometry.LineSegment(maxA, minB); 
		else if(maxB > minA && maxA > maxB) //B is left from A
			return new Geometry.LineSegment(minA, maxB);
		else if(minA > minB && maxA < maxB) //A included in B
			return new Geometry.LineSegment(A.y, A.x);
		
		return null;
	}
	
	public static Geometry.Vector getPlayerSceneCollision(Geometry.Vector old_pos, Geometry.Vector new_pos, Scene scene)
	{
		Geometry.Vector result = new Geometry.Vector(0.0f, 0.0f);
		Geometry.Vector moveVector = new Geometry.Vector(new_pos.subVector(old_pos));

		boolean isChanged = false;
		do
		{
			isChanged = false;
			//calc rectangles first
			for(int i = 0; i < scene.rectsCount; i++)
			{
				Geometry.Vector v = circleAndRectByVector(
					new Geometry.Circle(old_pos.x, old_pos.y, 0.1f),
					scene.rects[i],
					moveVector);
				if(v != null)
				{
					result.addVector(v);
					isChanged = true;
				}
			}
			moveVector = new Geometry.Vector(result);
		}
		while(isChanged);
		return result;
	}
	
	public static boolean getVisibilityPlayer(Geometry.Vector enemy, Geometry.Vector player, Scene scene)
	{
		boolean result = false;
		
		for(int i = 0; i < scene.rectsCount && !result; i++)
		{
			result = result || lookAndRect(enemy, player, scene.rects[i]);
		}	
		//result = lookAndRect(enemy, player, scene.rects[0]);
		
		return !result;
	}
	
	public static boolean getBulletSceneCollision(Geometry.Vector bullet, Scene scene)
	{
		boolean result = false;
		for(int i = 0; i < scene.rectsCount && !result; i++)
		{
			result = result || bulletAndRect(bullet, scene.rects[i]);;
		}
		
		return result;		
	}
	
	static public void DrawLine(float x, float y, Vector to, int color)
	{
		FloatBuffer lineBuffer;
		ByteBuffer vbb = ByteBuffer.allocateDirect(2 * 2 * 4); //This shit still can look much better!
	    vbb.order(ByteOrder.nativeOrder());
	    lineBuffer = vbb.asFloatBuffer();	    
	    
		lineBuffer.clear();		
		
		float[] coords = {x, y, x + to.x, y + to.y};
				  
		lineBuffer.put(coords);
		lineBuffer.position(0);	
		
		//float minC = 0.5f;
	//	float maxC = 0.5f;
		
		switch(color)
		{
			case(COLOR_RED):
				gl.glColor4f(1.0f, 0.5f, 0.5f, 0.0f);
			break;
			
			case(COLOR_GREEN):
				gl.glColor4f(0.5f, 1.0f, 0.5f, 0.0f);
			break;
			
			case(COLOR_BLUE):
				gl.glColor4f(0.5f, 0.5f, 1.0f, 0.0f);
			break;
			
			case(COLOR_YELLOW):
				gl.glColor4f(1.0f, 1.0f, 0.5f, 0.0f);
			break;
			
			case(COLOR_BLACK):
				gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
			break;
			
			case(COLOR_WHITE):
				gl.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
			break;
			
		}
		
				
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, lineBuffer);
		gl.glDrawArrays(GL10.GL_LINES, 0, 3);
	}

}
