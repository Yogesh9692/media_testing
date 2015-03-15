package com.buffer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import android.os.Environment;
import java.io.FileOutputStream;

import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.videostreaming.R;
public class Homepage extends Activity{
/*	FileWriter writer;
	
	//File root = Environment.getExternalStorageDirectory();
	//File gpxfile = new File(root, "mydata.csv");
	//File gpxfile = new File("D:\\mydata.csv");
	//File root = Environment.getExternalStoragePublicDirectory(getPackageCodePath());
	//File gpxfile = new File(root, "mydata.csv");
	//Environment.getExternalStorageDirectory().getPath() instead
	
	
	File root = Environment.getExternalStorageDirectory();
	
    
	//String x = (root + "/mydata.csv");
	File gpxfile = new File(root, "mydata.csv");
	
    private void writeCsvHeader(String h1, String h2, String h3) throws IOException {
		   String line = String.format("%s,%s,%s\n", h1,h2,h3);
		   writer.write(line);
	}
	private void writeCsvData(float d, float e, float f) throws IOException {  
		  String line = String.format("%f,%f,%f\n", d, e, f);
		  writer.write(line);
		}
	
	public void writeCSVhere(){
		for (int i=0;i<2;i++){
		try {
			
			Log.d("yo","path "+  String.valueOf(root));
	        
			gpxfile.createNewFile();
			writer = new FileWriter(gpxfile,true);  //true for append purpose
	        writeCsvHeader("FirstParam","SecondParam","ThirdParam");
	        writeCsvData(0.31f,5.2f,7.0f);
	        writeCsvData(0.31f,5.2f,7.1f);
	        writeCsvData(0.31f,5.2f,7.2f);
	        writer.write("\n");
	        writer.flush();
	        writer.close(); 
		} catch (IOException e) {
	            e.printStackTrace();
		}
		}	
	}
*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		
		// TODO Auto-generated method stub
		
		//writeCSVhere();
		
		
		
		
		/*
		String filename = "myfile";
		String[] numbers = new String[] {"1, 2, 3"};
		FileOutputStream outputStream;

		try {
			  outputStream = openFileOutput(filename, Context.MODE_APPEND);
			  for (String s : numbers) {  
			      outputStream.write(s.getBytes());  
			  } 
			  outputStream.close();
			} catch (Exception e) {
			  e.printStackTrace();
			}
		*/
		
		
		
		
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		Button startTest = (Button)findViewById(R.id.StartButton);
		startTest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Class ourClass;
			for (int i=0; i<1; i++){
	
				
			try {
					//ourClass = Class.forName("com.Andro.startingPoint");
				
					ourClass = Class.forName("com.buffer.VideoStreamMediaPlayer");
					Intent ourIntent = new Intent(Homepage.this, ourClass);
					startActivity(ourIntent);
				}
				catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
		
		});
	}
	
}
