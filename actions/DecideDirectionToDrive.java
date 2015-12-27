package urban_robot_controller.actions;

import java.util.logging.Logger;

import urban_robot_controller.actions.RunParallelToStructure.OrientationSide;
import urban_robot_controller.my_distance_sensor.IGear;
import urban_robot_controller.my_distance_sensor.IMyDistanceSensor;
import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.RUT;
import urban_robot_controller.robot_utility.RobotData;
import ch.aplu.ev3.Gear;

public class DecideDirectionToDrive implements Runnable {
	private static final Logger LOG = Logger.getLogger(DecideDirectionToDrive.class.getName());
	private IRobot robot;

	public DecideDirectionToDrive(IRobot robot) {
		this.robot = robot;
	}
	
	private void decide() {
		IGear gear = robot.getGear();
		gear.stop();
		IMyDistanceSensor left = robot.getLeftSensor();
		IMyDistanceSensor right = robot.getRightSensor();
		int leftDistance = left.getDistance();
		int rightDistance = right.getDistance();
		int frontDistance = robot.getFrontDistanceSensor().getDistance();
		LOG.info("------ Left " + leftDistance + " Right " + rightDistance + " Front " + frontDistance + "\n");
		if(rightDistance > leftDistance && leftDistance != 0) {
			LOG.info("Decided to Drive right with distance Trigger " + frontDistance);
			gear.backward(RUT.cm_to_ms(3));
			gear.right(RUT.angle_to_ms(90));
			RunParallelToStructure structureMethode= new RunParallelToStructure(OrientationSide.Left);
//			RobotData.startRunParallelToStructureThread(structureMethode);
		} else {
			LOG.info("Decided to Drive left with distance Trigger " + frontDistance);
			gear.backward(RUT.cm_to_ms(3));
			gear.left(RUT.angle_to_ms(90));
			RunParallelToStructure structureMethode= new RunParallelToStructure(OrientationSide.Right);
//			RobotData.startRunParallelToStructureThread(structureMethode);
		}
	}
	
	@Override
	public void run() {
		decide();
	}

}
