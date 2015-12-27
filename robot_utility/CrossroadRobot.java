package urban_robot_controller.robot_utility;

import java.util.Observable;

import urban_robot_controller.my_distance_sensor.IGear;
import urban_robot_controller.my_distance_sensor.IMyDistanceSensor;
import urban_robot_controller.my_distance_sensor.IMyLightSensor;
import urban_robot_controller.my_distance_sensor.MyIRDistanceSensor;
import urban_robot_controller.procedures.crossroad.CrossRoadA;
import urban_robot_controller.procedures.crossroad.CrossRoadB;
import urban_robot_controller.procedures.crossroad.CrossRoadState;
import urban_robot_controller.procedures.crossroad.RobotQueueSlot;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.SensorPort;
import ch.aplu.ev3.UltrasonicListener;

public class CrossroadRobot extends LegoRobot implements IRobot {
	private static final String X_ROAD_IP = "192.168.11.38";
	private static final long TIME_TRIGGER = 2000;
	private static CrossroadRobot robot;
	private static final int TRIGGER_LEVEL = 30;
	private MyIRDistanceSensor a1;
	private MyIRDistanceSensor b1;
	private MyIRDistanceSensor b2;
	private MyIRDistanceSensor a2;
	
	private CrossroadRobot() {
		super(X_ROAD_IP);
		robot = this;

		a1 = new MyIRDistanceSensor(SensorPort.S1);
		b1 = new MyIRDistanceSensor(SensorPort.S3);
		a2 = new MyIRDistanceSensor(SensorPort.S2);
		b2 = new MyIRDistanceSensor(SensorPort.S4);
			
//		robot.addPart(a1);
//		robot.addPart(b1);
		robot.addPart(a2);
//		robot.addPart(b2);
		
		addCrossRoadListener();
	}
	
	private void addCrossRoadListener() {
		a2.addUltrasonicListener(new UltrasonicListener() {
			
			@Override
			public void near(SensorPort port, int level) {
				System.out.println("-------incoming");
				RobotQueueSlot lastSlot = CrossRoadA.peekLastPriorityQueueSlot();
				if(lastSlot == null) {
					CrossRoadA.addPriorityQueueSlot(new RobotQueueSlot(RobotProvider.getUserControlledRobot(), System.currentTimeMillis()));
				} else {
					long deltaTime = System.currentTimeMillis() - lastSlot.getTimeStamp();
					if(deltaTime > TIME_TRIGGER) {
						CrossRoadA.addPriorityQueueSlot(new RobotQueueSlot(RobotProvider.getUserControlledRobot(), System.currentTimeMillis()));
					}
				}
			}
			
			@Override
			public void far(SensorPort port, int level) {
				System.out.println("-------leaving");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				CrossRoadA.setState(CrossRoadState.Free);
			}
		}, TRIGGER_LEVEL);
		
		b1.addUltrasonicListener(new UltrasonicListener() {
			
			@Override
			public void near(SensorPort port, int level) {
				RobotQueueSlot lastSlot = CrossRoadA.peekLastPriorityQueueSlot();
				if(lastSlot == null) {
					CrossRoadB.addPriorityQueueSlot(new RobotQueueSlot(RobotProvider.getUserControlledRobot(), System.currentTimeMillis()));
				} else {
					long deltaTime = System.currentTimeMillis() - lastSlot.getTimeStamp();
					if(deltaTime > TIME_TRIGGER) {
						CrossRoadA.addPriorityQueueSlot(new RobotQueueSlot(RobotProvider.getUserControlledRobot(), System.currentTimeMillis()));
					}
				}
			}
			
			@Override
			public void far(SensorPort port, int level) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				CrossRoadA.setState(CrossRoadState.Free);
			}
		}, TRIGGER_LEVEL);
	}
	
	public MyIRDistanceSensor getD1() {
		return a1;
	}


	public MyIRDistanceSensor getD2() {
		return b1;
	}

	public MyIRDistanceSensor getD4() {
		return b2;
	}

	public MyIRDistanceSensor getD3() {
		return a2;
	}

	public static IRobot getInstance() {
		if(robot == null) {
			robot = new CrossroadRobot();
		}
		return robot;
	}
	
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IMyDistanceSensor getLeftSensor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMyDistanceSensor getRightSensor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMyDistanceSensor getFrontDistanceSensor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGear getGear() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setState(RoboState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RoboState getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMyLightSensor getLeftLightSensor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMyLightSensor getRightLightSensor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColorSensor getColorSensor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bindParkourToRobot(Parkour parkour) {
		// TODO Auto-generated method stub
		
	}
}
