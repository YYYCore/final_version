package urban_robot_controller.ui;

import java.awt.Font;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CrossroadUi implements Runnable {

	private JFrame frame;
	private Font font;
	private JLabel redB;
	private JLabel greenB;
	private JLabel redA;
	private JLabel greenA;
	private JTextField b1;
	private JTextField b2;
	private JTextField a2;
	private JTextField a1;

	public CrossroadUi() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setLocation(0, 360);
		font = new Font("SansSerif", Font.BOLD, 30);
	}

	@Override
	public void run() {
		frame.setSize(10, 10);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLayout(null);

		initCrossRoadIMG();	
		initAGreen();
		initARed();
		initBGreen();
		initBRed();
		
		initA1();
		initA2();
		initB2();
		initB1();
	}

	private void initBRed() {
		try {
			BufferedImage myPicture = ImageIO.read(new File("red.png"));
			redB = new JLabel(new ImageIcon(myPicture));
			frame.add(redB);

			Insets insets = frame.getInsets();
			redB.setBounds(insets.left+777, insets.top+57, 80,
					80);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void initBGreen() {
		try {
			BufferedImage myPicture = ImageIO.read(new File("green.png"));
			greenB = new JLabel(new ImageIcon(myPicture));
			frame.add(greenB);

			Insets insets = frame.getInsets();
			greenB.setBounds(insets.left+777, insets.top+57, 80,
					80);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initARed() {
		try {
			BufferedImage myPicture = ImageIO.read(new File("red.png"));
			redA = new JLabel(new ImageIcon(myPicture));
			frame.add(redA);

			Insets insets = frame.getInsets();
			redA.setBounds(insets.left+577, insets.top+57, 80,
					80);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initAGreen() {
		try {
			BufferedImage myPicture = ImageIO.read(new File("green.png"));
			greenA = new JLabel(new ImageIcon(myPicture));
			frame.add(greenA);

			Insets insets = frame.getInsets();
			greenA.setBounds(insets.left+577, insets.top+57, 80,
					80);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initCrossRoadIMG() {
		try {
			BufferedImage myPicture = ImageIO.read(new File("crossRoads.png"));
			frame.setSize(1400, 600);
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			frame.add(picLabel);

			Insets insets = frame.getInsets();
			picLabel.setBounds(insets.left, insets.top, 1400,
					600);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initB1() {
		b1 = new JTextField("E6E7");
		b1.setEditable(false);
		b1.setFont(font);
		frame.add(b1);
		Insets insets = frame.getInsets();
		b1.setBounds(insets.left + 837, insets.top + 350, 80, 50);
		b1.setVisible(false);
	}

	private void initB2() {
		b2 = new JTextField("E6E7");
		b2.setEditable(false);
		b2.setFont(font);
		frame.add(b2);
		Insets insets = frame.getInsets();
		b2.setBounds(insets.left + 1070, insets.top + 135, 80, 50);		
		b2.setVisible(false);
	}

	private void initA2() {
		a2 = new JTextField("E6E7");
		a2.setEditable(false);
		a2.setFont(font);
		frame.add(a2);
		Insets insets = frame.getInsets();
		a2.setBounds(insets.left + 310, insets.top + 135, 80, 50);
		a2.setVisible(false);
	}

	private void initA1() {
		a1 = new JTextField("E6E7");
		a1.setEditable(false);
		a1.setFont(font);
		frame.add(a1);
		Insets insets = frame.getInsets();
		a1.setBounds(insets.left + 518, insets.top + 350, 80, 50);
		a1.setVisible(false);
	}
	
	public void updateA1(String msg, boolean visible) {
		a1.setText(msg);
		a1.setVisible(visible);
	}
	
	public void updateA2(String msg, boolean visible) {
		a2.setText(msg);
		a2.setVisible(visible);
	}
	
	public void updateB1(String msg, boolean visible) {
		b1.setText(msg);
		b1.setVisible(visible);
	}
	
	public void updateB2(String msg, boolean visible) {
		b2.setText(msg);
		b2.setVisible(visible);
	}
	
	public void lockA() {
		greenA.setVisible(false);
		redA.setVisible(true);
	}
	
	public void unlockA() {
		greenA.setVisible(true);
		redA.setVisible(false);
	}
	
	public void lockB() {
		greenB.setVisible(false);
		redB.setVisible(true);
	}
	
	public void unlockB() {
		greenB.setVisible(true);
		redB.setVisible(false);
	}
	
}
