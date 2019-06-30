package server_connections;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import unused.FireCalibration;

public class Room implements Serializable {

	/******************************************************
	 * Creator: Ted Wong
	 * StudentID: 2014012687
	 * Major: Software Engineering
	 * Class: 143
	 * Description: This class is used to store basic room information, 
	 * including room number and a list of teams in play. This class is 
	 * mainly used for creating rooms and joining rooms
	 *****************************************************/

	private static final long serialVersionUID = 3L;
	int roomnumber; // the roomnumber of this room
	public List<Team> Teams; // the list of teams in the current room

	public Room() // constructor 1
	{
		super();
		Teams = new LinkedList<Team>();
	}

	public Room(int Roomnumber) // constructor 2
	{
		super();
		Teams = new LinkedList<Team>();
		roomnumber = Roomnumber;
	}

	public Room(Room r) // constructor 3
	{
		super();
		Teams = new LinkedList<Team>();
		roomnumber = r.roomnumber;
		for(int i=0;i<r.Teams.size();i++)
		{
			Teams.add(new Team(r.getTeam(i).getName()));
			for(int j=0;j<r.getTeam(i).players.size();j++)
			{
				Teams.get(i).players.add(r.getTeam(i).players.get(j));
			}
		}
	}

	public void addTeams(Team t) // add a team to the teamlist
	{
		if(searchTeams(t.TeamName)==-1)
		{
			Teams.add(t);
		}
	}
	
	public int searchTeams(String teamname) // search for a team's index in the teamlist
	{
		if(Teams==null)
		{
			return -1;
		}
		for(int i=0;i<Teams.size();i++)
		{
			if(Teams.get(i).TeamName.equals(teamname))
			{
				return i;
			}
		}
		return -1;
	}

	public Team getTeam(int i) // get a specific team
	{
		if(i==-1)
		{
			return null;
		}
		Team t = null;
		t = Teams.get(i);
		return t;
	}
	
	public void removeplayerfromroom(String playername) // remove a specific player from this room
	{
		for(int i=0;i<Teams.size();i++)
		{
			for(int j=0;j<Teams.get(i).players.size();j++)
			{
				if(Teams.get(i).players.get(j).equals(playername))
				{
					Teams.get(i).players.remove(j);
				}
			}
		}
	}
	
	public int teamnumber() // get the number of teams in the room
	{
		if(Teams!=null)
		{
			return Teams.size();
		}
		else
		{
			return 0;
		}
	}
	
	// getters and setters, auto generated
	public void setRoomnumber(int roomnumber1)
	{
		roomnumber = roomnumber1;
	}
	
	public int getRoomnumber()
	{
		return roomnumber;
	}







}
