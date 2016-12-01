import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Start_GUI {
	static Scanner inputPage;
	public JFrame frame;
	public static JTextField Email_ID_text;
	public static JPasswordField passwordField;
	public static JTextField UserName_text;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public Start_GUI() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 627, 418);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel Email_ID_label = new JLabel("Please enter your Email ID");
		Email_ID_label.setBounds(30, 11, 209, 28);
		frame.getContentPane().add(Email_ID_label);

		Email_ID_text = new JTextField();
		Email_ID_text.setBounds(30, 50, 260, 28);
		frame.getContentPane().add(Email_ID_text);
		Email_ID_text.setColumns(10);

		JLabel lblNewLabel = new JLabel("Please enter your password");
		lblNewLabel.setBounds(30, 104, 209, 28);
		frame.getContentPane().add(lblNewLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(30, 143, 260, 28);
		frame.getContentPane().add(passwordField);

		JLabel lblNewLabel_1 = new JLabel("Please enter your User Name");
		lblNewLabel_1.setBounds(30, 199, 209, 21);
		frame.getContentPane().add(lblNewLabel_1);

		UserName_text = new JTextField();
		UserName_text.setBounds(30, 242, 260, 28);
		frame.getContentPane().add(UserName_text);
		UserName_text.setColumns(10);

		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					frame.dispose();
					MailGUI mg = new MailGUI();
					mg.set_Details();
					mg.showMailGUI();
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(277, 318, 126, 36);
		frame.getContentPane().add(btnNewButton);
	}
}
