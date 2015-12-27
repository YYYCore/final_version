package urban_robot_controller.robot_utility;

import urban_robot_controller.my_distance_sensor.IGear;
import urban_robot_controller.my_distance_sensor.IMyDistanceSensor;
import urban_robot_controller.my_distance_sensor.MyGear;
import ch.aplu.ev3.Gear;

public class LogData implements Runnable {

	@Override
	public void run() {
		IRobot robot = RobotProvider.getE6E7();
		IMyDistanceSensor front = robot.getFrontDistanceSensor();
		IMyDistanceSensor right = robot.getRightSensor();
		IMyDistanceSensor left = robot.getLeftSensor();
		IGear gear = robot.getGear();
		
		while(true) {
			System.out.println("front " + front.getDistance());
			System.out.println("left " + left.getDistance());
			System.out.println("right " + right.getDistance());
			System.out.println("gearspeed " + gear.getSpeed());
			System.out.println();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
	
	}

}
