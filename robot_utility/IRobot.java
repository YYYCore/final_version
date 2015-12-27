package urban_robot_controller.robot_utility;

import java.util.Observer;

import urban_robot_controller.my_distance_sensor.IGear;
import urban_robot_controller.my_distance_sensor.IMyDistanceSensor;
import urban_robot_controller.my_distance_sensor.IMyLightSensor;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;

public interface IRobot extends Observer{

	IMyDistanceSensor getLeftSensor();
	
	IMyDistanceSensor getRightSensor();
	
	IMyDistanceSensor getFrontDistanceSensor();
	
	IGear getGear();

	void setState(RoboState state);
	
	RoboState getState();

	IMyLightSensor getLeftLightSensor();

	IMyLightSensor getRightLightSensor();

	String toString();

	ColorSensor getColorSensor();
	
	void bindParkourToRobot(Parkour parkour);
}
