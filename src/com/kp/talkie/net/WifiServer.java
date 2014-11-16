/*this is the class to sending data to server......
 * 
 * performs
 * 1) manage TCP connection and start server functioning
 * 2) get message and set it to message textbox
 * */
//---------------------------------------------------------------------------------------
package com.kp.talkie.net;
import com.kp.talkie.net.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import com.kp.talkie.layout.MessageChatActivity;
import com.kp.talkie.protocol.WifiProtocol;
//------------------------------------------------------------------------------------------
public class WifiServer extends Thread 
{
	private ServerSocket socket;
	private Socket client;

	public void startServer() 
	{
		try {
					
				System.out.println(WifiProtocol.LOG_TEXT +"created server socket...");
				socket = new ServerSocket(WifiProtocol.SERVER_PORT);
				
			while(true)
			{
				try
				{
					System.out.println(WifiProtocol.LOG_TEXT+"waiting for "+WifiProtocol.counter +" client request");
					client = socket.accept();
					System.out.println(	WifiProtocol.LOG_TEXT
										+ "got incoming connection..."
										+ WifiProtocol.counter++);
					
					DataInputStream in = new DataInputStream(client.getInputStream());
					DataOutputStream out = new DataOutputStream(client.getOutputStream());
					String data = in.readUTF();
					System.out.println(WifiProtocol.LOG_TEXT + "from client:"+data);
					MessageChatActivity.setMessage(data); // sending message to activity and setting it to textbox
					in.close();
					out.close();
					client.close();
				}
				catch (Exception e)
				{
					System.out.println(WifiProtocol.LOG_TEXT+"exception in client : " + 1 +" :"+e.getMessage());
				}
			}

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			System.out.println(WifiProtocol.LOG_TEXT + "server Exception:"
								+ e.getMessage());
			e.printStackTrace();
		} 
		finally 
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

/************** Thread creation ********************/
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		startServer();
	}

	private boolean checkPort() 
	{
		try {
			Socket s = new Socket("192.168.0.102", WifiProtocol.SERVER_PORT);
			System.out.println(WifiProtocol.LOG_TEXT+"created test socket with server...");
			if(s!=null)
			{
				s.close();
				return false;
			}			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
/**************************************************************************************/
/*************************** ... END OF SERVER ACTIVATION CLASS ....******************/
/***************************************************************************************/