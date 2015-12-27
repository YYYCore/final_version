package urban_robot_controller.procedures.crossroad;

import java.util.LinkedList;
import java.util.Queue;

import urban_robot_controller.robot_utility.RobotData;
import urban_robot_controller.ui.CrossroadUi;

public class CrossRoadA {
	
	private static CrossroadUi ui = RobotData.getCrossroadUi();;

	public CrossRoadA() {
//		delteExpiredSlots();
		
	}

	private void delteExpiredSlots() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RobotQueueSlot slot = waitingQueue.peek();
				if(slot!=null) {
					long delta = System.currentTimeMillis() - slot.getTimeStamp();
					if(delta > 10000) {
						waitingQueue.poll();
					}
				}
				
				slot = priorityQueue.peek();
				if(slot !=null) {
					long delta = System.currentTimeMillis() - slot.getTimeStamp();
					if(delta > 10000) {
						priorityQueue.poll();
					}
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private static Queue<RobotQueueSlot> waitingQueue = new LinkedList<RobotQueueSlot>();
	private static Queue<RobotQueueSlot> priorityQueue = new LinkedList<RobotQueueSlot>();
	private static CrossRoadState state = CrossRoadState.Free;
	
	private static void update() {
		if(state.isFree()) {
			if(!priorityQueue.isEmpty()) {
				ui.updateA2("", false);
				givePremission(priorityQueue.poll());
			} else {
				if(!waitingQueue.isEmpty()) {
					ui.updateA1("", false);
					givePremission(waitingQueue.poll());
				}
			}
		}
	}

	public static void addWaitingQueueSlot(RobotQueueSlot slot) {
		ui.updateA1(slot.getRobot().toString(), true);
		waitingQueue.offer(slot);
		update();
	}
	
	public static void addPriorityQueueSlot(RobotQueueSlot slot) {
		ui.updateA2(slot.getRobot().toString(), true);
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
		RobotData.startPassCrossRoad(slot.getRobot(), CrossRoad.A);
		setState(CrossRoadState.Blocked);
	}
	
	public static void setState(CrossRoadState state) {
		if(state.isBlocked()) {
			ui.lockA();
		} else {
			ui.unlockA();
		}
		CrossRoadA.state = state;
		update();
	}

	public static CrossRoadState getState() {
		return state;
	}

	
}
