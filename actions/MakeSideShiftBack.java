package urban_robot_controller.actions;

import urban_robot_controller.robot_utility.RUT;

public class MakeSideShiftBack extends MakeSideShift{

	public MakeSideShiftBack(ShiftDirection direction, int cmToShift) {
		super(direction, cmToShift);
	}
	
	@Override
	protected void doSideShift() throws InterruptedException {
		gear = robot.getGear();
		gear.stop();
		gear.backward(RUT.cm_to_ms((int) Math.round(cmToShift*2.4)));
		Thread.sleep(1000);
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

}
