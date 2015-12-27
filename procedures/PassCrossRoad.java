package urban_robot_controller.procedures;

import static urban_robot_controller.robot_utility.RoboConfigs.ROBOT_CROSSING_ROAD;
import static urban_robot_controller.robot_utility.RoboConfigs.ROBOT_LINEFOLLOWING;
import urban_robot_controller.procedures.crossroad.CrossRoad;
import urban_robot_controller.procedures.crossroad.CrossRoadA;
import urban_robot_controller.procedures.crossroad.CrossRoadB;
import urban_robot_controller.procedures.crossroad.CrossRoadState;
import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.RobotData;
import urban_robot_controller.ui.RobotUi;

public class PassCrossRoad implements Runnable{

	private IRobot robot;
	private CrossRoad crossRoad;
	private RobotUi ui;

	public PassCrossRoad(IRobot robot, CrossRoad crossRoad) {
		this.crossRoad = crossRoad;
		this.robot = robot;
		ui = RobotData.getRobotUi(robot);
	}
	
	@Override
	public void run() {
		if(robot != null) {
			if(!RobotData.isLineFollowerRunning(robot)) {
				RobotData.startLineFollower(robot);
			}
			ui.updateStateMsg(ROBOT_CROSSING_ROAD);
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("unlock " + crossRoad.toString());
			if(crossRoad.isA()) {
				CrossRoadA.setState(CrossRoadState.Free);
			} else if (crossRoad.isB()) {
				CrossRoadB.setState(CrossRoadState.Free);
			}
			
			ui.updateStateMsg(ROBOT_LINEFOLLOWING);
		}
	}

}
