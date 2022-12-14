package AdminInterfaces;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Server.Classes.Group;
import Server.Classes.Message;
import Server.Classes.User;
import Server.Controllers.GroupController;
import Server.Controllers.MessageController;
import Server.Controllers.UserController;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class Main extends JFrame {

	private String indexMessage;
	private JPanel contentPane;
	private JPanel panelMainContent;

	private ImageIcon iconManagement = new ImageIcon(Main.class.getResource("/Image/UserManagement.png"));
	private ImageIcon iconLoginHis = new ImageIcon(Main.class.getResource("/Image/LoginHistory.png"));
	private ImageIcon iconGroupChat = new ImageIcon(Main.class.getResource("/Image/GroupChat.png"));
	private ImageIcon iconLogo = new ImageIcon(Main.class.getResource("/Image/Logo.png"));

	/**
	 * Panel UI
	 */
	private PanelManagement PanelManage;
	private LoginHistory PanelLoginHis;
	private GroupChat PanelGroupChat;

	/**
	 * Controller
	 */
	private UserController userController;
	private GroupController groupController;
	private MessageController messageController;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @Attribute: Boolean - Waiting Client Response True if server are waiting for
	 *             client's response False if server are not waiting, or just got
	 *             response
	 */
	private boolean waitingClientResponse;

	/**
	 * @Attribute: int - port
	 *
	 */
	private static int port = 8080;

	/**
	 * @Attribute: HashMap - Users Online users list
	 */
	private HashMap<Socket, User> users;

	/**
	 * @Attribute: HashMap - Accounts database
	 */
	private ArrayList<User> accounts;

	/**
	 * @Attribute: HashMap - Groups database
	 */
	private ArrayList<Group> groups;

	/********************************************************************************************
	 * MAIN FUNCTION *
	 ********************************************************************************************/
	/**
	 * Main function
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initializes the AppChat
	 */
	public Main() {
		userController = new UserController();
		groupController = new GroupController();
		messageController = new MessageController();
		//T??i kho???n, nh??m
		accounts = userController.getAllUsers();
		groups = groupController.getAllGroups();
		initUI();
		Thread openServer = new Thread(() -> waitClients());
		openServer.start();
	}

	/*************************************************************************************
	 * USER INTERFACE *
	 *************************************************************************************/

	/**
	 * Setting display component content panel PanelManage: Page management user.
	 * PanelLoginHis: Page show history. PanelGroupChat: Page management.
	 */
	public void menuClicked(JPanel panel) {
		PanelManage.setVisible(false);
		PanelLoginHis.setVisible(false);
		PanelGroupChat.setVisible(false);

		panel.setVisible(true);
	}

	private void initUI() {

		setResizable(false);

		// Setting content panel
		setTitle("Administrator");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				String[] ObjButtons = { "Yes", "No" };
				int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Confirmation",
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
				if (PromptResult == 0) {
					for (Socket e : users.keySet()) {
						sendMessage(e, "Command_Disconnection");
					}
					System.exit(0);
				}
			}
		});
		setBounds(100, 100, 1350, 520);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaptionBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		// Init component
		PanelManage = new PanelManagement(this);
		PanelLoginHis = new LoginHistory();
		PanelGroupChat = new GroupChat();

		// Panel Logo
		JPanel panelAdmin = new JPanel();
		panelAdmin.setBackground(Color.LIGHT_GRAY);
		panelAdmin.setLayout(null);

		JLabel lblIconLogo = new JLabel("");
		lblIconLogo.setForeground(Color.BLACK);
		lblIconLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblIconLogo.setFont(new Font("Dialog", Font.BOLD, 20));
		lblIconLogo.setBounds(0, 0, 250, 140);
		Image img = iconLogo.getImage(); // transform it
		Image newimg = img.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
		lblIconLogo.setIcon(new ImageIcon(newimg));
		panelAdmin.add(lblIconLogo);

		// Panel users management
		JPanel panelManagement = new JPanel();
		panelManagement.addMouseListener(new PanelButtonMouseAdapter(panelManagement) {
			@Override
			public void mouseClicked(MouseEvent e) {
				menuClicked(PanelManage);
			}
		});
		panelManagement.setBackground(Color.LIGHT_GRAY);
		panelManagement.setBounds(0, 150, 250, 60);
		panelAdmin.add(panelManagement);
		panelManagement.setLayout(null);

		JLabel lblManagement = new JLabel("   Qu???n l?? ng?????i d??ng");
		lblManagement.setHorizontalAlignment(SwingConstants.LEFT);
		lblManagement.setForeground(Color.BLACK);
		lblManagement.setFont(new Font("Dialog", Font.BOLD, 14));
		lblManagement.setBounds(5, 0, 245, 60);
		Image img1 = iconManagement.getImage(); // transform it
		Image newimg1 = img1.getScaledInstance(36, 47, java.awt.Image.SCALE_SMOOTH);
		lblManagement.setIcon(new ImageIcon(newimg1));
		panelManagement.add(lblManagement);

		// Panel Login History
		JPanel panelLoginHis = new JPanel();
		panelLoginHis.addMouseListener(new PanelButtonMouseAdapter(panelLoginHis) {
			@Override
			public void mouseClicked(MouseEvent e) {
				menuClicked(PanelLoginHis);
			}
		});
		panelLoginHis.setBackground(Color.LIGHT_GRAY);
		panelLoginHis.setBounds(0, 210, 250, 60);
		panelAdmin.add(panelLoginHis);
		panelLoginHis.setLayout(null);

		JLabel lblLoginHis = new JLabel("L???ch s??? ????ng nh???p");
		lblLoginHis.setForeground(Color.BLACK);
		lblLoginHis.setFont(new Font("Dialog", Font.BOLD, 14));
		lblLoginHis.setBounds(2, 0, 248, 60);
		Image img2 = iconLoginHis.getImage(); // transform it
		Image newimg2 = img2.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		lblLoginHis.setIcon(new ImageIcon(newimg2));
		panelLoginHis.add(lblLoginHis);

		// Panel Group Chat
		JPanel panelGroupChat = new JPanel();
		panelGroupChat.addMouseListener(new PanelButtonMouseAdapter(panelGroupChat) {
			@Override
			public void mouseClicked(MouseEvent e) {
				menuClicked(PanelGroupChat);
			}
		});
		panelGroupChat.setLayout(null);
		panelGroupChat.setBackground(Color.LIGHT_GRAY);
		panelGroupChat.setBounds(0, 270, 250, 60);
		panelAdmin.add(panelGroupChat);

		JLabel lblGroupChat = new JLabel("  Qu???n l?? nh??m chat");
		lblGroupChat.setForeground(Color.BLACK);
		lblGroupChat.setFont(new Font("Dialog", Font.BOLD, 14));
		lblGroupChat.setBounds(5, 0, 245, 60);
		Image img3 = iconGroupChat.getImage(); // transform it
		Image newimg3 = img3.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		lblGroupChat.setIcon(new ImageIcon(newimg3));
		panelGroupChat.add(lblGroupChat);

		// Panel components content
		panelMainContent = new JPanel();
		panelMainContent.setLayout(null);

		panelMainContent.add(PanelManage);
		panelMainContent.add(PanelLoginHis);
		panelMainContent.add(PanelGroupChat);
		menuClicked(PanelManage);

		// Group Panel
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(panelAdmin, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE).addGap(5)
						.addComponent(panelMainContent, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panelAdmin, GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
				.addComponent(panelMainContent, GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE));
		contentPane.setLayout(gl_contentPane);
	}

	private class PanelButtonMouseAdapter extends MouseAdapter {
		JPanel panel;

		public PanelButtonMouseAdapter(JPanel panel) {
			this.panel = panel;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			panel.setBackground(new Color(240, 240, 240));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			panel.setBackground(Color.LIGHT_GRAY);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			panel.setBackground(new Color(160, 160, 160));
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			panel.setBackground(new Color(240, 240, 240));
		}
	}

	/********************************************************************************
	 * SUPPORT FUNCTION *
	 ********************************************************************************/
	private int getAccountIndex(String username) {
		for (int i = 0; i < accounts.size(); i++) {
			if (accounts.get(i).getInfor().getUsername().equals(username))
				return i;
		}
		return -1;
	}

	private int getGroupIndex(String groupName) {
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getGroupName().equals(groupName))
				return i;
		}
		return -1;
	}

	private boolean containUsername(String username) {
		for (User user : accounts) {
			if (user.getInfor().getUsername().equals(username))
				return true;
		}
		return false;
	}

	private String createMemberString(int groupIndex) {
		String str = "";
		for (int i = 0; i < groups.get(groupIndex).getlistUsers().size(); i++) {
			if (groups.get(groupIndex).getManagers().contains(groups.get(groupIndex).getlistUsers().get(i)))
				str += "`" + groups.get(groupIndex).getlistUsers().get(i) + ":Qu???n tr??? vi??n";
			else
				str += "`" + groups.get(groupIndex).getlistUsers().get(i) + ":Th??nh vi??n";
		}
		return str;
	}

	private void sendCommandMsg2AllMenber(int groupIndex, String msg) {
		for (int i = 0; i < groups.get(groupIndex).getlistUsers().size(); i++)
			for (Socket socket : users.keySet())
				if (users.get(socket).getInfor().getUsername().equals(groups.get(groupIndex).getlistUsers().get(i)))
					sendMessage(socket, msg);
	}

	public void refreshGroups() {
		PanelGroupChat.refreshTable();
	}

	public void refreshAccount() {
		accounts = userController.getAllUsers();
		for (Socket socket : users.keySet())
			sendUserList(socket, accounts.get(getAccountIndex(users.get(socket).getInfor().getUsername())));
		System.out.println("Update complete!");
	}

	public boolean containUser(String username) {
		for (User user : users.values())
			if (user.getInfor().getUsername().equals(username))
				return true;
		return false;
	}

	public Socket getSocketByUser(String username) {
		for (Socket socket : users.keySet()) {
			if (users.get(socket).getInfor().getUsername().equals(username)) {
				return socket;
			}
		}
		return null;
	}

	// Check friend
	public Boolean checkFriend(String username, String friend, ArrayList<String> friends) {
		for (String e : friends) {
			if (e.equals(friend))
				return true;
		}
		return false;
	}

	public ArrayList<User> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<User> accounts) {
		this.accounts = accounts;
	}

	public GroupController getGroupController() {
		return groupController;
	}

	public MessageController getMessageController() {
		return messageController;
	}

	public UserController getUserController() {
		return userController;
	}

	/**********************************************************************************
	 * COMUNICATION WITH CLIENT *
	 **********************************************************************************/

	/**
	 * Wait for clients
	 */
	private void waitClients() {
		users = new HashMap<>();
		waitingClientResponse = false;
		try {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				// T??i kho???n, nh??m
				accounts = userController.getAllUsers();
				groups = groupController.getAllGroups();

				System.out.println("\nServer ??ang ch???y t???i port " + port + "...");
				while (true) {
					Socket client = serverSocket.accept();
					if (client == null)
						break;
					Thread receiveClientMessage = new Thread(() -> receiveClientMessages(client));
					receiveClientMessage.start();
				}
			}
		} catch (Exception exception) {
			System.out.println("Kh??ng th??? t???o server v?? c?? m???t server kh??c ??ang ch???y!");
			String[] ObjButtons = { "OK" };
			int PromptResult = JOptionPane.showOptionDialog(null, "Server is running...", "Confirmation",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[0]);
			System.exit(0);
		}
	}

	/**
	 * Add online user
	 * 
	 * @param socket   Socket
	 * @param username String
	 */
	public void addUserLogin(Socket socket, String username) {
		User getUser = userController.getUserByUsername(username);

		users.put(socket, getUser);
		sendUserList(socket, getUser);
		sendGroupList(socket, getUser);

		System.out.println(username + " connection success!");
	}

	/**
	 * Remove online user
	 * 
	 * @param socket Socket
	 */
	public void removeUser(Socket socket) {
		users.remove(socket);
	}

	/**
	 * Send user list to user
	 * 
	 * @param socket - Socket
	 * @param User   - user
	 */
	public void sendUserList(Socket socket, User user) {
		StringBuilder userList = new StringBuilder("Command_UserList");
		System.out.println("Sending user list");
		for (int i = 0; i < user.getFriend().size(); i++)
			userList.append("`").append(user.getFriend().get(i));

		sendMessage(socket, userList.toString());
	}

	/**
	 * Send group list to user
	 * 
	 * @param socket - Socket
	 * @param User   - user
	 */
	public void sendGroupList(Socket socket, User user) {
		StringBuilder groupList = new StringBuilder("Command_GroupList");

		for (int i = 0; i < groups.size(); i++)
			if (groups.get(i).getlistUsers().contains(user.getInfor().getUsername()))
				groupList.append("`").append(groups.get(i).getGroupName());

		sendMessage(socket, groupList.toString());
	}

	/**
	 * Send a message to client
	 * 
	 * @param client  Socket
	 * @param message String
	 */
	public void sendMessage(Socket client, String message) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			bufferedWriter.write(message);
			bufferedWriter.newLine();
			bufferedWriter.flush();

		} catch (Exception exception) {
			removeUser(client);
		}
	}

	/**
	 * Receive and Process Message from client
	 * 
	 * @param client Socket
	 */

	private void receiveClientMessages(Socket client) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

			while (true) {
				String receivedMessage = bufferedReader.readLine() + "";
				if (receivedMessage != null) {
					System.out.println(receivedMessage);
					System.out.println("Danh s??ch ??ang online: " + users.size());
				}
				if (receivedMessage.contains("Command_CloseConnect")) {
					sendMessage(client, "Command_CloseConnect");
					int i = getAccountIndex(users.get(client).getInfor().getUsername());
					accounts.get(i).getInfor().setStatus(false);

					// Chuy???n tr???ng th??i offline cho user
					PanelManage.changeStatusUserByUsername(accounts.get(i).getInfor().getUsername(), "Offline");

					// Chuy???n tr???ng th??i offline cho friend
					for (int j = 0; j < accounts.get(i).getFriend().size(); j++) {
						String name = accounts.get(i).getFriend().get(j);
						for (Socket socket : users.keySet())
							if (users.get(socket).getInfor().getUsername().equals(name)) {
								sendMessage(socket, "Command_changeFriendStatus`"
										+ accounts.get(i).getInfor().getUsername() + "`Offline");
							}
					}

					bufferedReader.close();
					removeUser(client);
					client.close();

				} else if (receivedMessage.contains("Command_SignedIn")) {
					String[] str = receivedMessage.split("`");
					addUserLogin(client, str[1]);

					// G???i danh s??ch l???i m???i k???t b???n cho client:
					String str2 = "Command_NewAddFriendRequest`";
					ArrayList<String> listData = users.get(client).getListAddFriend();
					for (String string : listData) {
						str2 += string + "`";
					}
					sendMessage(client, str2);
				}

				else if (receivedMessage.contains("Command_AccountVerify")) {
					String[] str = receivedMessage.split("`");
					int i = getAccountIndex(str[1]);
					if (i == -1) {
						sendMessage(client, "Command_AccountVerifyFailed");
					} else if (accounts.get(i).getInfor().getPassword().equals(str[2])) {
						if (containUser(str[1]))
							sendMessage(client, "Command_AccountVerifyAlready");
						else if (accounts.get(i).getInfor().getBlocked())
							sendMessage(client, "Command_AccountVerifyBlocked");
						else {
							sendMessage(client, "Command_AccountVerifyAccepted");
							accounts.get(i).getInfor().setStatus(true);
							accounts.get(i).getTimeLogin().add(
									DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(LocalDateTime.now()));
							userController.addLogin(accounts.get(i).getId());

							// Chuy???n tr???ng th??i online cho user
							PanelManage.changeStatusUserByUsername(accounts.get(i).getInfor().getUsername(), "Online");

							// Th??m v??o danh s??ch l???ch s??? ????ng nh???p
							PanelLoginHis.addToListUser(accounts.get(i));

							// Chuy???n tr???ng th??i online cho friend
							for (int j = 0; j < accounts.get(i).getFriend().size(); j++) {
								String name = accounts.get(i).getFriend().get(j);
								for (Socket socket : users.keySet())
									if (users.get(socket).getInfor().getUsername().equals(name)) {
										sendMessage(socket, "Command_changeFriendStatus`"
												+ accounts.get(i).getInfor().getUsername() + "`Online");
									}
							}
						}
					} else {
						sendMessage(client, "Command_AccountVerifyFailed");
					}

				}

				else if (receivedMessage.contains("Command_CreateAccount")) {
					String[] str = receivedMessage.split("`");

					if (getGroupIndex(str[1]) == -1 && getAccountIndex(str[1]) == -1) {
						User createUser = new User(str[1], str[2], str[3], str[4], str[5], str[6], str[7]);
						Boolean created = userController.create(createUser);
						accounts.add(createUser);
						if (created) {
							sendMessage(client, "Command_CreateAccountAccepted");
							PanelManage.refreshList();
						} else {
							sendMessage(client, "Command_CreateAccountFailed");
						}
					} else
						sendMessage(client, "Command_CreateAccountFailed");
				}

				else if (receivedMessage.contains("Command_AddFriendRequest")) {
					// ******************
					String[] str = receivedMessage.split("`");
					if (str[1].equals(users.get(client).getInfor().getUsername()))
						sendMessage(client, "Command_AddFriendRequestSelf");

					else if (users.get(client).getFriend().contains(str[1]))
						sendMessage(client, "Command_AddFriendRequestIsFriend");

					else if (containUsername(str[1])) {
						// G???i l???i m???i k???t b???n ?????n socket c???a client n???u ng?????i ???? ??ang onl:
						for (Socket socket : users.keySet())
							if (users.get(socket).getInfor().getUsername().equals(str[1])) {
								if (!users.get(socket).getListAddFriend()
										.contains(users.get(client).getInfor().getUsername())) {
									users.get(socket).addAddFriendRequest(users.get(client).getInfor().getUsername());

									sendMessage(socket, "Command_NewAddFriendRequest`"
											+ users.get(client).getInfor().getUsername());
								}
							}

						// L??u v??o account:
						accounts.get(getAccountIndex(str[1]))
								.addAddFriendRequest(users.get(client).getInfor().getUsername());

						sendMessage(client, "Command_AddFriendRequestAccepted");

						// L??u v??o db:
						userController.addRequestFriend(userController.getUserByUsername(str[1]).getId(),
								users.get(client).getInfor().getUsername());
					} else
						sendMessage(client, "Command_AddFriendRequestFailed");

				}

				else if (receivedMessage.contains("Command_AcceptAddFriendRequest")) {
					String[] str = receivedMessage.split("`");

					// X??a l???i m???i k???t b???n tr??n interface:
					sendMessage(client,
							"Command_deleteAddFriendRequest`" + users.get(client).getListAddFriend().indexOf(str[1]));

					// X??a l???i m???i k???t b???n trong users:
					users.get(client).deleteAddFriendRequest(str[1]);

					// X??a l???i m???i k???t b???n trong accounts:
					accounts.get(getAccountIndex(users.get(client).getInfor().getUsername()))
							.deleteAddFriendRequest(str[1]);

					// X??a l???i m???i k???t b???n trong db:
					userController.deleteRequestFriend(users.get(client).getId(), str[1]);

					// Th??m b???n v??o accounts:
					accounts.get(getAccountIndex(str[1])).addFriend(users.get(client).getInfor().getUsername());
					accounts.get(getAccountIndex(users.get(client).getInfor().getUsername())).addFriend(str[1]);

					for (Socket socket : users.keySet())
						if (users.get(socket).getInfor().getUsername().equals(str[1])) {
							// Th??m b???n v??o users:
							users.get(socket).addFriend(users.get(client).getInfor().getUsername());
							users.get(client).addFriend(str[1]);

							// Th??m v??o danh s??ch ng?????i li??n h???:
							sendUserList(socket,
									accounts.get(getAccountIndex(users.get(socket).getInfor().getUsername())));
						}

					// Th??m v??o danh s??ch ng?????i li??n h???:
					sendUserList(client, accounts.get(getAccountIndex(users.get(client).getInfor().getUsername())));

					// Th??m b???n v??o db:
					userController.addFriend(accounts.get(getAccountIndex(str[1])).getId(),
							users.get(client).getInfor().getUsername());
					userController.addFriend(
							accounts.get(getAccountIndex(users.get(client).getInfor().getUsername())).getId(), str[1]);
				}

				else if (receivedMessage.contains("Command_deleteAddFriendRequest")) {
					String[] str = receivedMessage.split("`");

					// X??a l???i m???i k???t b???n trong users:
					sendMessage(client,
							"Command_deleteAddFriendRequest`" + users.get(client).getListAddFriend().indexOf(str[1]));
					users.get(client).deleteAddFriendRequest(str[1]);

					// X??a l???i m???i k???t b???n trong accounts:
					accounts.get(getAccountIndex(users.get(client).getInfor().getUsername()))
							.deleteAddFriendRequest(str[1]);

					// X??a l???i m???i k???t b???n trong db:
					userController.deleteRequestFriend(users.get(client).getId(), str[1]);
				}

				// Hi???n th??? danh s??ch b???n b??
				else if (receivedMessage.contains("Command_ShowFriendList")) {
					String str = "Command_ShowFriendList";
					int index = getAccountIndex(users.get(client).getInfor().getUsername());

					for (int i = 0; i < accounts.get(index).getFriend().size(); i++) {
						String friendName = accounts.get(index).getFriend().get(i);
						String friendStatus = accounts.get(getAccountIndex(friendName)).getInfor().getStatus()
								? "Online"
								: "Offline";
						str += "`" + friendName + ":" + friendStatus;
					}

					sendMessage(client, str);
				}

				// H???y k???t b???n
				else if (receivedMessage.contains("Command_unfriend")) {
					String[] str = receivedMessage.split("`");

					// X??a b???n tr??n interface:
					sendMessage(client,
							"Command_unfriend`"
									+ accounts.get(getAccountIndex(users.get(client).getInfor().getUsername()))
											.getFriend().indexOf(str[1]));

					// X??a b???n trong account:
					accounts.get(getAccountIndex(users.get(client).getInfor().getUsername())).deleteFriend(str[1]);

					// X??a b???n trong users:
					users.get(client).deleteFriend(str[1]);

					for (Socket socket : users.keySet())
						if (users.get(socket).getInfor().getUsername().equals(str[1])) {
							// X??a b???n tr??n interface:
							sendMessage(socket, "Command_unfriend`" + accounts.get(getAccountIndex(str[1])).getFriend()
									.indexOf(users.get(client).getInfor().getUsername()));

							// X??a b???n trong account:
							accounts.get(getAccountIndex(str[1]))
									.deleteFriend(users.get(client).getInfor().getUsername());

							// X??a b???n trong users:
							users.get(socket).deleteFriend(users.get(client).getInfor().getUsername());
							users.get(client).deleteFriend(users.get(socket).getInfor().getUsername());

							// X??a trong danh s??ch ng?????i li??n h???:
							sendUserList(socket,
									accounts.get(getAccountIndex(users.get(socket).getInfor().getUsername())));
						}

					// X??a trong danh s??ch ng?????i li??n h???:
					sendUserList(client, accounts.get(getAccountIndex(users.get(client).getInfor().getUsername())));

					// X??a b???n trong db:
					userController.deleteFriend(accounts.get(getAccountIndex(str[1])).getId(),
							users.get(client).getInfor().getUsername());
					userController.deleteFriend(
							accounts.get(getAccountIndex(users.get(client).getInfor().getUsername())).getId(), str[1]);
				}

				else if (receivedMessage.contains("Command_CreateNewGroup")) {
					String[] str = receivedMessage.split("`");

					// Ki???m tra xem t??n m???i t???n t???i hay ch??a:
					if (getGroupIndex(str[1]) == -1 && getAccountIndex(str[1]) == -1) {
						String creator = users.get(client).getInfor().getUsername();
						ArrayList<String> members = new ArrayList<String>();
						ArrayList<String> managers = new ArrayList<String>();
						members.add(creator);
						managers.add(creator);
						for (int i = 2; i < str.length; i++) {
							members.add(str[i]);
						}
						Group createGroup = new Group(str[1], managers, members);
						Boolean created = groupController.create(createGroup);

						if (created) {
							groups.add(createGroup);
							sendMessage(client, "Command_CreateGroupAccepted");
							PanelGroupChat.refreshTable();
							for (int i = 0; i < members.size(); i++) {
								User testUser = userController.getUserByUsername(members.get(i));
								Socket testSocket = getSocketByUser(members.get(i));
								if (testSocket != null)
									addUserLogin(testSocket, members.get(i));
							}
						} else {
							sendMessage(client, "Command_CreateGroupFailed");
						}
					} else
						sendMessage(client, "Command_CreateGroupFailed");
					//
				} else if (receivedMessage.contains("Command_SendMessage")) {
					String[] str = receivedMessage.split("`");
					if (containUsername(str[1])) {
						System.out.println("ng?????i nh???n: " + str[1] + " ng?????i g???i: " + str[3]);
						// l??u message g???i th??nh c??ng v??o database
						indexMessage = messageController.findIndexBySender(str[3], str[1]);// ki???m tra A g???i B
						String temp = messageController.findIndexBySender(str[1], str[3]);
						if (!temp.equals("0")) {
							indexMessage = temp;
						}
						if (indexMessage.equals("0")) {// ki???m tra c?? l???y ???????c index hay kh??ng n???u == 0
							indexMessage = UUID.randomUUID().toString();// t???o m???t chu???i random
							System.out.println("kh???i t???o index th??nh c??ng:" + indexMessage);
						}
						messageController.create(new Message(str[3], str[1], str[2], indexMessage));
						System.out.println("luu tin nhan thanh cong ");
						for (Socket socket : users.keySet()) {
							if (users.get(socket).getInfor().getUsername().equals(str[1])) {// ki???m
								sendMessage(socket,
										"Command_Message`" + users.get(client).getInfor().getUsername() + "`" + str[2]);// g???i
																														// d???ng
																														// command
																														// +
																														// ng?????i
																														// nh???n
																														// +
																														// n???i
																														// dung
							}

						}
						sendMessage(client, "Command_SendMessageAccepted");

					} else {
						sendMessage(client, "Command_SendMessageFailed");
					}
					// g???i File
				} else if (receivedMessage.contains("Command_SendFile")) {
					String[] str = receivedMessage.split("`");
					if (containUser(str[1])) {
						sendMessage(client, "Command_SendMessageAccepted");// server ki???m tra t???n t???i ng?????i nh???n hay ko
																			// c?? th?? tr??? v??? l???nh h??? th???ng
																			// messageaccepted

						DataInputStream dataInputStream = new DataInputStream(client.getInputStream());//
						byte[] data = new byte[dataInputStream.readInt()];
						dataInputStream.readFully(data, 0, data.length);

						for (Socket socket : users.keySet()) {
							if (users.get(socket).getInfor().getUsername().equals(str[1])) {
								waitingClientResponse = true;
								sendMessage(socket,
										"Command_File`" + users.get(client).getInfor().getUsername() + "`" + str[2]);
								while (waitingClientResponse)
									System.out.print("");
								DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
								dataOutputStream.writeInt(data.length);
								dataOutputStream.write(data);
							}
						}
					} else {
						sendMessage(client, "Command_SendMessageFailed");
					}
				} else if (receivedMessage.contains("Command_MessageHistory")) {
					String[] str = receivedMessage.split("`");
					MessageController messGetFdataBase = new MessageController();
					if (str[2].equals("(Tin nh???n m???i)")) {
						str[2] = str[2].replace(" (Tin nh???n m???i)", "");
					}
					String stringArray = "";
					// lay tin nhan 1 gui 2
					indexMessage = messGetFdataBase.findIndexBySender(str[1], str[2]);

					String indexTemp = messGetFdataBase.findIndexBySender(str[2], str[1]);
					if (indexTemp.equals("0")) {
						System.out.println(indexTemp);
					} else {
						indexMessage = indexTemp;
					}
					System.out.println("history: get index: " + indexMessage);
					ArrayList<Message> historyMess = messGetFdataBase.findMessageByIndex(indexMessage);
					for (Message message : historyMess) {
						stringArray = stringArray.concat(message.getSenderId() + "~" + message.getReceiverId() + "~"
								+ message.getContent() + "~" + message.getCreateTime() + "~" + message.getSenderDelete()
								+ "~" + message.getReceiverDelete() + "`");
					}
					System.out.print("code 786: " + stringArray);

					for (Socket socket : users.keySet()) {
						if (users.get(socket).getInfor().getUsername().equals(str[1])) {
							sendMessage(socket, "Command_SendHistoryMessage`" + str[1] + "`" + stringArray);
						}
					}

				} else if (receivedMessage.contains("Command_MessageGroupHistory")) {

					String[] str = receivedMessage.split("`");
					String stringArray = "";
					System.out.println("code 818: " + receivedMessage);

					ArrayList<Message> historyMessReceived = messageController.findMessageByGroup(str[2]);
					for (Message message : historyMessReceived) {
						System.out.println("code 827:" + message.getContent());
						stringArray = stringArray.concat(message.getSenderId() + "~" + message.getContent() + "~"
								+ message.getCreateTime() + "`");
					}
					ArrayList<String> UsersList = groupController.searchListUsersByGroupName(str[2]);
					for (Socket socket : users.keySet()) {
						if (users.get(socket).getInfor().getUsername().equals(str[1])) {
							System.out.print("code 835: " + stringArray);
							sendMessage(socket,
									"Command_SendGroupHistoryMessage`" + str[1] + "`" + str[2] + "`" + stringArray);
						}

					}

				}

				else if (receivedMessage.contains("Command_ForgotPassword")) {
					String[] str = receivedMessage.split("`");
					int i = getAccountIndex(str[1]);
					System.out.println(str[1]);
					System.out.println("i = " + i);
					System.out.println(accounts.get(1).getInfor().getUsername());

					if (i == -1)
						sendMessage(client, "Command_ForgotPasswordFail");
					else {
						String email = accounts.get(i).getInfor().getEmail();
						if (email.isBlank())
							sendMessage(client, "Command_ForgotPasswordInvalid");
						else {
							String newPassword = ForgotPassword.randomPassword(6);
							accounts.get(i).getInfor().setPassword(newPassword);
							userController.updatePassword(str[1], newPassword);
							ForgotPassword.sendEmail("kltpchat@gmail.com", "bxqokcenihyxekfr", email, "Qu??n m???t kh???u",
									"Ch??o " + str[1] + ".\nM???t kh???u m???i c???a b???n l??: " + newPassword);
							sendMessage(client, "Command_ForgotPasswordSuccessful");
						}
					}
				}

				else if (receivedMessage.contains("Command_ChangePassword")) {
					String[] str = receivedMessage.split("`");
					String username = users.get(client).getInfor().getUsername();
					int i = getAccountIndex(username);

					if (!accounts.get(i).getInfor().getPassword().equals(str[1]))
						sendMessage(client, "Command_ChangePasswordFailed");

					else {
						// ?????i m???t kh???u trong accounts:
						accounts.get(i).getInfor().setPassword(str[2]);
						// ?????i m???t kh???u tr??n db:
						userController.updatePassword(username, str[2]);
						// G???i th??ng b??o v??? client:
						sendMessage(client, "Command_ChangePasswordSuccessful");
					}
				}

				else if (receivedMessage.contains("Command_SendGroupMessage")) {
					String[] str = receivedMessage.split("`");
					int index = getGroupIndex(str[1]);

					sendMessage(client, "Command_SendMessageAccepted");
					Message mess = new Message(str[3], str[1], str[2]);
					messageController.create(mess);
					groupController.addNewMessage(mess.getId(), groups.get(index).getGroupId());

					for (int i = 0; i < groups.get(index).getlistUsers().size(); i++) {
						for (Socket socket : users.keySet())
							if (users.get(socket).getInfor().getUsername()
									.equals(groups.get(index).getlistUsers().get(i)) && socket != client) {
								sendMessage(socket, "Command_GroupMessage`" + str[1] + "`" + str[2] + "~"
										+ mess.getCreateTime() + "`" + str[3]);
							}
					}
				}

				else if (receivedMessage.contains("Command_ShowGroupManagement")) {
					String[] str = receivedMessage.split("`");
					String str1 = "Command_ShowGroupManagement";
					int index = getGroupIndex(str[1]);

					for (int i = 0; i < groups.get(index).getlistUsers().size(); i++) {
						if (groups.get(index).getManagers().contains(groups.get(index).getlistUsers().get(i)))
							str1 += "`" + groups.get(index).getlistUsers().get(i) + ":Qu???n tr??? vi??n";
						else
							str1 += "`" + groups.get(index).getlistUsers().get(i) + ":Th??nh vi??n";
					}

					sendMessage(client, str1);
				}

				else if (receivedMessage.contains("Command_ChangeGroupName")) {
					String[] str = receivedMessage.split("`");
					int index = getGroupIndex(str[1]);

					// Ki???m tra quy???n admin:
					if (!groups.get(index).getManagers().contains(users.get(client).getInfor().getUsername()))
						sendMessage(client, "Command_NotPermitted");

					// Ki???m tra xem t??n m???i t???n t???i hay ch??a:
					else if (getGroupIndex(str[2]) != -1 || getAccountIndex(str[2]) != -1)
						sendMessage(client, "Command_ChangeGroupNameFail");

					else {
						// ?????i t??n nh??m trong db:
						groupController.update(groups.get(index).getGroupId(), str[2]);

						// ?????i t??n nh??m trong messages c???a db:
						messageController.updateReceiver(str[2], str[1]);

						sendMessage(client, "Command_ChangeGroupNameSuccessful");
						groups.get(index).setGroupName(str[2]);

						for (int i = 0; i < groups.get(index).getlistUsers().size(); i++) {
							for (Socket socket : users.keySet()) {
								if (users.get(socket).getInfor().getUsername()
										.equals(groups.get(index).getlistUsers().get(i))) {
									sendGroupList(socket, users.get(socket));
									sendMessage(socket, "Command_ChangeGroupNameSetConversationTitle`" + str[2]);
								}
							}
						}
					}
				}

				else if (receivedMessage.contains("Command_Invite2Group")) {
					String[] str = receivedMessage.split("`");
					int index = getGroupIndex(str[1]);

					if (!groups.get(index).getlistUsers().contains(users.get(client).getInfor().getUsername()))
						sendMessage(client, "Command_YouNotIn");

					else if (getAccountIndex(str[2]) == -1)
						sendMessage(client, "Command_Invite2GroupFail");

					else if (groups.get(index).getlistUsers().contains(str[2]))
						sendMessage(client, "Command_Invite2GroupAlreadyInGroup");

					else {
						sendMessage(client, "Command_Invite2GroupSucessful");

						// L??u v??o groups:
						groups.get(index).getlistUsers().add(str[2]);

						// G???i danh s??ch nh??m m???i:
						for (Socket socket : users.keySet())
							if (users.get(socket).getInfor().getUsername().equals(str[2]))
								sendGroupList(socket, users.get(socket));

						// Refresh group management table:
						String str1 = "Command_RefreshGroupManagementTable" + createMemberString(index);
						sendCommandMsg2AllMenber(index, str1);

						// L??u v??o db:
						groupController.addPeopleGroup(str[2], groups.get(index).getGroupId());
					}
				}

				else if (receivedMessage.contains("Command_LeftTheGroup")) {
					String[] str = receivedMessage.split("`");
					int index = getGroupIndex(str[1]);

					// N???u nh??m c??n t??? 2 th??nh vi??n tr??? l??n:
					if (groups.get(index).getlistUsers().size() > 1) {
						// X??a trong groups:
						groups.get(index).getlistUsers().remove(
								groups.get(index).getlistUsers().indexOf(users.get(client).getInfor().getUsername()));

						// Ki???m tra s??? l?????ng admin, n???u ng?????i r???i ??i l?? admin duy nh???t c???a nh??m th?? b???u
						// ng?????i ??? trong nh??m l??u nh???t l??m admin (trong groups):
						if (groups.get(index).getManagers().size() < 2
								&& groups.get(index).getManagers().contains(users.get(client).getInfor().getUsername()))
							groups.get(index).getManagers().add(groups.get(index).getlistUsers().get(0));

						// Set l???i conversationTitle:
						sendMessage(client, "Command_LeftTheGroupSetConverSationTitle");

						// G???i danh s??ch nh??m m???i:
						sendGroupList(client, users.get(client));

						// Refresh group management table:
						String str1 = "Command_RefreshGroupManagementTable" + createMemberString(index);
						sendCommandMsg2AllMenber(index, str1);

						// X??a trong db:
						groupController.removePeopleGroup(users.get(client).getInfor().getUsername(),
								groups.get(index).getGroupId());

						if (groups.get(index).getManagers().contains(users.get(client).getInfor().getUsername())) {
							// X??a trong groups:
							groups.get(index).getManagers().remove(groups.get(index).getManagers()
									.indexOf(users.get(client).getInfor().getUsername()));

							// Ki???m tra s??? l?????ng admin, n???u ng?????i r???i ??i l?? admin duy nh???t c???a nh??m th?? b???u
							// ng?????i ??? trong nh??m l??u nh???t l??m admin (trong db):
							if (groups.get(index).getManagers().size() < 2)
								groupController.addManagerGroup(groups.get(index).getlistUsers().get(0),
										groups.get(index).getGroupId());

							// X??a trong db:
							groupController.removeManagerGroup(users.get(client).getInfor().getUsername(),
									groups.get(index).getGroupId());
						}
					}

					// N???u nh??m ch??? c?? 1 th??nh vi??n duy nh???t th?? x??a nh??m ????:
					else {
						String tmp = groups.get(index).getGroupId();

						// X??a trong groups:
						groups.remove(index);

						// Set l???i conversationTitle:
						sendMessage(client, "Command_LeftTheGroupSetConverSationTitle");

						// G???i danh s??ch nh??m m???i:
						sendGroupList(client, users.get(client));

						// X??a trong db:
						groupController.deleteGroup(tmp);
					}
				}

				else if (receivedMessage.contains("Command_DeleteFromGroup")) {
					String[] str = receivedMessage.split("`");
					int index = getGroupIndex(str[1]);

					if (!groups.get(index).getlistUsers().contains(users.get(client).getInfor().getUsername()))
						sendMessage(client, "Command_YouNotIn`");

					else if (str[2].equals(users.get(client).getInfor().getUsername()))
						sendMessage(client, "Command_LeftTheGroupDeleteFromGroup`" + str[1]);

					else if (!groups.get(index).getlistUsers().contains(str[2]))
						sendMessage(client, "Command_TheyNotIn`");

					else if (!groups.get(index).getManagers().contains(users.get(client).getInfor().getUsername()))
						sendMessage(client, "Command_NotPermitted`");

					else {
						boolean isAdmin = false;
						if (groups.get(index).getManagers().contains(str[2]))
							isAdmin = true;

						// X??a trong groups:
						groups.get(index).getlistUsers().remove(groups.get(index).getlistUsers().indexOf(str[2]));
						if (isAdmin)
							groups.get(index).getManagers().remove(groups.get(index).getManagers().indexOf(str[2]));

						Socket tmp = null;
						for (Socket socket : users.keySet())
							if (users.get(socket).getInfor().getUsername().equals(str[2]))
								tmp = socket;

						if (tmp != null) {
							// Set l???i conversationTitle:
							sendMessage(tmp, "Command_LeftTheGroupSetConverSationTitle");

							// G???i danh s??ch nh??m m???i:
							sendGroupList(tmp, users.get(tmp));
						}

						// Refresh group management table:
						String str1 = "Command_RefreshGroupManagementTable" + createMemberString(index);
						sendCommandMsg2AllMenber(index, str1);

						// X??a trong db:
						groupController.removePeopleGroup(str[2], groups.get(index).getGroupId());
						if (isAdmin)
							groupController.removeManagerGroup(str[2], groups.get(index).getGroupId());
					}
				}

				else if (receivedMessage.contains("Command_ChangeRole")) {
					String[] str = receivedMessage.split("`");
					int index = getGroupIndex(str[1]);

					if (!groups.get(index).getlistUsers().contains(users.get(client).getInfor().getUsername()))
						sendMessage(client, "Command_YouNotIn`");

					else if (!groups.get(index).getlistUsers().contains(str[2]))
						sendMessage(client, "Command_TheyNotIn`");

					// Ki???m tra quy???n admin:
					else if (!groups.get(index).getManagers().contains(users.get(client).getInfor().getUsername()))
						sendMessage(client, "Command_NotPermitted");

					else if (str[2].equals(users.get(client).getInfor().getUsername()))
						sendMessage(client, "Command_ChangeRoleSelf`");

					else {
						// N???u ng?????i ???? ??ang l?? admin th?? cho xu???ng th??nh vi??n:
						if (groups.get(index).getManagers().contains(str[2])) {
							// X??a trong groups:
							groups.get(index).getManagers().remove(groups.get(index).getManagers().indexOf(str[2]));

							// Refresh group management table:
							String str1 = "Command_RefreshGroupManagementTable" + createMemberString(index);
							sendCommandMsg2AllMenber(index, str1);

							// X??a trong db:
							groupController.removeManagerGroup(str[2], groups.get(index).getGroupId());
						} else {
							// Th??m v??o groups:
							groups.get(index).getManagers().add(str[2]);

							// Refresh group management table:
							String str1 = "Command_RefreshGroupManagementTable" + createMemberString(index);
							sendCommandMsg2AllMenber(index, str1);

							// Th??m v??o db:
							groupController.addManagerGroup(str[2], groups.get(index).getGroupId());
						}
					}

				}

				else if (receivedMessage.contains("Command_SearchMsgHistoryAll")) {
					String[] str = receivedMessage.split("`");
					String str1 = "Command_SearchMsgHistory";
					ArrayList<Message> msg1 = messageController
							.findMessageBySender(users.get(client).getInfor().getUsername(), str[1]);
					ArrayList<Message> msg2 = messageController.findMessageBySender(str[1],
							users.get(client).getInfor().getUsername());

					for (Message msg : msg1) {
						if (msg.getContent().contains(str[2]))
							if (msg.getSenderDelete())
								str1 += "`" + msg.getSenderId() + "??" + msg.getCreateTime() + "??"
										+ "Tin nh???n ???? b??? x??a";
							else
								str1 += "`" + msg.getSenderId() + "??" + msg.getCreateTime() + "??" + msg.getContent();

					}

					for (Message msg : msg2) {
						if (msg.getContent().contains(str[2]))
							if (msg.getReceiverDelete())
								str1 += "`" + msg.getSenderId() + "??" + msg.getCreateTime() + "??"
										+ "Tin nh???n ???? b??? x??a";
							else
								str1 += "`" + msg.getSenderId() + "??" + msg.getCreateTime() + "??" + msg.getContent();
					}

					sendMessage(client, str1);
				}

				else if (receivedMessage.contains("Command_SearchMsgHistoryBySender")) {
					String[] str = receivedMessage.split("`");
					String str1 = "Command_SearchMsgHistory`";

					if (!str[2].equals(str[1]) && !str[2].equals(users.get(client).getInfor().getUsername()))
						sendMessage(client, str1);

					else {
						if (str[2].equals(str[1])) {
							ArrayList<Message> msg2 = messageController.findMessageBySender(str[1],
									users.get(client).getInfor().getUsername());

							for (Message msg : msg2)
								if (msg.getContent().contains(str[3])) {
									if (msg.getReceiverDelete())
										str1 += msg.getSenderId() + "??" + msg.getCreateTime() + "??"
												+ "Tin nh???n ???? b??? x??a`";
									else
										str1 += msg.getSenderId() + "??" + msg.getCreateTime() + "??" + msg.getContent()
												+ "`";
								}
						}

						else if (str[2].equals(users.get(client).getInfor().getUsername())) {
							ArrayList<Message> msg2 = messageController
									.findMessageBySender(users.get(client).getInfor().getUsername(), str[1]);

							for (Message msg : msg2)
								if (msg.getContent().contains(str[3])) {
									if (msg.getSenderDelete())
										str1 += msg.getSenderId() + "??" + msg.getCreateTime() + "??"
												+ "Tin nh???n ???? b??? x??a`";
									else
										str1 += msg.getSenderId() + "??" + msg.getCreateTime() + "??" + msg.getContent()
												+ "`";
								}
						}

						sendMessage(client, str1);
					}
				}

				else if (receivedMessage.contains("Command_SearchMsgHistoryInGroup")) {
					String[] str = receivedMessage.split("`");
					String str1 = "Command_SearchMsgHistory`";
					int index = getGroupIndex(str[1]);

					if (str[2].isBlank()) {
						for (int i = 0; i < groups.get(index).getlistUsers().size(); i++) {
							ArrayList<Message> msg2 = messageController
									.findMessageBySender(groups.get(index).getlistUsers().get(i), str[1]);
							for (Message msg : msg2)
								if (msg.getContent().contains(str[3]))
									str1 += msg.getSenderId() + "??" + msg.getCreateTime() + "??" + msg.getContent()
											+ "`";
						}
					}

					else {
						ArrayList<Message> msg2 = messageController.findMessageBySender(str[2], str[1]);
						for (Message msg : msg2)
							if (msg.getContent().contains(str[3]))
								str1 += msg.getSenderId() + "??" + msg.getCreateTime() + "??" + msg.getContent() + "`";
					}

					sendMessage(client, str1);
				}

				else if (receivedMessage.contains("Command_DeleteAllMsg")) {
					String[] str = receivedMessage.split("`");
					// Tin nh???n g???i:
					ArrayList<Message> msg1 = messageController
							.findMessageBySender(users.get(client).getInfor().getUsername(), str[1]);
					// Tin nh???n nh???n:
					ArrayList<Message> msg2 = messageController.findMessageBySender(str[1],
							users.get(client).getInfor().getUsername());

					// X??a tin nh???n g???i:
					for (Message msg : msg1)
						messageController.deleteByMsgSend(msg.getId());

					// X??a tin nh???n nh???n:
					for (Message msg : msg2)
						messageController.deleteByMsgReceive(msg.getId());
				}
			}

		} catch (Exception exception) {
			removeUser(client);
		}
	}
}
