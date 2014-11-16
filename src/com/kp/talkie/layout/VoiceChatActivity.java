/*this is MAIN TASK OF PROJECT the VOICE CHAT activity......
 * 
 * performs
 * 1) Initialization of CALL screen .
 * 2) VOICE sending and receiving functions 
 * 3) uses UDP to perform the data sending
 * 4) USES - 1) Audio recorder class 
 * 			 
 * */

package com.kp.talkie.layout;
import com.kp.talkie.protocol.WifiProtocol;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class VoiceChatActivity extends Activity 
{

	private EditText target;
	private TextView streamingLabel;
	private Button startButton,stopButton;
	private Chronometer Clock;
	Toast toast;
	public byte[] buffer;
	public static DatagramSocket socket;
	private int port=8000;         //which port??
	AudioRecord recorder;

	//Audio Configuration. 
	private int sampleRate = 8000;      //How much will be ideal?
	private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;    
	private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;       
	private AudioTrack speaker;
	private boolean status = true;
//------------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.voicecall);
	    
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// TO KEEP SCREEN ON FOREVER

	    target = (EditText)findViewById(R.id.IP);
	    target.setEnabled(false);
	    target.setText(WifiProtocol.CALLER_IP.toString());
	 
	    streamingLabel = (TextView) findViewById(R.id.label);
	    startButton = (Button) findViewById (R.id.startBTN);
	    stopButton = (Button) findViewById (R.id.StopBTN);
	 
	    // clock show event generation..................................
	    Clock = (Chronometer)findViewById(R.id.clock);
	    Clock.setEnabled(false);
	 
	    toast = Toast.makeText(getApplicationContext(), "",Toast.LENGTH_LONG);
	    startButton.setOnClickListener (startListener);
	    stopButton.setOnClickListener (stopListener);
	    startServer();
	}
//--------------------------------------------------------------------------------------------------
	
	private final OnClickListener stopListener = new OnClickListener() 
	{

	    @Override
	    public void onClick(View arg0)
	    {
	    			Log.d("VS","streamingLabel calling");
	    			streamingLabel.setText("Call End.......");
	    			Log.d("VS","streamingLabel called");
	    			
	                stopButton.setBackgroundResource(R.drawable.end2);
	                stopButton.setEnabled(false);
	                startButton.setEnabled(false);
	                startButton.setBackgroundResource(R.drawable.start);
	                startButton.setEnabled(true);
	                
	                Clock.stop();           
	                status = false;
	                recorder.stop();
	                socket.close();
	                
	                Log.d("VS","calling doWaiting()");
	                Log.d("VS","called doWaiting()");
	                
	               	Intent intent = new Intent(VoiceChatActivity.this,VoiceChatActivity.class);
	        		startActivity(intent);
	                          
	                Log.d("VS","Recorder released");
	    }

	};
//------------ delay generation -----------------------------------------------

	public static void doWaiting()
	{
		for(int i=0;i<3;i++)
        {
     	   try {
				Thread.sleep(1000);
			} 
     	   catch (InterruptedException e)
     	   {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
//----------------------------------------------------------------------------------------------
	private final OnClickListener startListener = new OnClickListener()
	{
		@Override
	    public void onClick(View arg0)
	    {
	    	stopButton.setBackgroundResource(R.drawable.end1);
	    	stopButton.setEnabled(true);
            startButton.setBackgroundResource(R.drawable.start1);
            startButton.setEnabled(false);
            Clock.setVisibility(View.VISIBLE);
            Clock.start();
            streamingLabel.setText("Calling.......");
	    	status = true;
	    	startStreaming();  
	    }

	};
//*********************************************************************************************
//************************ RECEIVING AUDIO THROUGH SOCKET AND WRITE IT TO THE AUDIOTRACK ****//
//*************************WHICH PLAYS THE RECEIVED AUDIO BY HARDWARE *********************//
	
	public void startServer()
	{
		

		    Thread receiveThread = new Thread (new Runnable() 
		    {
		        @Override
		        public void run() 
		        {
		            try {
		                socket = new DatagramSocket(8000);
		                Log.d("VR", "Socket Created");

		                byte[] buffer = new byte[160000];
		                Log.d("VR","byte buffer ok ");
		                int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
		                Log.d("VR","min buffer size ok ");
		                minBufSize *= 1;
		                speaker = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
		                										sampleRate,
		                										channelConfig,
		                										audioFormat,
		                										minBufSize,
		                										AudioTrack.MODE_STREAM);
		                Log.d("VR","speaker  ok ");
		                speaker.setPlaybackRate(sampleRate);
		                Log.d("VR","speaker playing ok ");
		                boolean playerStarted=false;
		                
		                while(status == true) 
		                {
		                    try {
		                    		DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
		                        
		                    		if(!playerStarted)
		                    		{
		                    			speaker.play();
		                    			playerStarted=true;
		                    		}
		                        
		                        System.out.println("server started and waiting....");
		                        socket.receive(packet);
		                        System.out.println("Server : "+packet.getAddress()+"\n"+packet.getPort());
		                        System.out.println("server got some data....");
		                        Log.d("VR", "Packet Received");
		                        
		                        buffer = packet.getData();
		                        Log.d("VR", "Packet data read into buffer");

		                        speaker.write(buffer, 0, minBufSize);
		                        Log.d("VR", "Writing buffer content to speaker");

		                    } catch(IOException e) {
		                        Log.e("VR","IOException");
		                    }
		                }

		            }
		            catch (SocketException e)
		            {
		                Log.e("VR", "SocketException");
		            }
		        }
		    });
		    receiveThread.start();
		}
//************************************************************************************************************************
//****************  AUDIO RECORDING FROM MIC AND STREAMING TO THE DATABGAM SOCKET **************************************//
	
	public void startStreaming()
	{
	    Thread streamThread = new Thread(new Runnable()
	    {
	        @Override
	        public void run()
	        {
	            try 
	            {;
	              int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
	              DatagramSocket socket = new DatagramSocket();               
	              byte[] buffer = new byte[minBufSize];
	              Log.d("VS","Buffer created of size " + minBufSize);
	              DatagramPacket packet;
	              
	              final InetAddress destination = InetAddress.getByName(target.getText().toString());
	              Log.d("VS", "Address retrieved");
	              
	              recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize);
	              Log.d("VS", "Recorder initialized");
	              recorder.startRecording();
	                
	                while(status == true) 
	                {
	                	minBufSize = recorder.read(buffer, 0, buffer.length);
	                    //putting buffer in the packet
	                    packet = new DatagramPacket (buffer,buffer.length,destination,port);
	                    socket.send(packet);
	                }
	            }
	            catch(UnknownHostException e) 
	            {
	                Log.e("VS", "UnknownHostException");
	            }
	            catch (IOException e) 
	            {
	                Log.e("VS", "IOException");
	            } 
	        }

	    });
	    streamThread.start();
	 }
//*******************************************************************************************
	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		toast.setText("Call Ended ");
		toast.show();
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
//********************************************************************************************
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
 }//end of class

/********************* ....END OF VOICE CHAT ACTIVITY ....***********************/