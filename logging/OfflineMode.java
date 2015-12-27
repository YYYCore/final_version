package urban_robot_controller.logging;

import java.io.File;
import java.io.FileNotFoundException;

import ch.aplu.robotsim.LegoRobot;
import ch.aplu.robotsim.LightSensor;
import ch.aplu.robotsim.Motor;
import ch.aplu.robotsim.MotorPort;
import ch.aplu.robotsim.RobotContext;
import ch.aplu.robotsim.SensorPort;
import ch.aplu.robotsim.TouchSensor;

// TODO: Auto-generated Javadoc
/**
 * The Class OfflineMode.
 */
public class OfflineMode {

	/** The file. */
	private File file;

	/** The directory. */
	private final String directory = "C:/Users/Mat/workspace/projektarbeit/EV3/src/logging/logfiles";

	/** The end line. */
	private boolean endLine = false;

	static {
		RobotContext.setStartPosition(250, 460);	
	}

	/**
	 * Instantiates a new main.
	 *
	 * @throws FileNotFoundException the file not found exception
	 */
	public OfflineMode() throws FileNotFoundException {

		ConsolePrintStream test = new ConsolePrintStream();
		
		//getting logfile
		OpenDialog fileChooser = new OpenDialog(directory);
		LegoRobot robot = new LegoRobot();

		//redirect console log to JFrame
		
		test.setText("fjaskfjlasdfjsdfajl", 50);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//select file
		file = fileChooser.open();

		//starting log reader
		LogReader logOutput = new LogReader();
		logOutput.setFile(file);
		logOutput.start();

		Motor motorlinks = new Motor(MotorPort.A);
		Motor motorrechts = new Motor(MotorPort.B);
		LightSensor ls = new LightSensor(SensorPort.S1);
		TouchSensor ts = new TouchSensor(SensorPort.S2);

		robot.addPart(motorlinks);
		robot.addPart(motorrechts);	
		robot.addPart(ls);
		robot.addPart(ts);


		if(logOutput.getConfig().isEmpty()) {
			System.out.println("Exiting...\nWrong configuration file chosen"); 
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}

		System.out.println("Configuration:");
		int i = 0;
		for (String config : logOutput.getConfig()){
			
			if(config.contains("time"))
				System.out.println("");

			if(config.contains("#")){
				if(!endLine){
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("");

					for (String commands : logOutput.getCommands()){
						test.setText(i + ". Durchlauf", 100);
						i++;
						String[] command = commands.split("\\.");

						if(command[0].contains("motorlinks") && command[1].contains("setSpeed")){
							motorlinks.setSpeed(getParams(command[1]));
							System.out.println("Command: " + commands);
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						if(command[0].contains("motorrechts") && command[1].contains("setSpeed")){
							motorrechts.setSpeed(getParams(command[1]));
							System.out.println("Command: " + commands);
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						if(command[0].contains("motorlinks") && command[1].contains("forward")){
							System.out.println("Command: " + commands);
							continue;
						}

						if(command[0].contains("motorrechts") && command[1].contains("forward")){
							motorlinks.forward();
							motorrechts.forward();
							System.out.println("Command: " + commands);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					endLine = true;
				}
				continue;
			}
			System.out.println(config.trim());
			
		}

		motorlinks.stop();
		motorrechts.stop();
	}



	/**
	 * Gets the parameters within brackets.
	 *
	 * @param str the str
	 * @return the params
	 */
	public static int getParams(String str){
		return Integer.parseInt(str.substring(str.indexOf('(')+1,str.indexOf(')')));
	}


}
