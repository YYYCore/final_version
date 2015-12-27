package urban_robot_controller.my_distance_sensor;

import urban_robot_controller.logging.OnlineMode;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.GenericGear;

public class MyGear extends Gear implements IGear {

	@Override
	public GenericGear forward() {
		if (OnlineMode.gear != null) {
			OnlineMode.gear.forward();

		}
		return super.forward();
	}

	@Override
	public GenericGear leftArc(double radius) {
		if (OnlineMode.gear != null) {
			OnlineMode.gear.leftArc(radius);

		}
		return super.leftArc(radius);
	}

	@Override
	public GenericGear leftArc(double radius, int duration) {
		if (OnlineMode.gear != null) {
			OnlineMode.gear.leftArc(radius, duration);

		}
		return super.leftArc(radius, duration);
	}

	@Override
	public GenericGear rightArc(double radius) {
		if (OnlineMode.gear != null) {
			OnlineMode.gear.rightArc(radius);
		}
		return super.rightArc(radius);
	}

	@Override
	public GenericGear rightArc(double radius, int duration) {
		if (OnlineMode.gear != null) {
			OnlineMode.gear.rightArc(radius, duration);
		}
		return super.rightArc(radius, duration);
	}

	@Override
	public GenericGear stop() {
		if (OnlineMode.gear != null) {
			OnlineMode.gear.stop();

		}
		return super.stop();
	}

	@Override
	public GenericGear setSpeed(int speed) {
		if (OnlineMode.gear != null) {
			OnlineMode.gear.setSpeed(speed);
		}
		return super.setSpeed(speed);
	}

}
