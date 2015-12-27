package urban_robot_controller.actions;

import urban_robot_controller.my_distance_sensor.IGear;
import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.RUT;
import urban_robot_controller.robot_utility.RobotProvider;
import ch.aplu.ev3.Gear;

public class MakeSideShift implements Runnable{
	protected  static final int SLEEP = 300;
	protected static final int HARD_SHIFT_TRIGGER = 2;
	protected int cmToShift;
	protected IRobot robot;
	protected ShiftDirection direction;
	protected IGear gear;


	public MakeSideShift(ShiftDirection direction, int cmToShift) {
		this.cmToShift = cmToShift;
		this.robot = RobotProvider.getE6E7();
		this.direction = direction;
	}
	
	@Override
	public void run() {
		try {
			doSideShift();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void doSideShift() throws InterruptedException {
		gear = robot.getGear();
		gear.stop();
		if(direction == ShiftDirection.Left) {
			if(robot.getRightSensor().getDistance() > HARD_SHIFT_TRIGGER ) {
				softArcLeft();
			}
		}
		if(direction == ShiftDirection.Right) {
			if(robot.getRightSensor().getDistance() > HARD_SHIFT_TRIGGER ) {
				softArcRight();
			}
		}
	}
	
	protected void softArcLeft() throws InterruptedException {
		gear.leftArc(0.1, 500);
		Thread.sleep(SLEEP);
		int cm = cmToShift - 3;
		if(cm > 0) {
			gear.forward(RUT.cm_to_ms(cm));
		}
		Thread.sleep(SLEEP);
		gear.rightArc(0.1, 500);
		Thread.sleep(SLEEP);
	}

	protected void softArcRight() throws InterruptedException {
		gear.rightArc(0.1, 500);
		Thread.sleep(SLEEP);
		int cm = cmToShift - 3;
		if(cm > 0) {
			gear.forward(RUT.cm_to_ms(cm));
		}
		Thread.sleep(SLEEP);
		gear.leftArc(0.1, 500);
		Thread.sleep(SLEEP);		
	}

	private void hardShiftLeft() throws InterruptedException {
		gear.left(RUT.angle_to_ms(90));
		Thread.sleep(SLEEP);
		gear.forward(RUT.cm_to_ms(cmToShift));
		Thread.sleep(SLEEP);
		gear.right(RUT.angle_to_ms(90));
		Thread.sleep(SLEEP);
	}
	
	private void hardShiftRight() throws InterruptedException {
		gear.right(RUT.angle_to_ms(90));
		Thread.sleep(SLEEP);
		gear.forward(RUT.cm_to_ms(cmToShift));
		Thread.sleep(SLEEP);
		gear.left(RUT.angle_to_ms(90));
		Thread.sleep(SLEEP);
	}
	
	public enum ShiftDirection {
		Right, Left
	}
}
