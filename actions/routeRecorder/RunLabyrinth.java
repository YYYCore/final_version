package urban_robot_controller.actions.routeRecorder;

import urban_robot_controller.actions.DecisionMaking;
import urban_robot_controller.my_distance_sensor.IMyDistanceSensor;
import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.RobotProvider;
import ch.aplu.ev3.SensorPort;
import ch.aplu.ev3.UltrasonicListener;

public class RunLabyrinth implements Runnable{
	private static final int COLLISION_TRIGGER_SIDE = 4;
	private static final int COLLISION_TRIGGER_FRONT = 9;
	private IRobot robot;

	@Override
	public void run() {
		this.robot = RobotProvider.getE6E7();
		robot.getGear().forward();
		initLabyrinthListener();
	}

	public void initLabyrinthListener() {
		robot.getFrontDistanceSensor().addUltrasonicListener(new UltrasonicListener() {
			@Override
			public void near(SensorPort port, int level) {
				if(robot.getState().isDriving()) {
					new DecisionMaking();
				}
			}
			
			@Override
			public void far(SensorPort port, int level) {
			}
		}, COLLISION_TRIGGER_FRONT);
	}
}
