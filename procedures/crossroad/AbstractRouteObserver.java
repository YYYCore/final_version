package urban_robot_controller.procedures.crossroad;

import java.util.Observable;
import java.util.Observer;

import urban_robot_controller.procedures.object_recognition.TrafficSign;

public abstract class AbstractRouteObserver implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		TrafficSign sign = getDetectedSign(arg);
		switch (sign) {
		case Stop: {
			stopSignAction(getDistance(arg));
			break;
		}
		case GiveWay: {
			giveWayAction(getDistance(arg));
			break;
		}
		case RightOfWay: {
			rightOfWayAction(getDistance(arg));
			break;
		}
		default: {

		}
		}
	}

	abstract void rightOfWayAction(float distance);

	abstract void giveWayAction(float distance);

	abstract void stopSignAction(float distance);

	private float getDistance(Object arg) {
		Object[] argArray = (Object[]) arg;
		float i = (Float) argArray[1];
		return i;
	}

	private TrafficSign getDetectedSign(Object arg) {
		Object[] argArray = (Object[]) arg;
		if (((String) argArray[0]).equals(TrafficSign.Stop.name())) {
			return TrafficSign.Stop;
		}
		if (((String) argArray[0]).equals(TrafficSign.GiveWay.name())) {
			return TrafficSign.GiveWay;
		}
		if (((String) argArray[0]).equals(TrafficSign.RightOfWay.name())) {
			return TrafficSign.RightOfWay;
		}
		return null;
	}

	public void pause(int duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
