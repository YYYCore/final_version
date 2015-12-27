package urban_robot_controller.robot_utility;

import ch.aplu.ev3.LegoRobot;

public class RobotProvider extends LegoRobot{
	public static IRobot getE6E7() {
		return E6E7Robot.getInstance();
	}
	
	public static IRobot getE8() {
		return Ev8Robot.getInstance();
	}
	
	public static IRobot getEv31() {
		return Ev31Robot.getInstance();
	}
	
	public static IRobot getUserControlledRobot() {
		return null;
	}
	
	public static IRobot getTrafficController() {
		return CrossroadRobot.getInstance();
	}

}
