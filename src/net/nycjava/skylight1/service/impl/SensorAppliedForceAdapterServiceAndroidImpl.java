package net.nycjava.skylight1.service.impl;

import net.nycjava.skylight1.dependencyinjection.Dependency;
import net.nycjava.skylight1.service.BalancedObjectPublicationService;
import net.nycjava.skylight1.service.SensorAppliedForceAdapter;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;

public class SensorAppliedForceAdapterServiceAndroidImpl implements SensorAppliedForceAdapter {

	private static final int Z_AXIS = 2;
	
	private static final int Y_AXIS = 1;

	private static final int X_AXIS = 0;

	private static final float FORCE_FACTOR = 1.0f;

	private static final int CALIBRATE_MAX_COUNT = 15;
	
	private static final boolean START_FLAT = false;

//	private static final String TAG = "SensorAppliedForceAdapterServiceAndroidImpl";
	private static final String TAG = "SkylightSensor";

	@Dependency
	BalancedObjectPublicationService balancedPublicationService;

	@Dependency
	private SensorManager mSensorManager;

	private long lastTime;

	private double sumX;

	private double sumY;
	
	private double sumZ;

	private int countXY;

	private boolean calibrateDone;

	private int calibrateCount;

	private float lowX;

	private float highX;

	private float lowY;

	private float highY;
	
	private float lowZ;
	private float highZ;
	
	
	private double origAngleSum;
	private float originalAngle;	

	private final SensorListener mListener = new SensorListener() {
		public void onSensorChanged(int sensor, float[] values) {
			final long thisTime = System.currentTimeMillis();
			float x = values[X_AXIS];
			float y = values[Y_AXIS];
			float z = values[Z_AXIS];
			if (calibrateDone == false) {
				if ((Math.abs(x) > 2.5 || Math.abs(y) > 2.5) && START_FLAT) {
					// User is holding the phone vertically (or at least > 15 deg)
					// so put some 'good' defaults into range and let glass drop
					lowX = -0.005f;
					highX = 0.005f;
					lowY = -0.005f;
					highY = 0.005f;
					calibrateDone = true;
//					Log.d(TAG,"lowX " + lowX + " lowY " + lowY + " highX " + highX + " highY " +highY);
				} else {
					// user is holding holding the phone facing the sky (more or less)
					if (calibrateCount < CALIBRATE_MAX_COUNT) {
						// TODO: replace accl calibration with time weighted average
						// do calibration of x,y
						setXRange(x);
						setYRange(y);
						setZRange(z);
						sumX += x;
						sumY += y;
						sumZ += z;
						countXY++;
						calibrateCount++;
					} else {
						Log.d(TAG,"lowX " + lowX + " lowY " + lowY + " highX " + highX + " highY " + highY + " lowZ " + lowZ + " highZ " + highZ);
						calibrateDone = true;
						double avgZ = sumZ / countXY;
						double avgY = sumY / countXY;
						originalAngle = (float) Math.toDegrees(Math.atan(avgZ/avgY));
						Log.d(TAG,"calibrateAngle:" + originalAngle);
					}
				}
			} else {
				// calibration done so use it.
				if (true) {
					if (x < lowX) {
						x = x - lowX;
					} else if (x > highX) {
						x = x - highX;
					} else {
						x = 0.0f;
					}
					if (y < lowY) {
						y = y - lowY;
					} else if (y > highY) {
						y = y - highY;
					} else {
						y = 0.0f;
					}
					x = x * FORCE_FACTOR;
					y = y * FORCE_FACTOR;
				}
				balancedPublicationService.applyForce(x, -y, (thisTime - lastTime));
			}
			lastTime = thisTime;
		}

		public void onAccuracyChanged(int sensor, int accuracy) {
		}
	};

	public void start() {
		int mask = 0;
		mask |= SensorManager.SENSOR_ACCELEROMETER;
		lastTime = System.currentTimeMillis();
		sumX = 0;
		sumY = 0;
		countXY = 0;
		lowX = 999;
		highX = -999;
		lowY = 999;
		highY = -999;
		calibrateCount = 0;
		calibrateDone = false;
		mSensorManager.registerListener(mListener, mask, SensorManager.SENSOR_DELAY_GAME);
		Log.d(TAG, "start");
	}

	public void stop() {
		mSensorManager.unregisterListener(mListener);
		Log.d(TAG, "stop");
	}

	private void setXRange(float x) {
		if ((x < lowX) || (lowX == 999)) {
			lowX = x;
		}
		if ((x > highX) || (highX == -999)) {
			highX = x;
		}
	}

	private void setYRange(float y) {
		if ((y < lowY) || (lowY == 999)) {
			lowY = y;
		}
		if ((y > highY) || (highY == -999)) {
			highY = y;
		}
	}
	
	private void setZRange(float z) {
		if ((z < lowZ) || (lowZ == 999)) {
			lowZ = z;
		}
		if ((z > highZ) || (highZ == -999)) {
			highZ = z;
		}
	}
}