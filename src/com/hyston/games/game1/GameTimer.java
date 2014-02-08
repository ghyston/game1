package com.hyston.games.game1;

import java.util.ArrayList;
import java.util.Iterator;

public class GameTimer 
{
	private long lastTime = 0;
	ArrayList<GameEvent> eventsList = new ArrayList<GameEvent>();
	
	public interface RunEvent
	{
		public void run();
	}
	
	static abstract public class GameEvent implements RunEvent
	{
		/*float startValue;
		float endValue;
		float startTime;
		float timeToProceed;*/
		long lastTime;
		long duration;
		GameManager gm;
		
		//abstract void run(); 
		
		GameEvent(long duration, GameManager gm)
		{
			this.duration = duration;
			this.gm = gm;
			this.lastTime = System.currentTimeMillis();
		}
	}
	
	/*static public class IntervatlEvent implements RunEvent
	{
		
	}*/
	
	public void start()
	{
		lastTime = System.currentTimeMillis();
	}
	
	public void pause()
	{
		//TODO: implement it!
	}
	
	public void idle()
	{
		long currentTime = System.currentTimeMillis();
		
		Iterator eventIterator = eventsList.iterator();
		while(eventIterator.hasNext())
		{
			GameEvent ge = (GameEvent)eventIterator.next();
			if(currentTime - ge.lastTime > ge.duration)
			{
				ge.run();
				ge.lastTime = currentTime;
			}
		}
		this.lastTime = currentTime;
	}
	
	public void addEvent(GameEvent ge)
	{
		eventsList.add(ge);
	}

}
