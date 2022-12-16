package AdminForm;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Server.Classes.Group;
import Server.Classes.InforUser;
import Server.Classes.Message;
import Server.Controllers.GroupController;
import Server.Controllers.MessageController;
import Server.Controllers.UserController;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class Admin extends JFrame {

	private JPanel contentPane;
	private JPanel panelMainContent;

	private ImageIcon iconManagement = new ImageIcon(Admin.class.getResource("/Image/UserManagement.png"));
	private ImageIcon iconLoginHis = new ImageIcon(Admin.class.getResource("/Image/LoginHistory.png"));
	private ImageIcon iconGroupChat = new ImageIcon(Admin.class.getResource("/Image/GroupChat.png"));
	private ImageIcon iconLogo = new ImageIcon(Admin.class.getResource("/Image/Logo.png"));

	private PanelManagement PanelManage;
	private LoginHistory PanelLoginHis;
	private GroupChat PanelGroupChat;

	private UserController manageUsers;
	private GroupController grp;
	private MessageController messC;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Admin frame = new Admin();
					frame.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Admin() {
		init();
		manageUsers = new UserController();
		messC = new MessageController();
		Message m = new Message( "dd942b10-dbd6-47d3-ac4e-2b1a0fe1fa68","d2c41b73-7da6-40af-86f1-235dfd885bd8", "Long");
		InforUser newUser = new InforUser("Cho Tien","TienAnCuc","Tran Bao Long","admin@gmail.com","HCM","02/10/2002","Nam");
		
		messC.deleteByMsgSend("217f3ca7-4c1c-4777-8fd2-d8387c74f6ab");
//		messC.deleteByRemoveFriend("dd942b10-dbd6-47d3-ac4e-2b1a0fe1fa68","d2c41b73-7da6-40af-86f1-235dfd885bd8");
//		messC.deleteBy("d2bbf989-4ae7-4ad8-9615-b11b7a757452", true);
//		manageUsers.create(newUser);
//		messC.create(m);
//		messC.read();
//		manageUsers.read();
		
		
//		grp = new GroupController();
//		Group newGroup = new Group();
//		grp.create(newGroup);
//		manageUsers.update("e6d425e2-0c30-4c1e-9645-5c40e0c81e9e", newUser);
//		manageUsers.addFriend("4fb225e7-31ef-488d-9d6f-0c91d122dae0", "3ba8339e-c817-444f-8919-89cde9ea5d7e");
//		System.out.println(manageUsers.searchListFriend("4fb225e7-31ef-488d-9d6f-0c91d122dae0"));
//		manageUsers.searchListFriend("4fb225e7-31ef-488d-9d6f-0c91d122dae0").forEach((ele)->{
//			System.out.println(ele);
//		});
//		
//		messC.findMessageBySender("dd942b10-dbd6-47d3-ac4e-2b1a0fe1fa68","d2c41b73-7da6-40af-86f1-235dfd885bd8")
//				.forEach((ele) -> {
//					System.out.println(ele);
//				});
//		System.out.println(messC.findMessageById("d9fd0730-3800-43c4-9553-da18718ca584").getContent());
		
	}

	// Setting display component content
	public void menuClicked(JPanel panel) {
		PanelManage.setVisible(false);
		PanelLoginHis.setVisible(false);
		PanelGroupChat.setVisible(false);

		panel.setVisible(true);
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

	private void init() {
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
				if (PromptResult == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		setBounds(100, 100, 1350, 519);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaptionBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		// Init component
		PanelManage = new PanelManagement();
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

		JLabel lblManagement = new JLabel("   Quản lý người dùng");
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

		JLabel lblLoginHis = new JLabel("Lịch sử đăng nhập");
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

		JLabel lblGroupChat = new JLabel("  Quản lý nhóm chat");
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
}
