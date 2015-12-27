package urban_robot_controller.my_distance_sensor;

import ch.aplu.ev3.SensorPort;
import ch.aplu.ev3.UltrasonicSensor;

public class MyUltrasonicSensor extends UltrasonicSensor implements IMyDistanceSensor{

	public MyUltrasonicSensor(SensorPort s2) {
		super(s2);
	}

}
