package com.is3av.sensors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
//import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener, AccelerometerListener {
	private SensorManager mSensorManager;
	private Sensor mGyroSensor;

	private ArrayList<String> xCo = new ArrayList<String>();
	private ArrayList<String> yCo = new ArrayList<String>();
	private ArrayList<String> zCo = new ArrayList<String>();
	private ArrayList<String> degreeX = new ArrayList<String>();
	private ArrayList<String> degreeY = new ArrayList<String>();
	private ArrayList<String> degreeZ = new ArrayList<String>();
	private ArrayList<String> timeStamp = new ArrayList<String>();
	private ArrayList<String> timeStampAccel = new ArrayList<String>();
	private String root;
	private File mydir;
	private Button buttonStop;
	private Button buttonStart;
	private Long tslong;
	private String ts;
	private int counter = 0;
	private String accelTs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		root = android.os.Environment.getExternalStorageDirectory().toString();
		System.out.println(root);
		mydir = new File(root+"/values");
		mydir.mkdirs();
		buttonStop = (Button) findViewById(R.id.stopButton);
		buttonStop.setText("Stop");
		buttonStop.setOnClickListener(stoplistener);
		buttonStart = (Button) findViewById(R.id.startButton);
		buttonStart.setText("Start");
		buttonStart.setOnClickListener(startListener);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


	}
	
	private OnClickListener startListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			xCo.clear();
			yCo.clear();
			zCo.clear();
			degreeX.clear();
			degreeY.clear();
			degreeZ.clear();
			timeStamp.clear();
			timeStampAccel.clear();
			if(counter>10)
				counter = 1;
			else
				counter += 1;
			
			
		}
	};
	private OnClickListener stoplistener =  new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				saveGyro(degreeX, degreeY, degreeZ,timeStamp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				saveValuesAccel(xCo, yCo, zCo);
			} catch(IOException e) {
				e.printStackTrace();
			}

		}
	};
	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mGyroSensor,SensorManager.SENSOR_DELAY_NORMAL);

		// If accelerometer is available, start listening
		if (AccelerometerManager.isSupported(this)) {	
			AccelerometerManager.startListening(this);
		}

	}

	@Override
	public void onStop() {
		super.onStop();

		// If accelerometer is listening, stop it.
		if (AccelerometerManager.isListening()) {

			AccelerometerManager.stopListening();

		}

	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Sensor", "Service  distroy");

		// If accelerometer is listening, stop it.
		if (AccelerometerManager.isListening()) {

			AccelerometerManager.stopListening();

		}

	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		degreeX.add(String.valueOf(event.values[0]));
		degreeY.add(String.valueOf(event.values[1]));
		degreeZ.add(String.valueOf(event.values[2]));
		tslong = System.currentTimeMillis();
		ts = tslong.toString();
		timeStamp.add(ts);

		

	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onAccelerationChanged(float x, float y, float z) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onShake(float force, float x, float y, float z,Long ts) {
		xCo.add(String.valueOf(x));	
		yCo.add(String.valueOf(y));
		zCo.add(String.valueOf(z));
		accelTs = ts.toString();
		timeStampAccel.add(accelTs);
	}

	private void saveValuesAccel(ArrayList<String> xCo,ArrayList<String> yCo,ArrayList<String> zCo) throws IOException {
		String fileName = "accel_values" + counter+ ".xls";
		File file = new File(mydir+File.separator+fileName);
		FileWriter writer = new FileWriter(file);
		writer.append("\n"+"X-Accel"+"\t"+"Y-Accel"+"\t"+"Z-Accel"+"\t"+"Time Stamp"+"\n");		
		for(int j=0;j<xCo.size();j++) {
			writer.append(xCo.get(j)+"\t"+yCo.get(j)+"\t"+zCo.get(j)+"\t"+timeStampAccel.get(j)+"\n");
		}
		writer.flush();
		writer.close();

	}

	private void saveGyro(ArrayList<String> degreeX, ArrayList<String> degreeY, ArrayList<String> degreeZ,ArrayList<String> timeStamp) throws IOException {
		String fileName = "Gyroscope_values" + counter+ ".xls";
		File file = new File(mydir+File.separator+fileName);
		FileWriter writer = new FileWriter(file);
		writer.append("\n"+"X-Degree"+"\t"+"Y-Degree"+"\t"+"Z-Degree"+"\t"+"Time Stamp"+"\n");		
		for(int i=0;i<degreeX.size();i++) {
			writer.append(degreeX.get(i)+"\t"+degreeY.get(i)+"\t"+degreeZ.get(i)+"\t"+timeStamp.get(i)+"\n");
		}
		writer.flush();
		writer.close();
		
	}

}
