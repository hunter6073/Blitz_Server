package server_connections;

import java.util.Vector;

import user_interfaces.BlitzGameServerInterface;
import user_interfaces.RoomInterface;
import utility.RoomThread;


public class SocketManager {
	
	/******************************************************
	 * Creator: Ted Wong
	 * StudentID: 2014012687
	 * Major: Software Engineering
	 * Class: 143
	 * Description: This is the main manager for connecting different functions and 
	 * interfaces together. This manager holds two lists, one being the transfer socket 
	 * list, and the other being the room interface list.
	 *****************************************************/

	private int roomnum = 0; // the total number of rooms created on the server
	public Vector<TransferSocket> v = new Vector<TransferSocket>();//a vector list to hold the transfer sockets
	public Vector<RoomInterface> riv = new Vector<RoomInterface>(); // a vector list to hold the room interfaces
	private static final SocketManager sm = new SocketManager(); //initialize a socket manager
	public static SocketManager getsocketmanager() //get the instance of the socket manager
	{   
		return sm;	
	}

	public void add(TransferSocket ts)//add the transfer sockets into the vector
	{		
		v.add(ts);
	}

	public void create_room(String username)	//create a room for the host using the host's username
	{
		for(int i=0;i<riv.size();i++)
		{
			if(riv.get(i).pia_list.get(0).getUsername().equals(username)) // if the player had already created a room
			{
				// send the player to the already created room;
				getsocket(username).outputObject("roomnumber", riv.get(i).r.roomnumber);
				getsocket(username).outputObject("room", riv.get(i).r);
				return;
			}
		}
		
		// if the player hadn't create any rooms
		Room r = new Room(++roomnum);
		//create room interface and send room created message
		RoomInterface frame = new RoomInterface(roomnum,r);
        frame.updateplayer_info(new Player_Info_active(username,0,0,0,0));
		riv.add(frame);
		BlitzGameServerInterface.getServer().CreateRooms(frame,roomnum);
		String msg = "room "+roomnum+" has been created by "+username+"\n";
		frame.SendTeamMessage(msg);
		//send room number to the room creator
		if(getsocket(username)!=null)
		{
			getsocket(username).outputObject("roomnumber", roomnum);
		}
		
		// use time thread to determine if this room should be destroyed
		RoomThread rt = new RoomThread(riv.size()-1);
		rt.start();
	}

	public void update_room(Room r) //update room information and distribute updated info to all players in the room
	{
		int roomnum = -1;
		//update room on server
		for(int i=0;i<riv.size();i++)
		{
			if(riv.get(i).getR().roomnumber==r.roomnumber)
			{
				roomnum = i;
				riv.get(i).setR(r);
				riv.get(i).updateTeam();// show updated messages
			}
		}
		//distribute updated messages to all players in the room 
		if(roomnum!=-1)
		{
			for(int i=0;i<riv.get(roomnum).pia_list.size();i++)
			{
				TransferSocket tf = getsocket(riv.get(roomnum).pia_list.get(i).username);
				if(tf!=null)
				{
					tf.outputObject("room", r);
				}
				
			}
		}
		
	}

	public TransferSocket getsocket(String playerid) //get the specific socket for the id
	{
		TransferSocket t = null;
		
		for(int i=0;i<v.size();i++)
		{
			if(v.get(i).getUsername().equals(playerid))
			{
				t = v.get(i);
			}
		}
		return t;
	}

	public Room find_room(int roomnumber)	//find room
	{
		for(int i=0;i<riv.size();i++)
		{
			if(riv.get(i).getR().roomnumber==roomnumber)
			{
				return riv.get(i).getR();
			}
		}
		return null;
	}
	
    public RoomInterface find_RoomInterface(int roomnumber)//find room interface
    {
    	for(int i=0;i<riv.size();i++)
		{
			if(riv.get(i).getR().roomnumber==roomnumber)
			{
				return riv.get(i);
			}
		}
		return null;
    }
    
    // getters and setters, auto generated
	public int getRoomnum() // get room number
	{
		return roomnum;
	}
	
	public void setRoomnum(int roomnum) // set room number
	{
		this.roomnum = roomnum;
	}


}
