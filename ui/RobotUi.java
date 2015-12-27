package urban_robot_controller.ui;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class RobotUi implements Runnable {
	private static int numberOfInstances = 0;
	
	private JFrame frame;
	private Font font;
	private JTextField giveWayMsg;
	private JTextField giveWayDistanz;
	private JTextField takeWayMsg;
	private JTextField takeWayDistanz;
	private JTextField stopMsg;
	private JTextField stopDistanz;
	private JTextField stateMsg;

	public RobotUi(String caption, boolean visible) {
		frame = new JFrame(caption);
		frame.setVisible(visible);
		frame.setLocation(numberOfInstances*660 , 0);
		numberOfInstances++;
	}

	@Override
	public void run() {
		frame.setSize(640, 360);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(null);
		font = new Font("SansSerif", Font.PLAIN, 30);
		initStopLine();
		initTakeWayLine();
		initGiveWayLine();
		initRobotStateLine();
	}

	private void initRobotStateLine() {
		JTextField robotStateTxt = new JTextField("Zustand");
		robotStateTxt.setEditable(false);
		robotStateTxt.setFont(font);

		stateMsg = new JTextField("Linefollower running");
		stateMsg.setFont(font);

		frame.add(robotStateTxt);
		frame.add(stateMsg);

		Insets insets = frame.getInsets();

		robotStateTxt.setBounds(insets.left + 40, insets.top + 195, 200, 50);
		stateMsg.setBounds(insets.left + 240, insets.top + 195, 310, 50);
	}

	private void initGiveWayLine() {
		JTextField giveWayTxt = new JTextField("Vorfahrt gew. ");
		giveWayTxt.setEditable(false);
		giveWayTxt.setFont(font);

		giveWayMsg = new JTextField(" - ");
		giveWayMsg.setFont(font);

		giveWayDistanz = new JTextField("  - ");
		giveWayDistanz.setFont(font);

		JTextField stoppEinheit = new JTextField("[cm]");
		stoppEinheit.setEditable(false);
		stoppEinheit.setFont(font);

		frame.add(giveWayTxt);
		frame.add(giveWayMsg);
		frame.add(giveWayDistanz);
		frame.add(stoppEinheit);

		Insets insets = frame.getInsets();

		giveWayTxt.setBounds(insets.left + 40, insets.top + 130, 200, 50);
		giveWayMsg.setBounds(insets.left + 240, insets.top + 130, 200, 50);
		giveWayDistanz.setBounds(insets.left + 440, insets.top + 130, 50, 50);
		stoppEinheit.setBounds(insets.left + 490, insets.top + 130, 60, 50);
	}

	private void initTakeWayLine() {
		JTextField stoppTxt = new JTextField("Vorfahrt ");
		stoppTxt.setEditable(false);
		stoppTxt.setFont(font);

		takeWayMsg = new JTextField(" - ");
		takeWayMsg.setFont(font);

		takeWayDistanz = new JTextField("  - ");
		takeWayDistanz.setFont(font);

		JTextField stoppEinheit = new JTextField("[cm]");
		stoppEinheit.setEditable(false);
		stoppEinheit.setFont(font);

		frame.add(stoppTxt);
		frame.add(takeWayMsg);
		frame.add(takeWayDistanz);
		frame.add(stoppEinheit);

		Insets insets = frame.getInsets();

		stoppTxt.setBounds(insets.left + 40, insets.top + 65, 200, 50);
		takeWayMsg.setBounds(insets.left + 240, insets.top + 65, 200, 50);
		takeWayDistanz.setBounds(insets.left + 440, insets.top + 65, 50, 50);
		stoppEinheit.setBounds(insets.left + 490, insets.top + 65, 60, 50);

	}

	private void initStopLine() {
		JTextField stoppTxt = new JTextField("Stopp ");
		stoppTxt.setEditable(false);
		stoppTxt.setFont(font);

		stopMsg = new JTextField(" - ");
		stopMsg.setFont(font);

		stopDistanz = new JTextField("  - ");
		stopDistanz.setFont(font);

		JTextField stoppEinheit = new JTextField("[cm]");
		stoppEinheit.setEditable(false);
		stoppEinheit.setFont(font);

		frame.add(stoppTxt);
		frame.add(stopMsg);
		frame.add(stopDistanz);
		frame.add(stoppEinheit);

		Insets insets = frame.getInsets();

		stoppTxt.setBounds(insets.left + 40, insets.top, 200, 50);
		stopMsg.setBounds(insets.left + 240, insets.top, 200, 50);
		stopDistanz.setBounds(insets.left + 440, insets.top, 50, 50);
		stoppEinheit.setBounds(insets.left + 490, insets.top, 60, 50);
	}

	public void updateGiveWay(String msg, String dst) {
		giveWayMsg.setText(msg);
		giveWayDistanz.setText(dst);
	}

	public void updateRightOfWay(String msg, String dst) {
		takeWayMsg.setText(msg);
		takeWayDistanz.setText(dst);
	}

	public void updateStopWay(String msg, String dst) {
		stopMsg.setText(msg);
		stopDistanz.setText(dst);
	}

	public void updateStateMsg(String msg) {
		stateMsg.setText(msg);
	}

	public void startStopCountdown(final String msg, final int duration) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = duration/10; i >= 0; i--) {
					updateStateMsg(msg + " " + String.valueOf(i) + " [ms]");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();;
	}
}
