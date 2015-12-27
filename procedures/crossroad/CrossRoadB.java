package urban_robot_controller.procedures.crossroad;

import java.util.LinkedList;
import java.util.Queue;

import urban_robot_controller.robot_utility.RobotData;
import urban_robot_controller.ui.CrossroadUi;

public class CrossRoadB {

	private static Queue<RobotQueueSlot> waitingQueue = new LinkedList<RobotQueueSlot>();
	private static Queue<RobotQueueSlot> priorityQueue = new LinkedList<RobotQueueSlot>();
	private static CrossRoadState state = CrossRoadState.Free;
	private static CrossroadUi ui= RobotData.getCrossroadUi();
	
	public CrossRoadB() {
	}
	
	private static void update() {
		if(state.isFree()) {
			if(!priorityQueue.isEmpty()) {
				ui.updateB1("", false);
				givePremission(priorityQueue.poll());
			} else {
				if(!waitingQueue.isEmpty()) {
					ui.updateB2("", false);
					givePremission(waitingQueue.poll());
				}
			}
		}
	}

	public static void addWaitingQueueSlot(RobotQueueSlot slot) {
		ui.updateB1(slot.getRobot().toString(), true);
		waitingQueue.offer(slot);
		update();
	}
	
	public static void addPriorityQueueSlot(RobotQueueSlot slot) {
		ui.updateB2(slot.getRobot().toString(), true);
		priorityQueue.offer(slot);
		update();
	}
	
	public static RobotQueueSlot peekLastWaitingQueueSlot() {
		return waitingQueue.peek();
	}
	
	public static RobotQueueSlot peekLastPriorityQueueSlot() {
		return priorityQueue.peek();
	}
	
	private static void givePremission(RobotQueueSlot slot) {
		RobotData.startPassCrossRoad(slot.getRobot(), CrossRoad.B);
		setState(CrossRoadState.Blocked);
	}
	
	public static void setState(CrossRoadState state) {
		if(state.isBlocked()) {
			ui.lockB();
		} else {
			ui.unlockB();
		}
		CrossRoadB.state = state;
		update();
	}

	public static CrossRoadState getState() {
		return state;
	}


}
