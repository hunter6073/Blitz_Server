package server_connections;

import java.net.ServerSocket;
import java.net.Socket;
import user_interfaces.BlitzGameServerInterface;

public class SocketThread extends Thread 
{
	/******************************************************
	 * Creator: Ted Wong
	 * StudentID: 2014012687
	 * Major: Software Engineering
	 * Class: 143
	 * Description: This is the main thread for connecting players, this 
	 * thread is used to connect every socket it receives and send 
	 * them to the socketmanager instance for further processing
	 *****************************************************/

	@Override
	public void run() {
		super.run();
		ServerSocket ss; // a server socket for client connection
		BlitzGameServerInterface UI = BlitzGameServerInterface.getServer(); // the main server interface

		try {
			ss = new ServerSocket(30000);
			while(true) // connect incoming sockets and register them in the socket manager
			{
				Socket s = ss.accept();
				TransferSocket ts = new TransferSocket(s);
				ts.start();
				SocketManager.getsocketmanager().add(ts);
				UI.ShowMessage("user connected");//show connected
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
