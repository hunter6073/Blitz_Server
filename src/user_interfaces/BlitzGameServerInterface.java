package user_interfaces;

import java.awt.EventQueue;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import server_connections.Player_Info_active;
import server_connections.Room;
import server_connections.SocketManager;
import server_connections.SocketThread;
import server_connections.Team;
import utility.testThread;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class BlitzGameServerInterface extends JFrame {
	
	/******************************************************
	 * Creator: Ted Wong
	 * StudentID: 2014012687
	 * Major: Software Engineering
	 * Class: 143
	 * Description: This is the main interface of the server, it 
	 * Shows the main system messages of users connecting and disconnecting. 
	 * Also, this interface shows the rooms created by players via a list of buttons.
	 *****************************************************/

	private static final long serialVersionUID = -3250662690140310525L;
	//initializing the components
	private JPanel contentPane;	// content pane
	private JTextArea textArea; // output game messages
	private JPanel RoomPanel; // panel for rooms
	private String system_message="";  // message for system output
	private LinkedList<JButton> buttonlist = new LinkedList<JButton>(); // a list of buttons for entering room interfaces
	private static final BlitzGameServerInterface serv = new BlitzGameServerInterface();//create an instance of the server interface when program starts
	public static BlitzGameServerInterface getServer() //return a instance of client object
	{
		return serv;
	}
	public JPanel getRoomPanel() // get room panel
	{
		return RoomPanel;
	}
	// start up the program
	public static void main(String[] args) {
		// startup the interface
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					getServer().setVisible(true); // show the interface
					createTest(); // create a room with a fake player 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		SocketThread st = new SocketThread(); // start the main thread for server connections
		st.start();
	}

	//constructor for creating the frame
	public BlitzGameServerInterface()  
	{
		setTitle("Blitz Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 565, 442);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 60, 245, 322);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);

		JLabel lblSystemMessages = new JLabel("system messages");
		lblSystemMessages.setBounds(14, 29, 245, 18);
		contentPane.add(lblSystemMessages);

		JLabel lblCurrentRooms = new JLabel("current rooms");
		lblCurrentRooms.setBounds(332, 29, 201, 18);
		contentPane.add(lblCurrentRooms);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_1.setBounds(342, 60, 180, 322);
		contentPane.add(scrollPane_1);

		RoomPanel = new JPanel();
		scrollPane_1.setViewportView(RoomPanel);
		GridBagLayout gbl_RoomPanel = new GridBagLayout();
		gbl_RoomPanel.columnWidths = new int[]{0, 0};
		gbl_RoomPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_RoomPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_RoomPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		RoomPanel.setLayout(gbl_RoomPanel);

	}

	public void ShowMessage(String message) // show system text messages
	{

		system_message+=message;
		if(!message.equals(""))
		{
			system_message+="\n";
		}
		textArea.setText(system_message);

	}

	public void CreateRooms(RoomInterface RI,int n)//create room buttons(click and enter Room Interface)
	{
		JButton btnNewButton = new JButton("Room"+n); // create a new button and set its listener
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RI.setVisible(true);
			}
		});
		buttonlist.add(btnNewButton); // add button to buttonlist
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = buttonlist.size();
		RoomPanel.add(buttonlist.get(buttonlist.size()-1), gbc_btnNewButton);
		RoomPanel.revalidate();
	}

	public void RemoveRoom(int n)//remove a room from interface
	{
		buttonlist.remove(n); // switch the unused button with null
		buttonlist.add(n, null);
		RoomPanel.remove(n); // remove the button component from the room panel
		RoomPanel.revalidate();
		RoomPanel.setVisible(false);
		RoomPanel.setVisible(true);
	}

	public static void createTest() // create test conditions
	{
		SocketManager.getsocketmanager().create_room("fakeEnemy");
		Room m = new Room(1);
		Team t = new Team("testTeam1");
		t.addPlayer("fakeEnemy");
		m.addTeams(t);
		m.addTeams(new Team("testTeam2"));
		SocketManager.getsocketmanager().update_room(m);
		Player_Info_active pi = new Player_Info_active("fakeEnemy",1,1,1,250);
		SocketManager.getsocketmanager().find_RoomInterface(pi.getRoomnumber()).processDP(pi);
		testThread tt = new testThread(1);
		tt.start();
		

	}

}
