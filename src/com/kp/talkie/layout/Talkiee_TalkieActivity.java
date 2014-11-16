/*this is the main talkie talkie activity......
 * 
 * performs
 * 1) Initialization of Welcome screen
 * 2) Initialization of menus
 * 3) fetching self ip address to interchange it in next activity
 * 4) menus and functioning of menu items.
 * */


package com.kp.talkie.layout;
import com.kp.talkie.protocol.WifiProtocol;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;



public class Talkiee_TalkieActivity extends Activity 
{
	/** Called when the activity is first created. */

	Toast toast;
	private static Context myContext = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		myContext = this;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// to keep screen on
		WifiManager wifiman = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		boolean wifi = wifiman.isWifiEnabled();
		
		if (wifi)
		{
			Toast.makeText(getApplicationContext(), "wifi is already on.....",
					Toast.LENGTH_LONG);
		} 
		else
		{
			// wifiman.setWifiEnabled(true);
			Toast.makeText(getApplicationContext(), "switching wifi on.....",
					Toast.LENGTH_LONG);
		}

		/*
		 * this is to get ip address........*/

				WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				// int ipAddress = wifiInfo.getIpAddress();
				String str = Formatter.formatIpAddress(wifiInfo.getIpAddress());
				WifiProtocol.ClIENT_IP = str;
		
				toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
				toast.setText("" + str);
				toast.show();
		
				
/*--- Designing and applying event to button just to proceed -----------*/

		final Button iv = (Button) findViewById(R.id.gobutton);
		iv.setFocusableInTouchMode(true);
		iv.setClickable(true);
		iv.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == android.view.MotionEvent.ACTION_DOWN)
				{
					Log.d("TouchTest", "Touch down");
					iv.setBackgroundResource(R.drawable.goimg);
				}
				else if (event.getAction() == android.view.MotionEvent.ACTION_UP)
				{
					iv.setBackgroundResource(R.drawable.goover);
					finish();
					Intent i = new Intent(Talkiee_TalkieActivity.this,
							ConfigIPActivity.class);
							startActivity(i);
				}
				return false;
			}
		});

	}
/******************* creating alert box for last exit ****************************************/
	
	 public void showConfigureMessage()
	    {
	            AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setMessage("Really want to exit ?")
	                   .setCancelable(false)
	                   .setIcon(R.drawable.talkie2)
	                   .setTitle(" Talkie Talkie ")
	                   .setPositiveButton(" Yes ", new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                                try {
	                                      Toast.makeText(getApplicationContext(), "Have a Great Time :)",Toast.LENGTH_SHORT).show();
	                                    //finish();
	                                      android.os.Process.killProcess(android.os.Process.myPid());
	                                    }
	                                catch (Exception e)
	                                    {
	                                    e.printStackTrace();
	                                    }
	                       }
	                   })
	                   .setNegativeButton(" No ", new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                            dialog.cancel();
	                       }
	                   });
	            AlertDialog alert = builder.create();
	            alert.show();
	    }
	
/***************************************************************************************/
/**********creating menu option and Initializing the menu screens**********************/	
/*************************************************************************************/
	
	
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) 
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater showMenu = getMenuInflater();
		showMenu.inflate(R.menu.coolmenu, menu);
		return true;
	}
/**************************** menu item selection ********************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) 
		{
		case R.id.appDetails:
							
							showInformation();
							System.out.println(" Showing Application Detailed Infromation...");
							break;
		case R.id.confIp:
			
							startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
							break;
						
		case R.id.appHelp:
							HelpInfo();
							break;
		}
		return false;
	}
/**************************** menu item selection ********************************/

	
	private void showInformation()
	{

		final Dialog abtApp = new Dialog(this);
		abtApp.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
		WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = li.inflate(R.layout.aboutapp, null);
		abtApp.setContentView(dialogView);
		abtApp.show();

		Button ok = (Button) dialogView.findViewById(R.id.bttnOkAboutApp);
		ok.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0) 
			{
				abtApp.dismiss();
			}
		});
	}
/********************* alert box for configuration setting help ***********************/
	
	public static void showAlertConnection(Context context)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false)
				.setIcon(R.drawable.about2)
				.setTitle("Configure Connection tips")
				.setMessage("Congifure WiFi connection with Access point first.\n"
							+ "get ip of other partner "
							+ "Please enter the correct IP into given Textbox and connect ..\n")
				.setPositiveButton(" Ok ",
									new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface dialog, int id)
										{}
						});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
/***********************************************************************************/
	
	public static void showAlertMsgChat(Context context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false)
				.setIcon(R.drawable.about2)
				.setTitle("Messege Chat tips")
				.setMessage("get connection done first .\n"
							+ "go to chat option selection page.\n"
							+ "select 'Message Chat' option and start...\n")
				.setPositiveButton(" Ok ",
									new DialogInterface.OnClickListener() 
									{
										public void onClick(DialogInterface dialog, int id)
										{}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
/****************************************************************************************/
	
	public static void showAlertVoiceChat(Context context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false)
				.setIcon(R.drawable.about2)
				.setTitle("Voice Chat Tips")
				.setMessage("Congifure connection first.\n"
							+ "go to selection page.\n"
							+ "select 'voice chat' button and start talking after pressing on start..\n")
				.setPositiveButton(" Ok ",
									new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface dialog, int id) {}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
/**********************************************************************************************/
	
	public void ShowConTips() 
	{
		showAlertConnection(this);
	}

	public void ShowMsgChatTips()
	{
		showAlertMsgChat(this);
	}

	public void ShowVoiceChatTips() 
	{
		showAlertVoiceChat(this);
	}

	private void HelpInfo() 
	{

		final Dialog abtApp = new Dialog(this);
		abtApp.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
		WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = li.inflate(R.layout.helpapp, null);
		abtApp.setContentView(dialogView);
		abtApp.show();

		Button connhint = (Button) dialogView.findViewById(R.id.connection);
		Button msghint = (Button) dialogView.findViewById(R.id.msghint);
		Button callhint = (Button) dialogView.findViewById(R.id.voicehint);

		connhint.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0)
			{
				ShowConTips();
			}
		});

		msghint.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0)
			{
				ShowMsgChatTips();
			}
		});

		callhint.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0)
			{
				ShowVoiceChatTips();
			}
		});

		Button ok = (Button) dialogView.findViewById(R.id.ok2);
		ok.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0)
			{
				abtApp.dismiss();
			}
		});
	}
/******************************************************************************/
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		//super.onBackPressed();
		showConfigureMessage();
		//android.os.Process.killProcess(android.os.Process.myPid());
	}
/*****************************************************************************/
}
/************************************ 	END  OF FIRST ACTIVITY ********************/