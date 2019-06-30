package user_interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import server_connections.Player_Info_active;
import server_connections.Room;
import server_connections.SocketManager;
import server_connections.TransferSocket;
import unused.FireCalibration;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RoomInterface extends JFrame { 

	/******************************************************
	 * Creator: Ted Wong
	 * StudentID: 2014012687
	 * Major: Software Engineering
	 * Class: 143
	 * Description: This is the room interface, it shows the match played currently
	 * the interface consists of three components, team message board, match message board 
	 * and the game map(not available yet)

	 *****************************************************/

	private JPanel contentPane; 
	public Room r; // the current room
	private String TeamMessage=""; // message for teams
	private String GameMessage=""; // message for the game
	JTextArea TeamM; // textarea for team messages
	JTextArea GameM; // textarea for game messages
	GameMapPanel panel;    // game map 
	public List<Player_Info_active> pia_list; // all the players in the room
	List<Integer> player_status; //player alive:1 dead:0

	public LinkedList<int[]> testlist = new LinkedList<int[]>(); // TODO delete this after testing
	int testroll = 0;
	public RoomInterface(int roomnum,Room r1)  // constructor
	{
		this.r = r1;
		setTitle("Room NO."+ roomnum);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1276, 441);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(24, 40, 240, 341);
		contentPane.add(scrollPane);
		TeamM = new JTextArea();
		scrollPane.setViewportView(TeamM);
		JLabel lblTeamDistribution = new JLabel("Team distribution");
		lblTeamDistribution.setBounds(26, 13, 162, 18);
		contentPane.add(lblTeamDistribution);
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(295, 37, 571, 344);
		contentPane.add(scrollPane_1);
		GameM = new JTextArea();
		scrollPane_1.setViewportView(GameM);
		JLabel lblRoomMessage = new JLabel("Room message");
		lblRoomMessage.setBounds(295, 13, 181, 18);
		contentPane.add(lblRoomMessage);
		panel = new GameMapPanel();
		panel.setBounds(901, 40, 337, 341);
		contentPane.add(panel);
		testlist.add(new int[]{3,5,0});
		testlist.add(new int[]{5,1,0});
		testlist.add(new int[]{3,-4,0});
		testlist.add(new int[]{-2,3,0});
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Player_Info_active pi = new Player_Info_active("fakeEnemy",1,testlist.get(testroll)[0],testlist.get(testroll)[1],testlist.get(testroll)[2]);
				processDP(pi);
				testroll++;
				if(testroll>3)
				{
					testroll = 0;
				}


			}
		});
		panel.add(btnNewButton);
		JLabel lblGameMap = new JLabel("Game Map");
		lblGameMap.setBounds(901, 13, 72, 18);
		contentPane.add(lblGameMap);
		pia_list = new LinkedList<Player_Info_active>();
		player_status = new LinkedList<Integer>();

	}

	public void SendTeamMessage(String msg) // set text for textarea
	{
		TeamMessage = "";
		TeamMessage+=msg;
		if(!msg.equals(""))
		{
			TeamMessage+="\n";
		}
		TeamM.setText(TeamMessage);
	}

	public void updateTeam() // show updated team messages
	{
		TeamMessage = "";
		for(int i=0;i<r.teamnumber();i++)
		{
			TeamMessage+="Team:"+r.getTeam(i).getName();
			for(int j = 0;j<r.getTeam(i).getPlayers().size();j++)
			{
				TeamMessage = TeamMessage+"\n"+"player:"+r.getTeam(i).getPlayers().get(j);
			}
			TeamMessage+="\n";

		}

		SendTeamMessage(TeamMessage) ;

	}

	public boolean updateplayer_info (Player_Info_active p) // update player's player_info_active information
	{

		for(int i=0;i<pia_list.size();i++)
		{
			if(p.getUsername().equals(pia_list.get(i).getUsername()))
			{
				pia_list.remove(i);
				pia_list.add(i, p);
				return true;
			}
		}
		pia_list.add(p);
		player_status.add(1);
		return true;
	}

	public void processDP(Player_Info_active p) //process a player's direction and position
	{
		updateplayer_info(p); // update player's direction and position information;
		refresh_board(); // show updated information;
		for(int i=0;i<pia_list.size();i++)//give enemy list to all players in the room
		{
			if(player_status.get(i)==1)
			{
				if(SocketManager.getsocketmanager().getsocket(pia_list.get(i).getUsername())!=null&&!getEnemyList(p).equals(""))
				{
					SocketManager.getsocketmanager().getsocket(pia_list.get(i).getUsername()).outputObject("enemy_list", getEnemyList(p));
				}

			}
		}
		//Server.getServer().getMap().n = pi.direction; TODO display map

	}
	
	public void process_dummy(Player_Info_active p)//TODO delete after testing
	{
		updateplayer_info(p); // update player's direction and position information;
		refresh_board(); // show updated information;
	}

	String get_team(String username) // get player's team name
	{
		for(int i=0;i<r.Teams.size();i++)
		{
			for(int j=0;j<r.Teams.get(i).players.size();j++)
			{
				if(username.equals(r.Teams.get(i).players.get(j)))
				{
					return r.Teams.get(i).getName();
				}
			}
		}
		return "";
	}

	public void remove_dead(Player_Info_active pi) // set dead player's status
	{
		for(int i=0;i<pia_list.size();i++)
		{
			if(pia_list.get(i).getUsername().equals(pi.getUsername()))
			{
				player_status.remove(i);
				player_status.add(i,0);
			}
		}

		refresh_board();
		//if only one team left , game over
		String teamname = "";
		int survivor = -1;
		for(int i=0;i<pia_list.size();i++) // find surviving team
		{
			if(player_status.get(i).equals(1))
			{
				teamname = get_team(pia_list.get(i).getUsername());
				survivor = i;
				break;
			}
		}

		boolean gameover = true;

		for(int i=0;i<player_status.size();i++) // find different surviving team
		{
			if(player_status.get(i).equals(1)&&!(get_team(pia_list.get(i).getUsername()).equals(teamname)))
			{
				gameover = false;
				break;
			}
		}

		if(gameover)
		{

			if(SocketManager.getsocketmanager().getsocket(pia_list.get(survivor).getUsername())!=null)
			{
				SocketManager.getsocketmanager().getsocket(pia_list.get(survivor).getUsername()).outputObject("game_over",teamname);// send game over to the player
			}

		}

	}

	public void removeplayerfromroom(String playername) // remove player from room
	{
		for(int i=0;i<pia_list.size();i++)
		{
			if(pia_list.get(i).getUsername().equals(playername))
			{
				pia_list.remove(i);
				r.removeplayerfromroom(playername);
			}
		}
	}

	public void refresh_board() // show current game message(a list of player information, including existing players' position and direction, and dead players' status);
	{
		GameMessage = "";
		for(int i=0;i<pia_list.size();i++)
		{
			if(player_status.get(i)==1)
			{
				GameMessage+=pia_list.get(i).toString()+"\n";
			}
			else
			{
				GameMessage+=pia_list.get(i).getUsername()+" has been removed from the field\n";
			}
		}
		GameM.setText(GameMessage);

	}

	public String getEnemyList(Player_Info_active p) // get a string of enemy list
	{

		String str = "";
		for(int i=0;i<pia_list.size();i++)
		{
			if(!get_team(pia_list.get(i).getUsername()).equals(get_team(p.getUsername()))&&player_status.get(i)==1)
			{
				str+=pia_list.get(i).getUsername()+":"+pia_list.get(i).getLocationx()+":"+pia_list.get(i).getLocationy()+":"+pia_list.get(i).getDirection()+";";
			}
		}

		return str;
	}
	// getters and setters, auto generated 
	public Room getR() // get room
	{
		return r;
	}

	public void setR(Room r) //set room
	{
		this.r = r;
	}
}
