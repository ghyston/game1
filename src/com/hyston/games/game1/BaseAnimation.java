package com.hyston.games.game1;

public class BaseAnimation
{
  float duration;
  float frames_count;
  int texture_id; //todo: make texture class
  long start_time; // when animation was started. 0 if wasn't
  
  void start()
  {
	  start_time = System.currentTimeMillis();
  }
  
  int getFrameNumber(long cur_time)
  {
	  if(cur_time - start_time > duration)
		  stop();
	return 0;	  
  }
  
  void stop()
  {
	  start_time = 0;
  }
}
