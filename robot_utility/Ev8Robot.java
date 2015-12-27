package urban_robot_controller.robot_utility;

import java.util.Observable;

import urban_robot_controller.actions.DecisionMaking;
import urban_robot_controller.actions.MakeSideShift;
import urban_robot_controller.actions.MakeSideShift.ShiftDirection;
import urban_robot_controller.my_distance_sensor.IMyDistanceSensor;
import urban_robot_controller.my_distance_sensor.IMyLightSensor;
import urban_robot_controller.my_distance_sensor.MyEOPDSensor;
import urban_robot_controller.my_distance_sensor.MyGear;
import urban_robot_controller.my_distance_sensor.MyUltrasonicSensor;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.SensorPort;
import ch.aplu.ev3.UltrasonicListener;
import ch.aplu.ev3.UltrasonicSensor;

public class Ev8Robot extends LegoRobot implements IRobot{
	private static final String EV8IP = "192.168.11.38";
//	private static final String EV8IP_BLUETOOTH = "10.0.1.1";
	private static final int COLLISION_TRIGGER_SIDE = 4;
	private static final int COLLISION_TRIGGER_FRONT = 9;
	private static Ev8Robot ev8 = null;
	protected MyGear gear;
	protected MyUltrasonicSensor usFront;
	protected MyEOPDSensor usRight;
	protected MyEOPDSensor usLeft;
	private RoboState state;
	private Parkour parkour;

	public static Ev8Robot getInstance() {
		if(ev8 == null) 
			return new Ev8Robot();
		else 
			return ev8;
	}
	
	private Ev8Robot() {
		super(EV8IP);
		ev8 = this;
		gear = new MyGear();
		usFront = new MyUltrasonicSensor (SensorPort.S1);
		usRight = new MyEOPDSensor (SensorPort.S3);
		usLeft= new MyEOPDSensor(SensorPort.S2);
		
		addPart(gear);
		addPart(usFront);
		addPart(usLeft);
		addPart(usRight);
		
		final Ev8Robot robot = Ev8Robot.getInstance();
		((UltrasonicSensor) robot.getFrontDistanceSensor()).addUltrasonicListener(new UltrasonicListener() {
			@Override
			public void near(SensorPort port, int level) {
//				RobotData.stopRunParallelToStructureThread();
//				RobotData.startDecideDirectionToDriveThread(new DecideDirectionToDrive(robot));
				if(state.isDriving()) {
					new DecisionMaking();
				}
			}
			
			@Override
			public void far(SensorPort port, int level) {
				
			}
		}, COLLISION_TRIGGER_FRONT);
		
//		usRight.addUltrasonicListener(new UltrasonicListener() {
//			@Override
//			public void near(SensorPort port, int level) {
//				new MakeSideShift(ShiftDirection.Left, 2).run();
//				System.out.println("aölsdkjföalsdkjf");
//			}
//			
//			@Override
//			public void far(SensorPort port, int level) {
//				
//			}
//		}, COLLISION_TRIGGER_SIDE);
//		
//		usLeft.addUltrasonicListener(new UltrasonicListener() {
//			@Override
//			public void near(SensorPort port, int level) {
//				new MakeSideShift(ShiftDirection.Right, 2).run();
//				System.out.println("aölsdkjföalsdkjf");
//			}
//			
//			@Override
//			public void far(SensorPort port, int level) {
//
//			}
//		}, COLLISION_TRIGGER_SIDE);
	}
	
	public MyUltrasonicSensor getFrontDistanceSensor() {
		return usFront;
	}
	
	public IMyDistanceSensor getLeftSensor() {
		return usLeft;
	}
	
	public IMyDistanceSensor getRightSensor() {
		return usRight;
	}
	
	public MyGear getGear() {
		return gear;
	}

	@Override
	public void setState(RoboState state) {
		this.state = state;
	}

	@Override
	public RoboState getState() {
		return state;
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
	public String toString() {
		return "E8";
	}

	@Override
	public ColorSensor getColorSensor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bindParkourToRobot(Parkour parkour) {
		this.parkour = parkour;
	}
}
