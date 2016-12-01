import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class MailDriver {
	public static void main(String[] args) {

		try {
			Scanner sc = new Scanner(new File(Include.CURDIR
					+ "/mailDB/AccountDetails.txt"));
			if (sc.hasNext() == false) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Start_GUI window = new Start_GUI();
							window.frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				sc.close();
			} else {
				Scanner br = new Scanner(new File(Include.CURDIR
						+ "/mailDB/AccountDetails.txt"));
				Include.setMyEmail(br.nextLine());
				Include.setPassword(br.nextLine());
				Include.setUserName(br.nextLine());
				MailGUI mg;
				try {
					mg = new MailGUI();
					mg.showMailGUI();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Please check your internet connection");
					e.printStackTrace();
				}
				br.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
