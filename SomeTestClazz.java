package urban_robot_controller;

import org.opencv.core.Scalar;

import urban_robot_controller.actions.DriveBetween;
import urban_robot_controller.actions.MakeSideShiftBack;
import urban_robot_controller.actions.RunParallelToStructure;
import urban_robot_controller.actions.MakeSideShift.ShiftDirection;
import urban_robot_controller.actions.RunParallelToStructure.OrientationSide;
import urban_robot_controller.actions.routeRecorder.Route;
import urban_robot_controller.my_distance_sensor.IMyDistanceSensor;
import urban_robot_controller.my_distance_sensor.MyEOPDSensor;
import urban_robot_controller.procedures.object_recognition.TrafficSign;
import urban_robot_controller.procedures.object_recognition.local_sign_detection.Objekterkennung;
import urban_robot_controller.procedures.object_recognition.local_sign_detection.Videoverarbeitung;
import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.Parkour;
import urban_robot_controller.robot_utility.RobotData;
import urban_robot_controller.robot_utility.RobotProvider;
import ch.aplu.ev3.SensorPort;
import ch.aplu.ev3.UltrasonicListener;

public class SomeTestClazz {
	private static IRobot robot;
	
	public static void main(String[] args) {

		robot = RobotProvider.getEv31();
		robot.bindParkourToRobot(Parkour.RouteTwo);
		RobotData.startLineFollower(robot);
		
		
		
		Videoverarbeitung video = new Videoverarbeitung(0,"Funktionierdendes Video");
		video.start();
		
		
		Objekterkennung obj1 = new Objekterkennung(video,"classifier/sign_stop_v1.1.xml",TrafficSign.Stop.name(), new Scalar(255,0,0));
		obj1.start();
		Objekterkennung obj2 = new Objekterkennung(video,"classifier/vorfahrt_Mai_12_08_11_48.xml",TrafficSign.RightOfWay.name(),new Scalar(0,255,0));
		obj2.start();
		Objekterkennung obj3 = new Objekterkennung(video,"classifier/vor_gewaehren_Mai_11_21_42_53.xml", TrafficSign.GiveWay.name(), new Scalar(0,0,255));
		obj3.start();
		
		obj1.registerObserver(robot);
		obj1.registerObserver(video);
		obj2.registerObserver(video);
		obj2.registerObserver(robot);
		obj3.registerObserver(video);
		obj3.registerObserver(robot);
	}
}
