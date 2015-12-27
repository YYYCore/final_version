package urban_robot_controller.procedures.crossroad;

import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

import static urban_robot_controller.robot_utility.RoboConfigs.*;
import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.RUT;
import urban_robot_controller.robot_utility.RobotData;
import urban_robot_controller.ui.RobotUi;

public class RouteTwoObserver extends AbstractRouteObserver implements Observer {
	private IRobot robot;
	private AtomicBoolean giveWayLock = new AtomicBoolean(false);
	private AtomicBoolean takeWayLock = new AtomicBoolean(false);
	private RobotUi ui;

	public RouteTwoObserver(IRobot robot) {
		this.robot = robot;
		ui = RobotData.getRobotUi(robot);
	}

	@Override
	void rightOfWayAction(float distance) {
		if (!takeWayLock.get()) {
			takeWayLock.set(true);
			ui.updateRightOfWay(SIGN_DETECT_MSG, String.valueOf(distance));
			if (distance < RUT.cameraDistance_mm(40)) {
				ui.updateRightOfWay(SIGN_PROCESSING_MSG,
						String.valueOf(distance));
				if (CrossRoadA.getState().isBlocked()) {
					ui.updateStateMsg(ROBOT_WAITING);
					robot.getGear().stop();
					RobotData.stopLineFollower(robot);
				}
				CrossRoadA.addPriorityQueueSlot(new RobotQueueSlot(robot,
						System.currentTimeMillis()));
				ui.updateRightOfWay(NO_SING_DETECTED, "  -");
				pause(3000);
			}
			takeWayLock.set(false);
		}
	}

	@Override
	void giveWayAction(float distance) {
		if (!giveWayLock.get()) {
			giveWayLock.set(true);
			ui.updateGiveWay(SIGN_DETECT_MSG, String.valueOf(distance));
			if (distance < RUT.cameraDistance_mm(60)) {
				ui.updateGiveWay(SIGN_PROCESSING_MSG, String.valueOf(distance));
				if (CrossRoadB.getState().isBlocked()) {
					pause(2000);
					ui.updateStateMsg(ROBOT_WAITING);
					robot.getGear().stop();
					RobotData.stopLineFollower(robot);
				}
				CrossRoadB.addWaitingQueueSlot(new RobotQueueSlot(robot, System
						.currentTimeMillis()));
				ui.updateGiveWay(NO_SING_DETECTED, "  -");
				pause(3000);
			}
			giveWayLock.set(false);
		}
	}

	@Override
	void stopSignAction(float distance) {

	}
}
