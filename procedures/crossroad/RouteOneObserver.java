package urban_robot_controller.procedures.crossroad;

import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.RUT;
import urban_robot_controller.robot_utility.RobotData;
import urban_robot_controller.ui.RobotUi;
import static urban_robot_controller.robot_utility.RoboConfigs.*;

public class RouteOneObserver extends AbstractRouteObserver implements Observer {
	private IRobot robot;
	private AtomicBoolean isStopHandled = new AtomicBoolean(false);
	private AtomicBoolean rightOfWayLock = new AtomicBoolean(false);
	private RobotUi ui;
	
	public RouteOneObserver(IRobot robot) {
		this.robot = robot;
		ui = RobotData.getRobotUi(robot);
	}

	@Override
	void rightOfWayAction(float distance) {
		if(!rightOfWayLock.get()) {
			rightOfWayLock.set(true);
			ui.updateRightOfWay(SIGN_DETECT_MSG, String.valueOf(distance));
			if (distance < RUT.cameraDistance_mm(50)) {
				ui.updateRightOfWay(SIGN_PROCESSING_MSG, String.valueOf(distance));
				pause(3000);
				if (CrossRoadB.getState().isBlocked()) {
					ui.updateStateMsg(ROBOT_WAITING);
					robot.getGear().stop();
					RobotData.stopLineFollower(robot);
				}
				CrossRoadB.addPriorityQueueSlot(new RobotQueueSlot(robot, System.currentTimeMillis()));
				ui.updateRightOfWay(NO_SING_DETECTED, "  -");
				pause(10000);
			}
			rightOfWayLock.set(false);
		}
	}

	@Override
	void giveWayAction(float distance) {
		//nothing to do here
	}

	@Override
	void stopSignAction(float distance) {
		if(!isStopHandled.get()) {
			isStopHandled.set(true); 
			ui.updateStopWay(SIGN_DETECT_MSG, String.valueOf(distance));
			if (distance < 40) {
				ui.updateStopWay(SIGN_PROCESSING_MSG, String.valueOf(distance));
				pause(2000);
				ui.startStopCountdown(ROBOT_STOP_WATING,2000);
				RobotData.stopLineFollower(robot);
				robot.getGear().stop();
				
				pause(2000);
				CrossRoadA.addWaitingQueueSlot(new RobotQueueSlot(robot, System.currentTimeMillis()));
				ui.updateStopWay(NO_SING_DETECTED, "  -");
				pause(3000);
			}
			isStopHandled.set(false);
		}
	}
}
