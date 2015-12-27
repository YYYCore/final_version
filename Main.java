package urban_robot_controller;

import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.Parkour;
import urban_robot_controller.robot_utility.RobotData;
import urban_robot_controller.robot_utility.RobotProvider;
import configs.Configs;


public class Main {
	public static void main(String[] args) {
		RobotData.startCrossRoadUi();
//		RobotProvider.getTrafficController();
		
		IRobot e31 = RobotProvider.getEv31();
		RobotData.startUiForRobot(e31);
		
		IRobot e6e7 = RobotProvider.getE6E7();
		RobotData.startUiForRobot(e6e7);
		
		e31.bindParkourToRobot(Parkour.RouteTwo);
		RobotData.startLineFollower(e31);
		
		e6e7.bindParkourToRobot(Parkour.RouteOne);
		RobotData.startLineFollower(e6e7);

		RobotData.StartTrafficSignDetectionServer(Configs.SERVER_PORT);
		

		//  E6E7  "http://192.168.11.213:8080"
		//  E31   "http://192.168.11.32:8080"
	}
	
	private static void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
