package urban_robot_controller.logging;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;



// TODO: Auto-generated Javadoc
/**
 * The Class SelectionMode.
 */
public class SelectionMode extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6113734042810247339L;

	/** The dialog. */
	JDialog dialog = new JDialog();

	/** The mode. */
	private String mode;
	
	/**
	 * Instantiates a new selection mode.
	 */
	public SelectionMode() {


		dialog.setSize(155, 150);
		dialog.setLayout(null);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		int x = (screenSize.width - dialog.getWidth()) / 2;
		int y = (screenSize.height - dialog.getHeight()) / 2;
		dialog.setLocation(x, y);

		JButton button1 = new JButton("online mode");
		JButton button2 = new JButton("offline mode");

		button1.setBounds(10,10,120,40);
		dialog.add(button1);

		button2.setBounds(10,60,120,40);
		dialog.add(button2);

		//Make dialog visible
		dialog.setVisible(true);

		//		JButton onlineModeButton = new JButton("Online mode");
		//		meinJDialog.add(onlineModeButton);
		button2.addActionListener( new ActionListener() {
			@Override 
			public void actionPerformed( ActionEvent e ) {
				dialog.dispose();
				mode = "offline";
			}
		} );
		
		button1.addActionListener( new ActionListener() {
			@Override 
			public void actionPerformed( ActionEvent e ) {
				dialog.dispose();
				mode = "online";
			}
		} );
	}
	
	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}


	/**
	 * Horizontal separator.
	 *
	 * @return the j component
	 */
	static JComponent horizontalSeparator() {
		JSeparator x = new JSeparator(SwingConstants.HORIZONTAL);
		x.setPreferredSize(new Dimension(100,3));
		return x;
	}


	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
