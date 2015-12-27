package urban_robot_controller.procedures.object_recognition.local_sign_detection;

import org.opencv.core.Scalar;

import urban_robot_controller.procedures.object_recognition.TrafficSign;
import urban_robot_controller.robot_utility.IRobot;

public class TrafficSignDetectionLauncher implements Runnable{
	
	private IRobot robot;
	private String webcampIp;

	public TrafficSignDetectionLauncher(IRobot robot, String webcamIp) {
		this.robot = robot;
		this.webcampIp = webcamIp;
	}
	
	@Override
	public void run() {

		Videoverarbeitung video = new Videoverarbeitung(webcampIp,"Funktionierdendes Video");
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
