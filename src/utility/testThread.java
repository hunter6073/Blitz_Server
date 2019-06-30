package utility;

import java.util.LinkedList;

import server_connections.Player_Info_active;
import server_connections.SocketManager;
import user_interfaces.BlitzGameServerInterface;

public class testThread extends Thread {

	/******************************************************
	 * Creator: Ted Wong
	 * StudentID: 2014012687
	 * Major: Software Engineering
	 * Class: 143
	 * Description: This  thread is used to change the location of the dummy
	 *****************************************************/

	int rim; // room number in the SocketManager list
	public LinkedList<int[]> testlist = new LinkedList<int[]>(); // TODO delete this after testing
	int testroll = 0;
	public testThread(int m) { // constructor
		super();
		rim = m;
		testlist.add(new int[]{3,5,0});
		testlist.add(new int[]{5,1,0});
		testlist.add(new int[]{3,-4,0});
		testlist.add(new int[]{-2,3,0});
	}
	@Override
	public void run() {
		super.run();
		while(true) // if the room is occupied
		{
			
			Player_Info_active pi = new Player_Info_active("fakeEnemy",1,testlist.get(testroll)[0],testlist.get(testroll)[1],testlist.get(testroll)[2]);
			SocketManager.getsocketmanager().find_RoomInterface(rim).process_dummy(pi);
			testroll++;
			if(testroll>3)
			{
				testroll = 0;
			}
		
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


}
