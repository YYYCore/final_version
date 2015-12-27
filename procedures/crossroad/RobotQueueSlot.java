package urban_robot_controller.procedures.crossroad;

import urban_robot_controller.robot_utility.IRobot;

public class RobotQueueSlot {
	private long timeStamp;
	private IRobot robot;
	
	public RobotQueueSlot(IRobot robot, long timeStamp) {
		this.robot = robot;
		this.timeStamp = timeStamp;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public IRobot getRobot() {
		return robot;
	}

	public void setRobot(IRobot robot) {
		this.robot = robot;
	}
}
