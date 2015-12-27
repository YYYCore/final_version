package urban_robot_controller.robot_utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;

import urban_robot_controller.my_distance_sensor.IGear;
import urban_robot_controller.my_distance_sensor.IMyDistanceSensor;
import urban_robot_controller.my_distance_sensor.IMyLightSensor;
import urban_robot_controller.my_distance_sensor.MyEOPDSensor;
import urban_robot_controller.my_distance_sensor.MyEOPDSensorRight;
import urban_robot_controller.my_distance_sensor.MyGear;
import urban_robot_controller.my_distance_sensor.MyLightSensor;
import urban_robot_controller.my_distance_sensor.MyUltrasonicSensor;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.Part;
import ch.aplu.ev3.SensorPort;
import ch.aplu.ev3.UltrasonicListener;

public class E6E7Robot implements IRobot{
	private static final String EV6IP = "192.168.11.36";
	private static final String EV7IP = "192.168.11.37";
	// private static final String EV8IP_BLUETOOTH = "10.0.1.1";
	private static final int COLLISION_TRIGGER_SIDE = 4;
	private static final int COLLISION_TRIGGER_FRONT = 9;
	private RoboState state;
	private LegoRobot ev6;
	private LegoRobot ev7;
	protected MyGear gear;
	protected IMyDistanceSensor frontDistanceSensor;
	protected IMyDistanceSensor rightDistanceSensor;
	protected IMyDistanceSensor leftDistanceSensor;
	private IMyLightSensor leftLightSensor;
	private IMyLightSensor rightLightSensor;
	private ColorSensor colorSensor;
	private Parkour parkour;
	private Class<?> parkourClazz;
	private Object parkourInstanz;
	private Method parkourMethod;

	private E6E7Robot() {
		state = RoboState.Driving;
		ev7 = new LegoRobot(EV7IP);
		ev6 = new LegoRobot(EV6IP);

		gear = new MyGear();

		frontDistanceSensor = new MyUltrasonicSensor(SensorPort.S1);
		
		leftDistanceSensor = new MyEOPDSensor(SensorPort.S2);
		rightDistanceSensor = new MyEOPDSensorRight(SensorPort.S3);

		leftLightSensor = new MyLightSensor(SensorPort.S2);
		rightLightSensor = new MyLightSensor(SensorPort.S1);

		colorSensor = new ColorSensor(SensorPort.S3);

		ev6.addPart((Part) frontDistanceSensor);
		ev6.addPart((Part) leftDistanceSensor);
		ev6.addPart((Part) rightDistanceSensor);

		ev7.addPart(gear);
		ev7.addPart((Part) leftLightSensor);
		ev7.addPart((Part) rightLightSensor);
		ev7.addPart((Part) colorSensor);

		frontDistanceSensor.addUltrasonicListener(new UltrasonicListener() {
			@Override
			public void near(SensorPort port, int level) {
				gear.stop();
				while(level <= RoboConfigs.EMERGENCY_DISTANCE) {
					level = frontDistanceSensor.getDistance();
					gear.stop();
				}
			}
			
			@Override
			public void far(SensorPort port, int level) {
			}
		}, RoboConfigs.EMERGENCY_DISTANCE);
	}

	private static IRobot robot = null;

	public static IRobot getInstance() {
		if (robot == null) {
			robot = new E6E7Robot();
		}
		return robot;
	}

	@Override
	public IMyDistanceSensor getLeftSensor() {
		return leftDistanceSensor;
	}

	@Override
	public IMyDistanceSensor getRightSensor() {
		return rightDistanceSensor;
	}

	@Override
	public IMyDistanceSensor getFrontDistanceSensor() {
		return frontDistanceSensor;
	}

	@Override
	public IGear getGear() {
		return gear;
	}

	@Override
	public IMyLightSensor getLeftLightSensor() {
		return leftLightSensor;
	}

	@Override
	public IMyLightSensor getRightLightSensor() {
		return rightLightSensor;
	}

	@Override
	public void setState(RoboState state) {
		this.state = state;
		System.out.println("---Robo State: " + state);

	}

	@Override
	public RoboState getState() {
		return state;
	}

	@Override
	public String toString() {
		return "E6E7";
	}

	@Override
	public ColorSensor getColorSensor() {
		return colorSensor;
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			if (parkour == null) {
				return;
			} else if(parkourMethod != null && parkourInstanz != null){
				parkourMethod.invoke(parkourInstanz, o, arg);
			} else {
				System.out.println("couldnt Invoke upadte");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void bindParkourToRobot(Parkour parkour) {
		this.parkour = parkour;
		try {
			parkourClazz = Class.forName(parkour.getClassToLoad());
		Class<?>[] args = new Class<?>[1];
		args[0] = IRobot.class;
		Constructor<?> constructor = parkourClazz.getConstructor(args);
		parkourInstanz = constructor.newInstance(this);
		Method[] methods = parkourClazz.getMethods();
		for (Method method : methods) {
			if (method.getName().equals("update")) {
				this.parkourMethod = method;
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}; 
	}
}
