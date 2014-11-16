/*this is the class to sending data to server......
 * 
 * performs
 * 1) send message to DataOutputStream
 * */

/*********************************************************************************************/
package com.kp.talkie.net;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import com.kp.talkie.protocol.*;
//---------------------------------------------------------------------------------------------
public class SendToServer 
{
	public void send(String data)
	{
		try {
				Socket s = new Socket(WifiProtocol.CALLER_IP,WifiProtocol.SERVER_PORT);
				DataOutputStream out = new DataOutputStream(s.getOutputStream());
	            out.writeUTF(data);
	            out.flush();
	            System.out.println(WifiProtocol.LOG_TEXT+"sent "+data + " to server..");
	            out.close();
	            s.close();	
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			System.out.println(WifiProtocol.LOG_TEXT+"failed to send "+data + " to server..");
			e.printStackTrace();
		}
	
	}
}

/************************** ... END OF THE SENDTOSERVER CLASS...*************************************/