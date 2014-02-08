package com.hyston.games.game1;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import 	android.util.Log;
import android.widget.TextView;
import android.view.*;

public class Game1 extends Activity implements SensorEventListener {
	
	GameSurfaceView gameSurfaceView;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    GameManager gManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);        
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gameSurfaceView = new GameSurfaceView(this);
        setContentView(gameSurfaceView);
        
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        gameSurfaceView.onPause();
        mSensorManager.unregisterListener(this);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        gameSurfaceView.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    	
    }

    public void onSensorChanged(SensorEvent event)
    {
    	gManager = GameManager.getInstance();
    	gManager.sensorOnRotate(event.values[0],event.values[1]);
    	
    	//gameSurfaceView.mRenderer.setNewValue(event.values[0], event.values[1], event.values[2]);
    }
}

class GameSurfaceView extends GLSurfaceView {
	
	public GameES2Renderer mRenderer;
	GameManager gManager;
	
	private static final int INVALID_POINTER_ID = -1;
	
	private int movePointerId = INVALID_POINTER_ID;
	private int firePointerId = INVALID_POINTER_ID;
	private int gotoPointerId = INVALID_POINTER_ID;

    public GameSurfaceView(Context context)
    {
        super(context);
        gManager = GameManager.getInstance(); //May be move it higher?
		gManager.init(context);
        mRenderer = new GameES2Renderer();
        setRenderer(mRenderer);
        
    }
    
    public boolean onTouchEvent(final MotionEvent event)
    {
    	queueEvent(new Runnable()
		{
			public void run()
			{
				gManager.gui.onTouchEvent(MotionEvent.obtain(event));
			}
		});
	    return true;
	}
}