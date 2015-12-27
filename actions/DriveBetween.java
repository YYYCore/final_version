package urban_robot_controller.actions;

import urban_robot_controller.my_distance_sensor.MyEOPDSensor;
import urban_robot_controller.my_distance_sensor.MyEOPDSensorRight;
import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.RobotProvider;

public class DriveBetween implements Runnable{
	private IRobot robot;
	private int distanceLeft;
	private int distanceRight;

	public DriveBetween(IRobot robot) {
		this.robot = robot;
	}

	private void drive() throws InterruptedException {
		distanceLeft = ((MyEOPDSensor) robot.getLeftSensor()).getFineDistance();
		distanceRight = ((MyEOPDSensorRight) robot.getRightSensor()).getFineDistance();
//		System.out.println(distanceLeft + " <[mm]left  right[mm]> " + distanceRight);
		if(distanceLeft == 100 || distanceRight == 100) {
			return;
		}
		int delta = distanceLeft - distanceRight;
		if(delta >= 0 && Math.abs(delta) >= 10) {
			manouverLeft();
		}
		if(delta <= 0 && Math.abs(delta) >= 10) {
			manouverRight();
		}
	}
	
	
	private void manouverRight() {
//		System.out.println(distanceLeft + " <[mm]left RIGHT right[mm]> " + distanceRight);
		robot.getGear().rightArc(0.5,200);
		robot.getGear().forward();
	}

	private void manouverLeft() {
//		System.out.println(distanceLeft + " <[mm]left LEFT right[mm]> " + distanceRight);
		robot.getGear().leftArc(0.5,200);
		robot.getGear().forward();
	}

	@Override
	public void run() {
		while(true) {
			try {
				if(robot.getState().isDriving()) {
					drive();
				}
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
