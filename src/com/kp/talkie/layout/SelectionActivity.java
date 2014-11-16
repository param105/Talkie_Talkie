/* THIS IS SIMPLE SELECTION activity......
 * 
 * performs
 * 1) Initialization of  selection buttons.......
 * 2) selection of either 
 * 							1) message chat
 * 							2) voice chat
 * 3) exit dialog invocation function for back press event.... 
 *  
 * */



package com.kp.talkie.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SelectionActivity extends Activity  implements OnClickListener
{
	Button msgButton ;
	Button voiceButton; 
 
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modeselect);
        msgButton = (Button)findViewById(R.id.msgbtn);
        voiceButton = (Button)findViewById(R.id.voicecallbtn);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        msgButton.setOnClickListener(new OnClickListener() 
        {			
			@Override
			public void onClick(View v1)
			{
				Intent i = new Intent(SelectionActivity.this,MessageChatActivity.class);
				startActivity(i);		
			}
		});
        
        voiceButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v1)
			{
				Intent i = new Intent(SelectionActivity.this,VoiceChatActivity.class);
				startActivity(i);
				//Toast.makeText(getApplicationContext(), "voice streaming started.....", Toast.LENGTH_LONG);
			}
		});  
    }
	
	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub	
	}
/************************************************************************************************************/
/*********** method for invoking confirmation dialog for exit when back press event pressed................*/
/**********************************************************************************************************/
	
	public void showConfigureMessage()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		/*------------------------------------------------------------------------*/
		builder.setMessage("Really want to exit ?")
			    .setCancelable(false)
				.setIcon(R.drawable.talkie2)
				.setTitle(" TalikeTalkie ")
				.setPositiveButton(" Yes ",
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						try {
						 	  Toast.makeText(getApplicationContext(),
								 			 "Have a Great Time :)",
											  Toast.LENGTH_SHORT).show();
						 	 android.os.Process.killProcess(android.os.Process.myPid());
								//	finish();
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
					}
				})
				.setNegativeButton(" No ",
									new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface dialog, int id)
										{
											dialog.cancel();
										}
						});
		/*------------------------------------------------------------------------------------------*/
		
		AlertDialog alert = builder.create();
		alert.show();
	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		showConfigureMessage();
		//android.os.Process.killProcess(android.os.Process.myPid());
	}
}
/*************************************** SELECTION ACTIVITY FINISHED ***************************************/