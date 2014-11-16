/*this is the main IP Configuration activity......
 * 
 * performs
 * 1) Initialization of ip input screen
 * 2) interchange IP addresses with partner device automatically 
 * */


package com.kp.talkie.layout;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import com.kp.talkie.protocol.WifiProtocol;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap.Config;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigIPActivity extends Activity implements OnClickListener
{
	private static final String TAG = "WiFiscan";
	static EditText ip;
	Button btn;
	WifiManager wifiman;
    static boolean wifi;
    static ServerSocket sskt = null;
    static Socket skt=null;
    static Socket client_Socket = null;
    static DataInputStream din = null;
    static DataOutputStream dout = null;
    static Toast toast;
    
/*******On create method ,Called when the activity is first created. ***************/
	
    @Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchlist);
		ip = (EditText)findViewById(R.id.editText1);
		btn = (Button)findViewById(R.id.configbtn);
		wifiman = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifi = wifiman.isWifiEnabled();
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		toast = Toast.makeText(getApplicationContext(), "",Toast.LENGTH_SHORT);
		getAddress();
		
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				
				String callAddress = ip.getText().toString();
				WifiProtocol.CALLER_IP = callAddress;
				finish();
				Intent i = new Intent(ConfigIPActivity.this,SelectionActivity.class);//mode select activity 
				startActivity(i);
			}
		});
	}// ON CREATE FINISHED

	public static void getAddress()
	{
		if(wifi)
	    {  
			/* wifi is enabled  ie. access point is not on so.... this is client .......
			 * considering client it will send IP address to server
			 * and will wait to get IP address from server in response 		  	
			*/
			try
			{
				client_Socket = new Socket(WifiProtocol.SERVER_IP,9600);
				din = new DataInputStream(client_Socket.getInputStream());
				dout = new DataOutputStream(client_Socket.getOutputStream());
				dout.writeUTF(WifiProtocol.ClIENT_IP);
				dout.flush();
				System.out.println("Success");
				ip.setText(WifiProtocol.SERVER_IP);
				
				System.out.println("Serverr : "+WifiProtocol.SERVER_IP);
				toast.setText(WifiProtocol.SERVER_IP);
				toast.show();
				
			}
			catch (Exception e)
			{
				System.out.println("Errrrrr : "+e.getMessage());
			}
			finally
			{
				try {
						client_Socket.close();
						din.close();
						dout.close();
					}
					catch (Exception e2) 
					{
						// TODO: handle exception
					}
			}
	    }
		
		else
		{
			/* wifi is not enable ie. this is server .......
			 * considering server it will listen to client for getting IP address			*  	
			*/
			try
			{
				sskt = new ServerSocket(9600);
				toast.setText("Obtaining Network Address");
				toast.show();

				skt = sskt.accept();
				din = new DataInputStream(skt.getInputStream());
				dout = new DataOutputStream(skt.getOutputStream());
							
				String getMessage = din.readUTF();
				WifiProtocol.ClIENT_IP = getMessage;
				
				ip.setText(WifiProtocol.ClIENT_IP);
				toast.setText(WifiProtocol.ClIENT_IP);
				toast.show();
				
			}
			catch (Exception e) 
			{
				System.out.println("Errrrrr : "+e.getMessage());
			}
			finally
			{
				try {
						sskt.close();
						skt.close();
						din.close();
						dout.close();
					}
					catch (Exception e2)
					{
						// TODO: handle exception
					}
			}			

		}
	}
/*********************** IP ADDRESS INTERCHANGE FUNCTIONING DONE **************/
	
	protected void onPause() 
	{
		super.onPause();
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
	}

	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}// CLASS END

/******************* CONFIGURATION ACTIVITY FINISHED ************************************/