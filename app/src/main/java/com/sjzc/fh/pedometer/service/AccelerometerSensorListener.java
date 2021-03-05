package com.sjzc.fh.pedometer.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.sjzc.fh.pedometer.ui.fragment.ZujiFragment;

/**
 * 加速度传感器的监听器
 *
 */
public class AccelerometerSensorListener implements SensorEventListener {

	public static int CURRENT_SETP = 0; // 步数
	public static float SENSITIVITY = 8; // 灵敏度

	private float mLastValues[] = new float[3 * 2];
	private float mScale[] = new float[2];
	private float mYOffset;
	private static long end = 0;
	private static long start = 0;

	private float mLastDirections[] = new float[3 * 2];
	private float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
	private float mLastDiff[] = new float[3 * 2];
	private int mLastMatch = -1;

	public static void reset() {
		CURRENT_SETP = 0;
	}

	public AccelerometerSensorListener(Context context) {
		super();
		int h = 480;
		mYOffset = h * 0.5f;
		mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));//x轴加速度
		mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));//y轴加速度
	}
	/**
	 * 计步传感器数据变化回调接口
	 * 这种类型的传感器返回步骤的数量由用户自上次重新启动时激活。返回的值是作为浮动(小数部分设置为0),
	 *只在系统重启复位为0。事件的时间戳将该事件的第一步的时候。这个传感器是在硬件中实现,预计低功率。
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor sensor = event.sensor;
		// 加锁
		synchronized (this) {

			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				//算出加速度传感器的x、y、z三轴的平均数值（平衡在某一个方向数值过大造成的数据误差）
				float vSum = 0;
				for (int i = 0; i < 3; i++) {
					final float v = mYOffset + event.values[i] * mScale[1];
					vSum += v;
				}
				int k = 0;
				float v = vSum / 3;

				float direction = (v > mLastValues[k] ? 1
						: (v < mLastValues[k] ? -1 : 0));
				if (direction == -mLastDirections[k]) {
					// Direction changed
					int extType = (direction > 0 ? 0 : 1);
					mLastExtremes[extType][k] = mLastValues[k];
					float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

					if (diff > SENSITIVITY) {
						boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
						boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
						boolean isNotContra = (mLastMatch != 1 - extType);

						if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough
								&& isNotContra) {
							end = System.currentTimeMillis();
							if (end - start > 500) {
								// 视为走了一步
								 CURRENT_SETP++;
								if (ZujiFragment.isInPocketMode) {// 判断口袋模式
									if (LightSensorService.isInPocket) {
										CURRENT_SETP++;
									}
								} else {
									CURRENT_SETP++;
								}

								mLastMatch = extType;
								start = end;
							}
						} else {
							mLastMatch = -1;
						}
					}
					mLastDiff[k] = diff;
				}
				mLastDirections[k] = direction;
				mLastValues[k] = v;
			}

		}

	}
	/**
	 * 计步传感器精度变化回调接口
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
