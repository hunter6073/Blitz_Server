package server_connections;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import user_interfaces.BlitzGameServerInterface;
import utility.GameUtil;


public class TransferSocket extends Thread{//2.0 code standard reached. last edit date:2018/5/15

	/******************************************************
	 * Creator: Ted Wong
	 * StudentID: 2014012687
	 * Major: Software Engineering
	 * Class: 143
	 * Description: This is the transfer socket, the most important part of communication 
	 * between the player socket and the server. This class is being modulized and new 
	 * codes can be directly inserted into the empty slot
	 *****************************************************/
    //test use
	int mp = 0; // TODO: delete this after testing 
	
	Socket socket; // client socket
	String username; 
	BlitzGameServerInterface BG = BlitzGameServerInterface.getServer(); // main server interface
	ObjectInputStream is = null;
	ObjectOutputStream os = null;

	//send arguments into transfer socket
	public TransferSocket(Socket s)
	{

		this.socket = s;
		try {
			is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			os = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void run()
	{
		while(true)
		{
			try {

				// get messages from phone and decide what to do with it
				Object obj = is.readObject();  
				if(obj!=null)
				{
					Transmit tran = (Transmit) obj;
					String Type = tran.type;
					if(Type.equals("update_DP")) // if player updates position and direction
					{
						Player_Info_active pi = (Player_Info_active)tran.obj;  
						SocketManager.getsocketmanager().find_RoomInterface(pi.getRoomnumber()).processDP(pi);
					}
					else if(Type.equals("shoot_success")) // if player successfully shoots an enemy
					{
						Player_Info_active pi = (Player_Info_active)tran.obj;
						String enemy= pi.getUsername();
						if(SocketManager.getsocketmanager().getsocket(enemy)!=null)
						{
							SocketManager.getsocketmanager().getsocket(enemy).outputObject("got_hit", username);
						}
					
						
					}
					else if(Type.equals("create_room")) // if player creates a room
					{
						Player_Info_active pi = (Player_Info_active)tran.obj;
						setUsername(pi.getUsername());
						SocketManager.getsocketmanager().create_room(pi.getUsername());
					}
					else if(Type.equals("update_room")) // if player updates room information
					{
						Room r = (Room)tran.obj;
						SocketManager.getsocketmanager().update_room(r);
						for(int i=0;i<r.teamnumber();i++)
						{
							for(int j=0;j<r.getTeam(i).getPlayers().size();j++)
							{
							
								SocketManager.getsocketmanager().find_RoomInterface(r.roomnumber).updateplayer_info(new Player_Info_active(r.getTeam(i).players.get(j),r.roomnumber,4.5f,4.5f,30f));
							}
						}
					}
					else if(Type.equals("search_room")) // if the player requests a search on a specific room
					{
						Player_Info_active pi = (Player_Info_active)tran.obj;
						setUsername(pi.getUsername());
						int roomnum = pi.getRoomnumber();
						Room r = SocketManager.getsocketmanager().find_room(roomnum);
						if(r!=null)
						{
							outputObject("room", r);
							SocketManager.getsocketmanager().find_RoomInterface(roomnum).updateplayer_info(pi);
						}
						else
						{
							outputObject("room", new Room());
						}

					}
					else if(Type.equals("check_connection")) // if the player requests check on current connection
					{
						outputObject("connection_fine",null);
					}
					else if(Type.equals("wifi_log")) // this is used to get data during experiments and write it into an excel form
					{
						String tmp = (String) tran.obj;
						BlitzGameServerInterface.getServer().ShowMessage(tmp);
						GameUtil.writeDataContent(tmp);
					}
					else if(Type.equals("user_killed")) // if the player updates their status to killed
					{

						Player_Info_active pi = (Player_Info_active)tran.obj;  
						SocketManager.getsocketmanager().find_RoomInterface(pi.getRoomnumber()).remove_dead(pi);

					}

					//add here for other type of messages

				}

			} 
			catch (Exception e) {

				e.printStackTrace();
				SocketManager.getsocketmanager().v.remove(this);
				/* TODO  uncomment this for formal release
					    for(int i=0;i<SocketManager.getsocketmanager().riv.size();i++)
					    {
					    	SocketManager.getsocketmanager().riv.get(i).removeplayerfromroom(username);
					    }
				 */
				BlitzGameServerInterface.getServer().ShowMessage("user "+username+" disconnected");
				break;
			}  


		}
		System.out.println("user" + username + "disconnected");

	}

	public void outputObject(String info,Object obj) // send Object or message to phone
	{

		try {
			Transmit t = new Transmit(info, obj);
			os.writeObject(t);
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getUsername() // get username
	{
		return username;
	}

	public void setUsername(String username) // set username
	{
		this.username = username;
	}

}
