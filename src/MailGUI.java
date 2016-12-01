import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

public class MailGUI {
	static int is_inbox = 1;
	static int is_outbox = 0;
	static int is_editorpane = 0;
	static int is_search = 0;
	static int no_of_pages;
	static int page;
	JFrame frame;
	JPanel panel;
	static Browser browser;
	static BrowserView browserView;
	public static HoverButton[] buttons = new HoverButton[10];
	static SendMail sendMail;
	SendMailGUI window1 = new SendMailGUI();
	static long lastuid;
	static long lastuid_outbox;
	static LinkedList<String> ll = new LinkedList<String>();
	static LinkedList<String> ll_1 = new LinkedList<String>();
	static Database database;
	static MailGUI window;
	static ReadMail readMail;
	static JScrollPane jsp = new JScrollPane();
	static FromSearchTree from_search_tree;
	static FromNameSearchTree from_name_search_tree;
	static ToSearchTree to_search_tree;
	static ToNameSearchTree to_name_search_tree;
	static SubjectSearchTree subject_search_tree;
	static ArrayList<MailTree> resultant_al;
	static Message inboxMail = null;

	/**
	 * Function to show the next page of GUI on clicking next button
	 */
	public static void update_next_GUI() {
		if (is_inbox == 1) {
			lastuid = lastuid - 1;
			ll = database.getKmails("INBOX", lastuid, 10);
			for (int i = 0; i < 10; i++) {
				Font font = new Font("Consolas", Font.PLAIN, 14);
				buttons[i].setFont(font);
				buttons[i].setText(change_from(ll.get(2 + 5 * i).substring(5)
						.split("<")[0])
						+ change_subject(ll.get(1 + 5 * i).substring(8))
						+ ll.get(5 * i).substring(5));
			}
			lastuid = Integer.parseInt(ll.get(49));
		}
		if (is_outbox == 1) {
			lastuid_outbox = lastuid_outbox - 1;
			ll_1 = database.getKmails("outbox", lastuid_outbox, 10);
			for (int i = 0; i < 10; i++) {
				Font font = new Font("Consolas", Font.PLAIN, 14);
				buttons[i].setFont(font);
				buttons[i].setText(change_from(ll_1.get(2 + 5 * i).substring(3)
						.split("<")[0])
						+ change_subject(ll_1.get(1 + 5 * i).substring(8))
						+ ll_1.get(5 * i).substring(5));
			}
			lastuid_outbox = Integer.parseInt(ll_1.get(49));
		}
	}

	/**
	 * Function to show the previous page of GUI on clicking previous button
	 */

	public static void update_previous_GUI() {
		if (is_inbox == 1) {
			lastuid = lastuid + 19;
			ll = database.getKmails("INBOX", lastuid, 10);
			for (int i = 0; i < 10; i++) {
				Font font = new Font("Consolas", Font.PLAIN, 14);
				buttons[i].setFont(font);
				buttons[i].setText(change_from(ll.get(2 + 5 * i).substring(5)
						.split("<")[0])
						+ change_subject(ll.get(1 + 5 * i).substring(8))
						+ ll.get(5 * i).substring(5));
			}
			lastuid = Integer.parseInt(ll.get(49));
		}
		if (is_outbox == 1) {
			lastuid_outbox = lastuid_outbox + 19;
			ll_1 = database.getKmails("outbox", lastuid_outbox, 10);
			for (int i = 0; i < 10; i++) {
				Font font = new Font("Consolas", Font.PLAIN, 14);
				buttons[i].setFont(font);
				buttons[i].setText(change_from(ll_1.get(2 + 5 * i).substring(3)
						.split("<")[0])
						+ change_subject(ll_1.get(1 + 5 * i).substring(8))
						+ ll_1.get(5 * i).substring(5));
			}
			lastuid_outbox = Integer.parseInt(ll_1.get(49));

		}
	}

	/**
	 * Function which limits the length of from address to 75 and add 25 spaces
	 * to the end of string
	 * 
	 */
	public static String change_from(String s) {
		int len = s.length();
		if (len < 25) {
			for (int i = 0; i < 45 - len; i++) {
				s = s + " ";
			}
			return s;
		} else {
			s = s.substring(0, 22) + "...";
			for (int i = 0; i < 20; i++) {
				s = s + " ";
			}
			return s;
		}

	}

	/**
	 * Function which limits the length of subject to 75 and add 25 spaces to
	 * the end of string
	 * 
	 */
	public static String change_subject(String s) {

		int len = s.length();
		if (len < 40) {
			for (int i = 0; i < 60 - len; i++) {
				s = s + " ";
			}
			return s;
		} else {
			s = s.substring(0, 37) + "...";
			for (int i = 0; i < 20; i++) {
				s = s + " ";
			}
			return s;
		}
	}

	public void set_Details() {
		Include.setMyEmail(Start_GUI.Email_ID_text.getText());
		Include.setPassword(Start_GUI.passwordField.getText());
		Include.setUserName(Start_GUI.UserName_text.getText());
		try {
			FileWriter fw = new FileWriter(Include.CURDIR
					+ "/mailDB/AccountDetails.txt");
			fw.write(Start_GUI.Email_ID_text.getText() + "\n");
			fw.write(Start_GUI.passwordField.getText() + "\n");
			fw.write(Start_GUI.UserName_text.getText() + "\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showMailGUI() {

		Include.setIMAPSession();

		Thread smtpThread = new Thread() {
			public void run() {
				Include.setSMTPSession();
				sendMail = new SendMail();
				browser = new Browser();
				browserView = new BrowserView(browser);
			}
		};

		smtpThread.start();

		database = new Database();
		readMail = new ReadMail();
		database.updateDB("inbox");
		database.updateDB("outbox");
		lastuid = database.lastUID;
		ll = database.getKmails("INBOX", lastuid, 10);
		from_search_tree = new FromSearchTree(database);
		from_search_tree.updateSearchTree();
		from_name_search_tree = new FromNameSearchTree(database);
		from_name_search_tree.updateSearchTree();
		to_search_tree = new ToSearchTree(database);
		to_search_tree.updateSearchTree();
		to_name_search_tree = new ToNameSearchTree(database);
		to_name_search_tree.updateSearchTree();
		subject_search_tree = new SubjectSearchTree(database);
		subject_search_tree.updateSearchTree();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					UIManager
							.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

					window = new MailGUI();
					for (int i = 0; i < 10; i++) {
						Font font = new Font("Consolas", Font.PLAIN, 14);
						buttons[i].setFont(font);
						buttons[i]
								.setText(change_from(ll.get(2 + 5 * i)
										.substring(5).split("<")[0])
										+ change_subject(ll.get(1 + 5 * i)
												.substring(8))
										+ ll.get(5 * i).substring(5));
					}
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		lastuid = Integer.parseInt(ll.get(49));
		System.out.println(ll.size());

		database.writeStateChanges();
		from_search_tree.writeStateChanges();
		from_name_search_tree.writeStateChanges();
		to_search_tree.writeStateChanges();
		to_name_search_tree.writeStateChanges();
		subject_search_tree.writeStateChanges();
	}

	/**
	 * Create the application.
	 * 
	 * @throws MessagingException
	 */
	public MailGUI() throws MessagingException {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws MessagingException
	 */
	private void initialize() throws MessagingException {
		frame = new JFrame();

		frame.setBounds(100, 100, 718, 432);

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel logolabel = new JLabel();
		logolabel.setBounds(30, 30, 100, 100);
		logolabel.setVisible(true);
		Image img = null;
		try {
			img = ImageIO.read(new File(System.getProperty("user.dir")
					+ "/imgs/hmail.png"));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		logolabel.setIcon(new ImageIcon(img));

		final Image bgImage;
		try {
			bgImage = ImageIO.read(new File(Include.CURDIR + "/bgImage.jpg"));
			panel = new JPanel() {

				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(bgImage, 0, 0, this);
				}
			};
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		panel.setLayout(null);
		panel.setBounds(10, 10, 1300, 770);
		for (int i = 0; i < 10; i++) {
			final int r = i;
			buttons[i] = new HoverButton("New button");
			final Border raisedBevelBorder = BorderFactory
					.createRaisedBevelBorder();
			final Insets insets = raisedBevelBorder.getBorderInsets(buttons[i]);
			final EmptyBorder emptyBorder = new EmptyBorder(insets);
			buttons[i].setBorder(emptyBorder);
			buttons[i].setBorderPainted(true);
			buttons[i].setBackground(new Color(255, 255, 255));
			buttons[i].setForeground(new Color(0, 0, 0));
			buttons[i].setOpaque(true);
			buttons[i].setContentAreaFilled(false);
			buttons[i].getModel().addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					ButtonModel model = (ButtonModel) e.getSource();
					if (model.isRollover()) {
						buttons[r].setBorder(raisedBevelBorder);
						buttons[r].setBorderPainted(true);
						buttons[r].setForeground(new Color(216, 216, 216));
						buttons[r].setBackground(new Color(50, 50, 50));
						buttons[r].setOpaque(true);
					} else {
						buttons[r].setBorder(emptyBorder);
						buttons[r].setBorderPainted(false);
						buttons[r].setBackground(new Color(255, 255, 255));
						buttons[r].setForeground(new Color(0, 0, 0));
						buttons[r].setOpaque(true);
					}
				}
			});
			buttons[i].setBounds(170, 100 + 60 * i, 1100, 60);
			Color colour = new Color(230, 230, 230);
			buttons[i].setHorizontalAlignment(SwingConstants.LEFT);
			panel.add(buttons[i]);
		}

		for (int i = 0; i < 10; i++) {
			final int r = i;
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (int i = 0; i < 10; i++) {
						buttons[i].setVisible(false);
					}

					String body = "";

					if (is_search == 1) {
						is_editorpane = 1;
						try {
							inboxMail = ReadMail.inbox
									.getMessageByUID(resultant_al.get(
											(page - 1) * 10 + r).getUID());
						} catch (NumberFormatException | MessagingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					} else {
						if (is_inbox == 1) {
							try {
								inboxMail = ReadMail.inbox
										.getMessageByUID(Integer.parseInt(ll
												.get(4 + 5 * r)));
							} catch (NumberFormatException | MessagingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						if (is_outbox == 1) {
							try {
								inboxMail = ReadMail.outbox
										.getMessageByUID(Integer.parseInt(ll_1
												.get(4 + 5 * r)));
							} catch (NumberFormatException | MessagingException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					body = readMail.getHeaders_GUI(inboxMail);
					try {
						body += readMail.getText(inboxMail);

					} catch (MessagingException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					browser.loadHTML(body);
					browserView.setVisible(true);
					browserView.setBounds(170, 100, 1100, 600);
					panel.add(browserView);
					if (readMail.hasAttachment(inboxMail)) {
						int selectedOption = JOptionPane
								.showConfirmDialog(
										null,
										"This Mail has attachment(s) Do you want to download them?",
										"Choose", JOptionPane.YES_NO_OPTION);
						if (selectedOption == JOptionPane.YES_OPTION) {
							Thread downloadThread = new Thread() {
								// @Override
								public void run() {
									readMail.downloadAttachments(inboxMail);
									JOptionPane.showMessageDialog(null,
											"Download Complete!!!");
								}
							};
							downloadThread.start();
						}
					}

				}
			});
		}

		JButton compose = new JButton("Compose");
		compose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							window1.initialize();
							window1.frame.setVisible(true);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
			}
		});
		compose.setBounds(10, 160, 122, 60);
		panel.add(compose);

		JButton search = new JButton("Search");

		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFrame subframe = new JFrame();
				subframe.setBounds(100, 100, 800, 500);
				JPanel jpanel = new JPanel();
				jpanel.setLayout(null);

				JLabel jlabel_1 = new JLabel("From Search");
				jlabel_1.setBounds(50, 30, 500, 50);
				final JTextField jt1 = new JTextField();
				jt1.setBounds(50, 80, 500, 50);
				jt1.setVisible(true);
				JButton jbutton_1 = new JButton("Search");
				jbutton_1.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						subframe.dispose();
						is_search = 1;
						for (int i = 0; i < 10; i++) {
							buttons[i].setVisible(true);
						}
						ArrayList<FromMail> from_search_al = new ArrayList<FromMail>();
						from_search_al = from_search_tree.search(jt1.getText());

						ArrayList<FromNameMail> from_name_search_al = new ArrayList<FromNameMail>();
						from_name_search_al = from_name_search_tree.search(jt1
								.getText());

						resultant_al = new ArrayList<MailTree>();
						resultant_al.addAll(from_search_al);
						resultant_al.addAll(from_name_search_al);

						Collections.sort(resultant_al,
								new Comparator<MailTree>() {

									@Override
									public int compare(MailTree o1, MailTree o2) {
										return o1.getUID().compareTo(
												o2.getUID());
									}

								});

						Iterator<MailTree> iterator = resultant_al.iterator();
						MailTree prevMail = null, mail;
						while (iterator.hasNext()) {
							mail = iterator.next();
							if (prevMail != null
									&& prevMail.getUID().equals(mail.getUID())) {
								iterator.remove();
							} else {
								prevMail = mail;
							}
						}

						no_of_pages = 1 + (resultant_al.size()) / 10;
						page = 1;

						if (page == no_of_pages) {
							int temp = resultant_al.size() - (page - 1) * 10;
							for (int i = 0; i < temp; i++) {
								Font font = new Font("Consolas", Font.PLAIN, 14);
								buttons[i].setFont(font);
								buttons[i].setText(change_from(resultant_al
										.get((page - 1) * 10 + i)
										.getFromAddress())
										+ change_subject(resultant_al.get(
												(page - 1) * 10 + i)
												.getSubject())
										+ resultant_al.get((page - 1) * 10 + i)
												.getDate());

							}
							for (int i = temp; i < 10; i++) {
								buttons[i].setText("");
							}
						} else {
							for (int i = 0; i < 10; i++) {
								Font font = new Font("Consolas", Font.PLAIN, 14);
								buttons[i].setFont(font);
								buttons[i].setText(change_from(resultant_al
										.get((page - 1) * 10 + i)
										.getFromAddress())
										+ change_subject(resultant_al.get(
												(page - 1) * 10 + i)
												.getSubject())
										+ resultant_al.get((page - 1) * 10 + i)
												.getDate());
							}
						}

					}
				});
				jbutton_1.setBounds(550, 80, 100, 50);
				jbutton_1.setVisible(true);
				jlabel_1.setVisible(true);

				JLabel jlabel_2 = new JLabel("To Search");
				jlabel_2.setBounds(50, 130, 500, 50);
				final JTextField jt2 = new JTextField();
				jt2.setBounds(50, 180, 500, 50);
				jt2.setVisible(true);
				JButton jbutton_2 = new JButton("Search");
				jbutton_2.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						subframe.dispose();
						is_search = 1;
						for (int i = 0; i < 10; i++) {
							buttons[i].setVisible(true);
						}
						ArrayList<ToMail> to_search_al = new ArrayList<ToMail>();
						to_search_al = to_search_tree.search(jt2.getText());

						ArrayList<ToNameMail> to_name_search_al = new ArrayList<ToNameMail>();
						to_name_search_al = to_name_search_tree.search(jt2
								.getText());

						resultant_al = new ArrayList<MailTree>();
						resultant_al.addAll(to_search_al);
						resultant_al.addAll(to_name_search_al);

						Collections.sort(resultant_al,
								new Comparator<MailTree>() {

									@Override
									public int compare(MailTree o1, MailTree o2) {
										// TODO Auto-generated method stub
										return o1.getUID().compareTo(
												o2.getUID());
									}

								});
						Iterator<MailTree> iterator = resultant_al.iterator();
						MailTree prevMail = null, mail;
						while (iterator.hasNext()) {
							mail = iterator.next();
							if (prevMail != null
									&& prevMail.getUID().equals(mail.getUID())) {
								iterator.remove();
							} else {
								prevMail = mail;
							}
						}

						no_of_pages = 1 + (resultant_al.size()) / 10;
						page = 1;

						if (page == no_of_pages) {
							int temp = resultant_al.size() - (page - 1) * 10;
							for (int i = 0; i < temp; i++) {
								Font font = new Font("Consolas", Font.PLAIN, 14);
								buttons[i].setFont(font);
								buttons[i].setText(change_from(resultant_al
										.get((page - 1) * 10 + i)
										.getFromAddress())
										+ change_subject(resultant_al.get(
												(page - 1) * 10 + i)
												.getSubject())
										+ resultant_al.get((page - 1) * 10 + i)
												.getDate());

							}
							for (int i = temp; i < 10; i++) {
								buttons[i].setText("");
							}
						} else {
							for (int i = 0; i < 10; i++) {
								Font font = new Font("Consolas", Font.PLAIN, 14);
								buttons[i].setFont(font);
								buttons[i].setText(change_from(resultant_al
										.get((page - 1) * 10 + i)
										.getFromAddress())
										+ change_subject(resultant_al.get(
												(page - 1) * 10 + i)
												.getSubject())
										+ resultant_al.get((page - 1) * 10 + i)
												.getDate());
							}
						}

					}
				});
				jbutton_2.setBounds(550, 180, 100, 50);
				jbutton_2.setVisible(true);
				jlabel_2.setVisible(true);

				JLabel jlabel_3 = new JLabel("Subject Search");
				jlabel_3.setBounds(50, 230, 500, 50);
				final JTextField jt3 = new JTextField();
				jt3.setBounds(50, 280, 500, 50);
				jt3.setVisible(true);
				JButton jbutton_3 = new JButton("Search");
				jbutton_3.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						subframe.dispose();
						is_search = 1;
						for (int i = 0; i < 10; i++) {
							buttons[i].setVisible(true);
						}
						ArrayList<SubjectMail> subject_search_al = new ArrayList<SubjectMail>();
						subject_search_al = subject_search_tree.search(jt3
								.getText());

						resultant_al = new ArrayList<MailTree>();
						resultant_al.addAll(subject_search_al);

						no_of_pages = 1 + (resultant_al.size()) / 10;
						page = 1;

						if (page == no_of_pages) {
							int temp = resultant_al.size() - (page - 1) * 10;
							for (int i = 0; i < temp; i++) {
								Font font = new Font("Consolas", Font.PLAIN, 14);
								buttons[i].setFont(font);
								buttons[i].setText(change_from(resultant_al
										.get((page - 1) * 10 + i)
										.getFromAddress())
										+ change_subject(resultant_al.get(
												(page - 1) * 10 + i)
												.getSubject())
										+ resultant_al.get((page - 1) * 10 + i)
												.getDate());

							}
							for (int i = temp; i < 10; i++) {
								buttons[i].setText("");
							}
						} else {
							for (int i = 0; i < 10; i++) {
								Font font = new Font("Consolas", Font.PLAIN, 14);
								buttons[i].setFont(font);
								buttons[i].setText(change_from(resultant_al
										.get((page - 1) * 10 + i)
										.getFromAddress())
										+ change_subject(resultant_al.get(
												(page - 1) * 10 + i)
												.getSubject())
										+ resultant_al.get((page - 1) * 10 + i)
												.getDate());
							}
						}

					}
				});
				jbutton_3.setBounds(550, 280, 100, 50);
				jbutton_3.setVisible(true);
				jlabel_3.setVisible(true);

				jpanel.add(jt1);
				jpanel.add(jbutton_1);
				jpanel.add(jlabel_1);

				jpanel.add(jt2);
				jpanel.add(jbutton_2);
				jpanel.add(jlabel_2);

				jpanel.add(jt3);
				jpanel.add(jbutton_3);
				jpanel.add(jlabel_3);

				jpanel.setVisible(true);
				subframe.add(jpanel);
				subframe.setVisible(true);

			}
		});
		search.setBounds(600, 11, 150, 42);
		panel.add(search);

		JButton refresh = new JButton("Refresh");
		refresh.setBounds(170, 11, 150, 42);
		refresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (is_inbox == 1) {
					long uid = Integer.parseInt(ll.get(4));
					database.refreshPage("INBOX", uid, 10);
					uid = database.latestUID;
					ll = database.getKmails("INBOX", uid, 10);
					for (int i = 0; i < 10; i++) {
						Font font = new Font("Consolas", Font.PLAIN, 14);
						buttons[i].setFont(font);
						buttons[i]
								.setText(change_from(ll.get(2 + 5 * i)
										.substring(5).split("<")[0])
										+ change_subject(ll.get(1 + 5 * i)
												.substring(8))
										+ ll.get(5 * i).substring(5));
					}
					lastuid = Integer.parseInt(ll.get(49));
				}
				if (is_outbox == 1) {
					long uid = Integer.parseInt(ll_1.get(4));
					// System.out.println("uid: " + uid);
					database.refreshPage("outbox", uid, 10);
					uid = database.latestOutboxUID;
					ll_1 = database.getKmails("outbox", uid, 10);
					for (int i = 0; i < 10; i++) {
						Font font = new Font("Consolas", Font.PLAIN, 14);
						buttons[i].setFont(font);
						buttons[i].setText(change_from(ll_1.get(2 + 5 * i)
								.substring(3).split("<")[0])
								+ change_subject(ll_1.get(1 + 5 * i).substring(
										8)) + ll_1.get(5 * i).substring(5));
					}
					lastuid = Integer.parseInt(ll_1.get(49));
				}
			}
		});
		refresh.setVisible(true);
		panel.add(refresh);

		JButton back = new JButton("Back");

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (is_editorpane == 1) {
					is_editorpane = 0;
					for (int i = 0; i < 10; i++) {
						buttons[i].setVisible(true);
					}
					is_search = 1;
					if (page == no_of_pages) {
						int temp = resultant_al.size() - (page - 1) * 10;
						for (int i = 0; i < temp; i++) {
							Font font = new Font("Consolas", Font.PLAIN, 14);
							buttons[i].setFont(font);
							buttons[i].setText(change_from(resultant_al.get(
									(page - 1) * 10 + i).getFromAddress())
									+ change_subject(resultant_al.get(
											(page - 1) * 10 + i).getSubject())
									+ resultant_al.get((page - 1) * 10 + i)
											.getDate());

						}
						for (int i = temp; i < 10; i++) {
							buttons[i].setText("");
						}
					} else {
						for (int i = 0; i < 10; i++) {
							Font font = new Font("Consolas", Font.PLAIN, 14);
							buttons[i].setFont(font);
							buttons[i].setText(change_from(resultant_al.get(
									(page - 1) * 10 + i).getFromAddress())
									+ change_subject(resultant_al.get(
											(page - 1) * 10 + i).getSubject())
									+ resultant_al.get((page - 1) * 10 + i)
											.getDate());
						}
					}
				} else if (is_search == 1) {
					if (page > 1) {
						page = page - 1;
					}
					if (page == no_of_pages) {
						int temp = resultant_al.size() - (page - 1) * 10;
						for (int i = 0; i < temp; i++) {
							Font font = new Font("Consolas", Font.PLAIN, 14);
							buttons[i].setFont(font);
							buttons[i].setText(change_from(resultant_al.get(
									(page - 1) * 10 + i).getFromAddress())
									+ change_subject(resultant_al.get(
											(page - 1) * 10 + i).getSubject())
									+ resultant_al.get((page - 1) * 10 + i)
											.getDate());

						}
						for (int i = temp; i < 10; i++) {
							buttons[i].setText("");
						}
					} else {
						for (int i = 0; i < 10; i++) {
							Font font = new Font("Consolas", Font.PLAIN, 14);
							buttons[i].setFont(font);
							buttons[i].setText(change_from(resultant_al.get(
									(page - 1) * 10 + i).getFromAddress())
									+ change_subject(resultant_al.get(
											(page - 1) * 10 + i).getSubject())
									+ resultant_al.get((page - 1) * 10 + i)
											.getDate());
						}
					}

				} else {
					update_previous_GUI();
				}
			}
		});
		back.setBounds(1000, 11, 100, 42);
		panel.add(back);

		JButton forward = new JButton("Next");

		forward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (is_search == 1) {
					if (page < no_of_pages) {
						page = page + 1;
					}
					if (page == no_of_pages) {
						int temp = resultant_al.size() - (page - 1) * 10;
						for (int i = 0; i < temp; i++) {
							Font font = new Font("Consolas", Font.PLAIN, 14);
							buttons[i].setFont(font);
							buttons[i].setText(change_from(resultant_al.get(
									(page - 1) * 10 + i).getFromAddress())
									+ change_subject(resultant_al.get(
											(page - 1) * 10 + i).getSubject())
									+ resultant_al.get((page - 1) * 10 + i)
											.getDate());

						}
						for (int i = temp; i < 10; i++) {
							buttons[i].setText("");
						}
					} else {
						for (int i = 0; i < 10; i++) {
							Font font = new Font("Consolas", Font.PLAIN, 14);
							buttons[i].setFont(font);
							buttons[i].setText(change_from(resultant_al.get(
									(page - 1) * 10 + i).getFromAddress())
									+ change_subject(resultant_al.get(
											(page - 1) * 10 + i).getSubject())
									+ resultant_al.get((page - 1) * 10 + i)
											.getDate());
						}
					}

				} else {
					update_next_GUI();
				}
			}
		});
		forward.setBounds(1100, 11, 100, 42);
		panel.add(forward);

		JButton inbox = new JButton("Inbox");
		inbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				is_outbox = 0;
				is_inbox = 1;
				is_search = 0;
				is_editorpane = 0;
				for (int i = 0; i < 10; i++) {
					buttons[i].setVisible(true);
				}
				for (int i = 0; i < 10; i++) {
					buttons[i].setText(change_from(ll.get(2 + 5 * i)
							.substring(5).split("<")[0])
							+ change_subject(ll.get(1 + 5 * i).substring(8))
							+ ll.get(5 * i).substring(5));
				}
				lastuid = Integer.parseInt(ll.get(49));
				jsp.setVisible(true );
				database.writeStateChanges();
			}
		});
		inbox.setBounds(10, 220, 122, 60);
		panel.add(inbox);

		JButton outbox = new JButton("Outbox");
		outbox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				is_outbox = 1;
				is_inbox = 0;
				is_search = 0;
				is_editorpane = 0;
				lastuid_outbox = database.latestOutboxUID;
				ll_1 = database.getKmails("outbox", lastuid_outbox, 10);
				for (int i = 0; i < 10; i++) {
					buttons[i].setVisible(true);
				}
				for (int i = 0; i < 10; i++) {
					Font font = new Font("Consolas", Font.PLAIN, 14);
					buttons[i].setFont(font);
					buttons[i].setText(change_from(ll_1.get(2 + 5 * i)
							.substring(3).split("<")[0])
							+ change_subject(ll_1.get(1 + 5 * i).substring(8))
							+ ll_1.get(5 * i).substring(5));
				}
				lastuid_outbox = Integer.parseInt(ll_1.get(49));

			}
		});
		outbox.setBounds(10, 280, 122, 60);
		panel.add(outbox);
		panel.add(logolabel);
		frame.getContentPane().add(panel);
	}
}
