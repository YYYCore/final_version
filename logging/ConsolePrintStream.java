package urban_robot_controller.logging;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;


// TODO: Auto-generated Javadoc
/**
 * The Class ConsolePrintStream.
 */
public class ConsolePrintStream 
{

	/** The console frame. */
	private JFrame consoleFrame;
	private JTextArea signOutput;
	/**
	 * Instantiates a new console print stream.
	 */
	public ConsolePrintStream()
	{
		consoleFrame = new JFrame("Log Data - IRobot");
		consoleFrame.setSize(320, 350);
		consoleFrame.setLocation(950, 120);
		consoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		consoleFrame.setResizable(false);

		final JTextArea logOutput = new JTextArea();
		logOutput.setEditable(false);
		logOutput.setMargin(new Insets(10, 10, 10, 10));

		signOutput = new JTextArea() {	
			/**
			 * 
			 */
			private static final long serialVersionUID = 2006468631774678978L;

			public Dimension getPreferredSize() {
				return new Dimension(320, 80);
			};
		};
		signOutput.setEditable(false);
		signOutput.setMargin(new Insets(10, 10, 10, 10));

		DefaultCaret caret = (DefaultCaret)logOutput.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.add(logOutput);
		scrollPane.setViewportView(logOutput);

		PrintStream streamOut = new PrintStream(System.out){

			@Override
			public void print(String s)
			{
				logOutput.append(s + "\n");
			}
		};

		System.setOut(streamOut);
		consoleFrame.add(scrollPane, BorderLayout.CENTER);
		consoleFrame.add(signOutput, BorderLayout.NORTH);
		consoleFrame.setVisible(true);
	}

	public void setText(String sign, int distance) {
		String newSign = "";
		switch(sign.toLowerCase()){
		case "stopp":
			newSign += "Stopp: \tdetected  \t" + distance + " cm\n";
			newSign += "Vorfahrt: \t------------ \t---------- \n";
			newSign += "gewähren: \t------------ \t";
			signOutput.setText(newSign);
			break;

		case "vorfahrt":
			newSign += "Stopp: \t------------ \t---------- \n";
			newSign += "Vorfahrt: \tdetected  \t" + distance + " cm\n";
			newSign += "gewähren: \t------------ \t";
			signOutput.setText(newSign);
			break;

		default:
			newSign += "Stopp: \t------------  \t---------- \n";
			newSign += "Vorfahrt: \t------------ \t---------- \n";
			newSign += "gewähren: \t------------ \t---------- \n";
			signOutput.setText(newSign);
			break;
		}

	}
}