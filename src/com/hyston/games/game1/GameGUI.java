package com.hyston.games.game1;

import android.util.Log;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.lang.Math;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.Map;



public class GameGUI 
{
	public ArrayList<BaseController> controllers = new ArrayList<BaseController>();
	public HashMap<Integer, Integer> pointers = new HashMap<Integer, Integer>(); //id - controller_id
	public int screenWidth;
	public int screenHeight;
	
	public static final int MY_ACTION_DOWN = 0;
	public static final int MY_ACTION_MOVE = 1;
	public static final int MY_ACTION_RELEASE = 2;
	
	public void reset_controllers()
	{
		controllers.clear();
		//(0,0) is on top right phone's corner
		float size = Math.min(screenHeight, screenWidth) / 5;
		controllers.add(new FireController(screenWidth - size, screenHeight - size, size, size));
		controllers.add(new LookController(size, screenHeight - size, size, size));
		controllers.add(new WeaponController(screenWidth - screenWidth/5, screenHeight/10, screenWidth/6, screenHeight/10));
	}
	
	public void setWeightAndHeight(int screenWidth, int screenHeight)
	{
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;		
		reset_controllers();
	}
	
	
	private Integer chooseController(float x, float y)
	{
		ListIterator<BaseController> iter = controllers.listIterator();
		while(iter.hasNext())
		{			
			int nextIndex = iter.nextIndex();
			BaseController bc = iter.next();
			
			if((Math.abs(bc.x - x) < bc.width) && (Math.abs(bc.y - y) < bc.height))
			{	
				return nextIndex;
			}
		}
		return null;
	}
	
	private void catchAction(int my_action, Integer ctrl_id, float x, float y)
	{
		if(ctrl_id == null)
			return;
		
		int ictrl_id = ctrl_id.intValue();
		
		if(ictrl_id >= controllers.size() && (ictrl_id < 0))
			return; //try-catch?
		
		switch(my_action)
		{
			case MY_ACTION_MOVE: 
				controllers.get(ictrl_id).onMove(x, y);
				break;
				
			case MY_ACTION_DOWN:
				controllers.get(ictrl_id).onTouch(x, y);
				break;
				
			case MY_ACTION_RELEASE:
				controllers.get(ictrl_id).onRelease();
				break;
		}		
	}
		
	
	public boolean onTouchEvent(MotionEvent event)
    {
		final int typeAction = event.getAction();
		switch(typeAction & MotionEvent.ACTION_MASK)
		{
			//TODO: may be last controller is ALWAYS null at this point?
			case MotionEvent.ACTION_DOWN:
			{				
					final float x = event.getX();
		        	final float y = event.getY();
		        	int pointerId = 0;
		        	
		        	pointerId = event.getPointerId(0);
					
		        	Integer curr_controller = chooseController(x,y);
		        	Integer last_controller = pointers.get(pointerId);

		        	if(last_controller != curr_controller)
		        		catchAction(MY_ACTION_RELEASE, last_controller, 0, 0);
		        
		        	catchAction(MY_ACTION_DOWN, curr_controller, x, y);
		        	pointers.put(pointerId, curr_controller);
								
				
				break;
			}
			
			case MotionEvent.ACTION_MOVE:
			{
				try
				{
					for (Map.Entry<Integer, Integer> entry : pointers.entrySet()) 
					{
						int pointerId = entry.getKey();
						final int pointerIndex = event.findPointerIndex(pointerId);
						final float x = event.getX(pointerIndex);
						final float y = event.getY(pointerIndex);
						Log.d("ACTION_MOVE", "x:" + x + " y:" + y);
			        
						Integer curr_controller = chooseController(x,y);
						Integer last_controller = entry.getValue();
			        
						if(last_controller != curr_controller)
							catchAction(MY_ACTION_RELEASE, last_controller, 0, 0);
			        
						catchAction(MY_ACTION_MOVE, curr_controller, x, y);
						pointers.put(pointerId, curr_controller);
					}
				}
				catch(Exception e)
				{
					Log.d("ACTION_DOWN", e.toString());
				}	
				break;
			}
			
			case MotionEvent.ACTION_UP:
			{
				try
				{			
					final int pointerId = event.getPointerId(0);
					catchAction(MY_ACTION_RELEASE, pointers.get(pointerId), 0.0f, 0.0f);
					pointers.remove(pointerId);
				}
				catch(Exception e)
				{
					Log.d("ACTION_UP", e.toString());
				}	
				break;
			}
			
			case MotionEvent.ACTION_CANCEL:
			{
				try
				{			
					final int pointerId = event.getPointerId(0);
					catchAction(MY_ACTION_RELEASE, pointers.get(pointerId), 0.0f, 0.0f);
					pointers.remove(pointerId);
				}
				catch(Exception e)
				{
					Log.d("ACTION_CANCEL", e.toString());
				}	
				
				break;
			}
			
			case MotionEvent.ACTION_POINTER_UP:
			{
				try
				{
					final int pointerIndex = (typeAction & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		        	final int pointerId = event.getPointerId(pointerIndex);
		        
		        	catchAction(MY_ACTION_RELEASE, pointers.get(pointerId), 0.0f, 0.0f);
		        	pointers.remove(pointerId);
				}
				catch(Exception e)
				{
					Log.d("ACTION_POINTER_UP", e.toString());
				}	
				break;
			}
			
			case MotionEvent.ACTION_POINTER_DOWN:
			{
				
				try
				{
					final int pointerIndex = (typeAction & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		            final int pointerId = event.getPointerId(pointerIndex);
		        
		            final float x = event.getX(pointerIndex);
		            final float y = event.getY(pointerIndex);
		        
		            Integer curr_controller = chooseController(x,y);
		        
		            pointers.put(pointerId, curr_controller);
		            catchAction(MY_ACTION_DOWN, curr_controller, x, y);
				}
				catch(Exception e)
				{
					Log.d("ACTION_POINTER_DOWN", e.toString());
				}	
				break;
			}
		
		}       
        
    	return true;
    }
}
    			
    		
    		
  
