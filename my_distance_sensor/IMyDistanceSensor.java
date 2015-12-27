package urban_robot_controller.my_distance_sensor;

import ch.aplu.ev3.UltrasonicListener;

public interface IMyDistanceSensor {

	int getDistance();
	
	public void addUltrasonicListener(UltrasonicListener ultrasonicListener, int triggerLevel);
}
