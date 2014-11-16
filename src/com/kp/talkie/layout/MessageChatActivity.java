/*this is the MESSAGE CHAT activity......
 * 
 * performs
 * 1) Initialization of message chat screen .
 * 2) msg sending and receiving functions 
 * 3) uses TCP to perform the messaging
 * 4) USES - 1)SendToServer class 
 * 			 2) WifiServer class
 * 			 3) WifiProtocol class
 * */



package com.kp.talkie.layout;
import com.kp.talkie.net.SendToServer;
import com.kp.talkie.net.WifiServer;
import com.kp.talkie.protocol.WifiProtocol;
import android.R.color;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageChatActivity extends Activity 
{
    private static String msg1;
	static Handler dataHandler = new Handler();
	private static TextView txtMsg;
	ScrollView sv;
	/********** Called when the activity is first created. **********/
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // to keep screen on forever
        
        txtMsg=(TextView)findViewById(R.id.ed1);
        txtMsg.setBackgroundColor(color.white);
        txtMsg.setTextColor(Color.WHITE);
        txtMsg.setTextSize(21);
             
        System.out.println(WifiProtocol.LOG_TEXT+"before starting server");
        WifiServer serverThread = new WifiServer();
        serverThread.start();
          
               try 
               {
                  final EditText et = (EditText) findViewById(R.id.editText1);
                  et.setOnKeyListener(new OnKeyListener()
                  {
                	    public boolean onKey(View v, int keyCode, KeyEvent event) 
                	    {
                	        // If the event is a key-down event on the "enter" button
                	        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                	            (keyCode == KeyEvent.KEYCODE_ENTER)) 
                	        {
                	          // Perform action on key press
                	        	String str = et.getText().toString();
                	        	System.out.println(WifiProtocol.LOG_TEXT+"sending. "+str + " to " + WifiProtocol.SERVER_IP);
                	        	new SendToServer().send(str);
                                txtMsg.append("\n Me : "+str);
                                et.setText("");
                                Log.d("Client", "Client sent message");
                                return true;
                	        }
                	        return false;
                	    }
                	});
               } 
               catch (Exception e) 
               {
            	   e.printStackTrace();
               }
          
               System.out.println(WifiProtocol.LOG_TEXT+"after closing server");
       }// end of oncreate method

    
/**************  this is  thread creation*************************/
    
    static Runnable txtRunnable = new Runnable()
	{
		public void run()
		{
			//txtMsg.setTextColor(Color.BLUE);
			txtMsg.append("\n You :"+msg1);
		}
	};
    
    public static void setMessage(String msg)
	{
		msg1 = msg;
		System.out.println("Obtained Message In From Server : "+msg1);
		dataHandler.post(txtRunnable);
	}
}

/**************** ....MESSAGE CHAT ACTIVITY END ...***************/