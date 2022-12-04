package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.Color;

public class CreateGroup extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField groupNameTextField;

	/**
	 * Create the frame.
	 */
	public CreateGroup() {
		setBounds(100, 100, 355, 444);
		setTitle("Tạo nhóm");
		setResizable(false);
		setVisible(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblToNhm = new JLabel("Tạo nhóm", SwingConstants.CENTER);
		lblToNhm.setFont(new Font("Arial", Font.BOLD, 20));
		lblToNhm.setBounds(70, 13, 204, 24);
		contentPane.add(lblToNhm);
		
		JLabel lblTnNhm = new JLabel("Tên nhóm");
		lblTnNhm.setFont(new Font("Arial", Font.BOLD, 12));
		lblTnNhm.setBounds(10, 47, 67, 24);
		contentPane.add(lblTnNhm);
		
		groupNameTextField = new JTextField();
		groupNameTextField.setFont(new Font("Arial", Font.PLAIN, 12));
		groupNameTextField.setBounds(78, 50, 255, 19);
		contentPane.add(groupNameTextField);
		groupNameTextField.setColumns(10);
		
		JScrollPane friendScroll = new JScrollPane((Component) null);
		friendScroll.setBorder(new EmptyBorder(10, 0, 0, 0));
		friendScroll.setBounds(10, 114, 150, 238);
		contentPane.add(friendScroll);
		
		JList<String> friendList = new JList<String>();
		friendList.setVisibleRowCount(10);
		friendScroll.setViewportView(friendList);
		
		JScrollPane groupMemberScroll = new JScrollPane((Component) null);
		groupMemberScroll.setBorder(new EmptyBorder(10, 0, 0, 0));
		groupMemberScroll.setBounds(183, 114, 150, 238);
		contentPane.add(groupMemberScroll);
		
		JList<String> groupMemberList = new JList<String>();
		groupMemberList.setVisibleRowCount(10);
		groupMemberScroll.setRowHeaderView(groupMemberList);
		
		JLabel lblDanhSchBn = new JLabel("Danh sách bạn bè");
		lblDanhSchBn.setFont(new Font("Arial", Font.BOLD, 12));
		lblDanhSchBn.setBounds(10, 97, 204, 24);
		contentPane.add(lblDanhSchBn);
		
		JLabel lblDanhSchThnh = new JLabel("Thành viên nhóm");
		lblDanhSchThnh.setFont(new Font("Arial", Font.BOLD, 12));
		lblDanhSchThnh.setBounds(183, 97, 204, 24);
		contentPane.add(lblDanhSchThnh);
		
		JButton createGroupBtn = new JButton("Tạo nhóm");
		createGroupBtn.setBounds(10, 362, 323, 35);
		contentPane.add(createGroupBtn);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.LIGHT_GRAY);
		separator.setBounds(10, 85, 323, 2);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setForeground(Color.LIGHT_GRAY);
		separator_1.setBounds(170, 124, 3, 228);
		contentPane.add(separator_1);
	}
}
