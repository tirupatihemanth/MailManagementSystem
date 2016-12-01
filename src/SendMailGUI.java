import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SendMailGUI {

	public JFrame frame;
	public JTextField to;
	public JTextField subject;
	public JTextField attachment;
	public JTextArea message;

	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 718, 432);
		frame.getContentPane().setLayout(null);

		JLabel tolbl = new JLabel("To");
		tolbl.setBounds(20, 26, 46, 14);
		frame.getContentPane().add(tolbl);

		to = new JTextField();
		to.setBounds(123, 11, 415, 32);
		frame.getContentPane().add(to);
		to.setColumns(10);

		JLabel subjectlbl = new JLabel("Subject");
		subjectlbl.setBounds(10, 67, 46, 14);
		frame.getContentPane().add(subjectlbl);

		subject = new JTextField();
		subject.setBounds(123, 54, 415, 30);
		frame.getContentPane().add(subject);
		subject.setColumns(10);

		JLabel attachmentlbl = new JLabel("Attachment");
		attachmentlbl.setBounds(10, 110, 75, 14);
		frame.getContentPane().add(attachmentlbl);

		attachment = new JTextField();
		attachment.setBounds(123, 95, 415, 32);
		attachment.setEditable(false);
		frame.getContentPane().add(attachment);
		attachment.setColumns(10);

		message = new JTextArea();
		message.setBounds(10, 135, 682, 247);
		frame.getContentPane().add(message);

		JButton browse = new JButton("Browse");
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogType(JFileChooser.OPEN_DIALOG);
				jfc.showOpenDialog(null);
				File f = jfc.getSelectedFile();
				attachment.setText(f.getAbsolutePath());

			}
		});
		browse.setBounds(561, 92, 131, 37);
		frame.getContentPane().add(browse);

		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String[] strarr = to.getText().split(";");
				MailGUI.sendMail.setSubject(subject.getText());
				if ((message.getText().toLowerCase().contains("find attached") || message
						.getText().contains("pfa"))
						&& attachment.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"It seems you forgot send a attachment");
				} else {
					MailGUI.sendMail.addTextContent(message.getText());
					if (!attachment.getText().isEmpty()) {
						MailGUI.sendMail.attachFile(attachment.getText());
					}
					if (to.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null,
								"Please enter Recipient's Address");
					} else {
						SendMailGUI.this.frame.dispose();
						Thread t = new Thread() {
							public void run() {
								Boolean b = true;
								for (int i = 0; i < strarr.length; i++) {
									b = b
											&& MailGUI.sendMail
													.sendMail(strarr[i]);
								}

								if (b) {
									JOptionPane.showMessageDialog(null,
											"Email sent");
								} else {
									JOptionPane.showMessageDialog(null,
											"An error occured");
								}
							}
						};
						t.start();
					}
				}
			}
		});

		send.setBounds(561, 26, 131, 55);
		frame.getContentPane().add(send);
	}
}
