package com.buffer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.videostreaming.R;

public class VideoStreamMediaPlayer extends Activity implements
		SurfaceHolder.Callback{

	private static final String PAUSE_CSV_FILENAME = "mediapause01.csv";
	private static final String STARTDELAY_CSV_FILENAME = "mediastartdelay01.csv";
	// Declare variables
	ProgressDialog pDialog;
	MediaPlayer xyz = new MediaPlayer();

	// String VideoURL =
	// "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";
	// String VideoURL =
	// "http://twii.edgeboss.net/download/twii/manutd/mufc_trickbox_240215.mp4";
//	String mediaUrl = "http://115.119.242.26:8095/ukd/DemoFiles/Beautiful.mp4";
	// String VideoURL =
	// "http://115.119.242.26:8095/ukd/videos/Hoopla_480P.mp4";
	//String mediaUrl = "http://192.168.0.110/song.mp3";
	String mediaUrl = "http://notanswered.org/Beautiful.mp4";

	SurfaceView playerSurfaceView;
	int filesize;
	SurfaceHolder surfaceHolder;
	public ProgressBar mProgress;
	MediaPlayer mediaPlayer;
	static long tStart = 0, tEnd = 0, tDelta = 0, vPause = 0, vResume = 0,
			vDelta = 0, vPause_sum = 0, vStartTime = 0, vFirstMediaFrame = 0;// st=0;
	List<Long> vPausedDuration = new ArrayList<Long>();
	List<Integer> buffPercent = new ArrayList<Integer>();
	List<Long> vPausedPoints = new ArrayList<Long>();
	List<Long> startDelay = new ArrayList<Long>();

	int i;
	static double elapsedSeconds = 0;
	boolean start = true, end = true, print = true;
	int percent_frag = 0, prev_percent = 0;
	// float vPause_sum=0,vStartTime=0;
	// CSV writing

	// st=System.currentTimeMillis();

	FileWriter writer;
	// FileWriter writer1;
	// String root = (Environment.getExternalStorageDirectory()+
	// "Test_Results");
	// File gpxfile = (File)(new File(root+ "\\mydata5.csv"));
	File root = Environment.getExternalStorageDirectory();

	File pauseLogFile = new File(root, PAUSE_CSV_FILENAME);
	File startDelayLogFile = new File(root, STARTDELAY_CSV_FILENAME);

	// File gpxfile1=new File(root, "mydata9_loop.csv");

	/*
	 * private void writeCsvHeader(String h1, String h2, String h3) throws
	 * IOException { String line = String.format("%s,%s,%s\n", h1,h2,h3);
	 * writer.write(line); }
	 * 
	 * private void writeCsvData(float d, float e, float f) throws IOException {
	 * String line = String.format("%f,%f,%f\n", d, e, f); writer.write(line); }
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_stream_media_player);
		playerSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
		surfaceHolder = playerSurfaceView.getHolder();
		surfaceHolder.addCallback(this);

	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// to remove surface ...put this entire try-cache into onCreate and
		// comment all surfave related variables
		// for(int j=0; j<2; j++){
		// //////////////////////////
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDisplay(surfaceHolder);
			// mediaPlayer.reset();

			mediaPlayer.setDataSource(mediaUrl);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			
			vStartTime = System.currentTimeMillis();
			Log.d("yo", "Play button pressed at:" + vStartTime);
			
			mediaPlayer.prepare();
			Log.d("yo", "mediaplayer prepared:" + System.currentTimeMillis());
			
			mediaPlayer.start();
			Log.d("yo", "media player started:" + System.currentTimeMillis());

			// mediaPlayer.setOnPreparedListener(this);
			Log.d("yo", "Duration of video: " + mediaPlayer.getDuration()
					/ 1000.0);
			
			mediaPlayer
				.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer mp) {
							startDelay.add(System.currentTimeMillis()
									- vStartTime);
							Log.d("yo", "onPreparedCalled!" + System.currentTimeMillis());
						}
					});

			mediaPlayer
					.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
						@Override
						public void onBufferingUpdate(MediaPlayer mp,
								int percent) {

							
							Log.d("yo",
									"percent "
											+ String.valueOf(percent)
											+ "  "
											+ String.valueOf(System
													.currentTimeMillis()));

							percent_frag = percent - prev_percent;
							buffPercent.add(percent_frag);
							prev_percent = percent;
							if (percent < 100 && start == true) {
								start = false;
								tStart = System.currentTimeMillis();
							}
							if (percent == 100 && end == true) {
								end = false;
								tEnd = System.currentTimeMillis();
								tDelta = tEnd - tStart;
								elapsedSeconds = (tDelta / 1000);

								Log.d("yo",
										"Time taken: "
												+ String.valueOf(elapsedSeconds)
												+ "  " + tEnd + " " + tStart);
								Toast.makeText(getApplicationContext(),
										"Buffering time: " + elapsedSeconds,
										Toast.LENGTH_LONG).show();
								if (print == true) {
									//
									// Write start delay log file
									//
									try {
										Log.d("yo",
												"path " + String.valueOf(root));
										startDelayLogFile.createNewFile();
										writer = new FileWriter(
												startDelayLogFile, true);
									} catch (IOException e) {
										e.printStackTrace();
									}

									try {
										for (Long delay : startDelay) {
											Log.d("yo", "Delay:" + delay);
											writer.write(delay
													+ System.getProperty("line.separator"));
										}
										writer.close();
									} catch (IOException ioe) {
										ioe.printStackTrace();
									}

									//
									// Pause and related calculations
									//

									Log.d("yo", "No of times video paused"
											+ "  " + vPausedDuration.size());
									String lineneg = String.format("%s\t%s\n",
											"StartTime", "FirstMediaFrame");
									String line0 = String.format("%d\t%d\n",
											vStartTime, vFirstMediaFrame);
									String line1 = String.format("%s\t,%s\n",
											"TS", "Pause");

									// CSV create if not created already
									try {
										Log.d("yo",
												"path " + String.valueOf(root));
										pauseLogFile.createNewFile();
										writer = new FileWriter(pauseLogFile,
												true);
									} catch (IOException e) {
										e.printStackTrace();
									}
									try {
										writer.write(lineneg);
										writer.write(line0);
										writer.write(line1);
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

									for (int i = 0; i < vPausedDuration.size(); i++) {
										System.out.println(vPausedDuration
												.get(i));
										vPause_sum = vPause_sum
												+ vPausedDuration.get(i);
										// here, I will call CSV
										Log.d("yo",
												"Video Stopped at sec "
														+ ((vPausedPoints
																.get(i) - vStartTime) / 1000)
														+ "  For period of "
														+ vPausedDuration
																.get(i)
														/ 1000.0 + " seconds");
										String line2 = String
												.format("%d,%f\n",
														(int) ((vPausedPoints
																.get(i) - vStartTime) / 1000),
														(float) (vPausedDuration
																.get(i) / 1000.0));
										try {
											writer.write(line2);
											if (i == (vPausedDuration.size() - 1))
												writer.write("\n");
											writer.flush();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										//

									}
									try {
										writer.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Log.d("yo",
											"Total seconds video was paused"
													+ "  " + vPause_sum / 1000);
									// CSV writing

									//
									print = false;
									mediaPlayer.stop();
									mediaPlayer.release();
									SystemClock.sleep(5000);
									onBackPressed();
									// mediaPlayer.release();
									// mediaPlayer.reset();
								}
							}
						}
					});

			mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
				@Override
				public boolean onInfo(MediaPlayer mp, int what, int extra) {
					if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
						Log.d("yo", "buffering start, video paused" + "  "
								+ System.currentTimeMillis());
						vPause = System.currentTimeMillis();

					}
					if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
						Log.d("yo", "First media frame pushed at" + "  "
								+ System.currentTimeMillis());
						vFirstMediaFrame = System.currentTimeMillis();

					}
					if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
						Log.d("yo", "buffering end, video resumed" + "  "
								+ System.currentTimeMillis());
						vResume = System.currentTimeMillis();
						vDelta = vResume - vPause;
						vPausedPoints.add((long) vPause);
						vPausedDuration.add((long) vDelta);

					}
					return false;
				}
			});
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// }////////////////////////////for loop ends here

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
	}
}
