import javax.swing.JButton;

public class HoverButton extends JButton {

	float alpha = 0.5f;

	public HoverButton(String text) {
		super(text);
		setFocusPainted(false);
		setBorderPainted(false);

		// setFocusPainted(false);
		// setBorderPainted(true);
		setContentAreaFilled(false);
		// setFont(getFont().deriveFont(Font.BOLD));
		// addMouseListener(new ML());
		// setContentAreaFilled(true);
		// setBorderPainted(false);

		setOpaque(true);

		// setFont(getFont().deriveFont(Font.BOLD));
		// addMouseListener(new ML());
	}

}