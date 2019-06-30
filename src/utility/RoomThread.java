package utility;

import server_connections.SocketManager;
import user_interfaces.BlitzGameServerInterface;

public class RoomThread extends Thread {

	/******************************************************
	 * Creator: Ted Wong
	 * StudentID: 2014012687
	 * Major: Software Engineering
	 * Class: 143
	 * Description: This  thread is used to determine if a room has been abandoned
	 * and dispose of it if so.
	 *****************************************************/

	int rim; // room number in the SocketManager list
	int k = 0; // room status
	public RoomThread(int m) { // constructor
		super();
		rim = m;
	}
	@Override
	public void run() {
		super.run();
		while(k<300) // if the room is occupied
		{
			if(SocketManager.getsocketmanager().riv.get(rim).getR().teamnumber()!=0) // if there are teams in the room
			{
				int j = 0;
				for(int i=0;i<SocketManager.getsocketmanager().riv.get(rim).getR().teamnumber();i++)
				{
					if(SocketManager.getsocketmanager().riv.get(rim).getR().getTeam(i).getPlayers().size()!=0) // if the teams are not empty
					{
						k = 0;
						j=0;
						break;
					}
					else // if the teams are empty
					{
						j++;
					}
				}
				if(j!=0) // if the teams are empty
				{
					k++;
				}

			}
			else // if there are no teams in the room
			{
				k++;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// if the room has been abandoned for 300 seconds, 
		//remove the room and free up the space
		BlitzGameServerInterface.getServer().RemoveRoom(rim); 
	}


}
