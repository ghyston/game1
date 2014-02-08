package com.hyston.games.game1;

public final class Geometry
{	
	static public class Rectangle
	{
		Rectangle(float x, float y, Vector vec_x, Vector vec_y, float width, float height)
		{
			this.x = x;
			this.y = y;
			this.vec_x = vec_x;
			this.vec_y = vec_y;				
			this.width = width;
			this.height = height;
		}
			
		float x;
		float y;
		Vector vec_x;
		Vector vec_y;
		float width;
		float height;
			
	}
	
	static public class Color
	{
		float r;
		float g;
		float b;
		
		Color(float r, float g, float b)
		{
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}
		
		
		static public class Circle
		{
			Circle(float x, float y, float radius)
			{
				this.x = x;
				this.y = y;
				this.rad = radius;
			}
			
			float x;
			float y;
			float rad;
		}
		
		static public class Vector
		{
			Vector(float x, float y)
			{
				this.x = x;
				this.y = y;
			}
			
			Vector(Vector v)
			{
				this.x = v.x;
				this.y = v.y;
			}
			
			float x;
			float y;
			
			public Vector addVector(Vector v)
			{
				this.x += v.x;
				this.y += v.y;
				return this;
			}
			
			public Vector subVector(Vector v)
			{
				this.x -= v.x;
				this.y -= v.y;
				return this;
			}
			
			public float length()
			{
				return (float)Math.sqrt(x*x + y*y);
			}
			
			public Vector invert()
			{
				x = -x;
				y = -y;
				return this;
			}
			
			public Vector setLength(float length)
			{
				float thisLength = this.length();
				this.x = this.x * length / thisLength;
				this.y = this.y * length / thisLength;
				return this;
			}
		}
		
		//������� (���.). Easy to work with projections 
		static public class LineSegment extends Vector
		{
			LineSegment(float x, float y)
			{
				super(x,y);
			}
			
			public float length()
			{
				return Math.abs(x - y);
			}
			
			public float vecLength()
			{
				return (x - y);
			}
			
		}
		
		//TODO: make it to normaled vectors!
		static public Vector vectorProjection(Vector from, Vector to)
		{
			Vector result = new Vector(0.0f, 0.0f);
			
			//get scalar multiple
			float scalar = from.x * to.x + from.y * to.y;
			float toSquareLength = to.x * to.x + to.y * to.y;
			result.x = to.x * scalar / toSquareLength;
			result.y = to.y * scalar / toSquareLength;
			return result;			
		}
		
		//Not normiered! Return cos
		static public float vectorScalarMultiple(Vector v1, Vector v2)
		{			
			return v1.x * v2.x + v1.y * v2.y;
		}
		
		static public boolean getsignZfromVectorMultiple(Vector v1, Vector v2)
		{
			return (v1.x*v2.y - v1.y*v2.x) > 0;
		}

}
