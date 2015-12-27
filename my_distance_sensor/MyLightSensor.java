package urban_robot_controller.my_distance_sensor;

import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.SensorPort;

public class MyLightSensor extends LightSensor implements IMyLightSensor{
	
	public MyLightSensor(SensorPort port) {
		super(port);
	}
	
	@Override
	public int getValue() {
		return super.getValue();
	}
}
