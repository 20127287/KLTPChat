package Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GroupManagement extends JFrame {

	private JPanel contentPane;
	private JTextField inviteTextField;
	private JScrollPane memberScrollPane;
	private JTable memberTable;
	private JButton btniTnNhm;
	private DefaultTableModel memberListTableModel;

	/**
	 * Create the frame.
	 */
	public GroupManagement(String groupName, String[] memberList) {
		addComponents(groupName, memberList);
		setResizable(false);
		setVisible(true);
		setTitle("Quản lý nhóm - " + groupName);

	}

	public void addComponents(String groupName, String[] memberList) {
		// Content Pane
		setBounds(100, 100, 539, 477);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Member Table
		memberListTableModel = new DefaultTableModel();
		memberListTableModel.addColumn("Tên tài khoản");
		memberListTableModel.addColumn("Vai trò");
		memberListTableModel.addColumn("Xóa khỏi nhóm");

		for (int i = 1; i < memberList.length; i++) {
			String[] str = memberList[i].split(":");
			Object[] rowObjects = { str[0], str[1], "Xóa khỏi nhóm" };
			memberListTableModel.addRow(rowObjects);
			memberTable = new JTable(memberListTableModel);
		}

		if (memberList.length > 1) {
			memberTable.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());
			memberTable.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor(new JTextField()));
			memberTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
			memberTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JTextField()));

			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(JLabel.CENTER);
			memberTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		}

		JButton inviteBtn = new JButton("Mời vào nhóm");
		inviteBtn.setBounds(178, 10, 133, 24);
		contentPane.add(inviteBtn);

		inviteTextField = new JTextField();
		inviteTextField.setColumns(10);
		inviteTextField.setBounds(10, 12, 158, 21);
		contentPane.add(inviteTextField);

		JButton leftBtn = new JButton("🚪");
		leftBtn.setBounds(464, 10, 52, 24);
		contentPane.add(leftBtn);

		memberScrollPane = new JScrollPane();
		memberScrollPane.setBounds(10, 52, 506, 378);
		memberScrollPane.setViewportView(memberTable);
		contentPane.add(memberScrollPane);

		btniTnNhm = new JButton("Đổi tên nhóm");
		btniTnNhm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField newName = new JTextField();
				Object[] object = { "Nhập tên mới:", newName };

				int option = JOptionPane.showConfirmDialog(null, object, "Đổi mật khẩu", JOptionPane.OK_CANCEL_OPTION);

				if (option == JOptionPane.OK_OPTION && !newName.getText().isBlank()) {
					Main.sendMessage("Command_ChangeGroupName`" + groupName + "`" + newName.getText());
				}
			}
		});
		btniTnNhm.setBounds(321, 10, 133, 24);
		contentPane.add(btniTnNhm);
	}
}
