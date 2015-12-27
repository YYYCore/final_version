package urban_robot_controller.robot_utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;

import urban_robot_controller.my_distance_sensor.IGear;
import urban_robot_controller.my_distance_sensor.IMyDistanceSensor;
import urban_robot_controller.my_distance_sensor.IMyLightSensor;
import urban_robot_controller.my_distance_sensor.MyGear;
import urban_robot_controller.my_distance_sensor.MyIRDistanceSensor;
import urban_robot_controller.my_distance_sensor.MyLightSensor;
import ch.aplu.ev3.ColorSensor;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.SensorPort;
import ch.aplu.ev3.UltrasonicListener;

public class Ev31Robot extends LegoRobot implements IRobot {
	private static final String EV31IP = "192.168.11.31";
	private static Ev31Robot robot;
	private ColorSensor cls;
	private MyGear gear;
	private MyLightSensor leftLightSensor;
	private MyLightSensor rightLightSensor;
	private MyIRDistanceSensor irSensor;
	private Parkour parkour;
	private Class<?> parkourClazz;
	private Object parkourInstanz;
	private Method parkourMethod;

	private Ev31Robot() {
		super(EV31IP);
		robot = this;
		cls = new ColorSensor(SensorPort.S4);

		leftLightSensor = new MyLightSensor(SensorPort.S1);
		rightLightSensor = new MyLightSensor(SensorPort.S3);

		irSensor = new MyIRDistanceSensor(SensorPort.S2);
		irSensor.addUltrasonicListener(new UltrasonicListener() {
			@Override
			public void near(SensorPort port, int level) {
				gear.stop();
				while(level <= RoboConfigs.EMERGENCY_DISTANCE) {
					System.out.println("Emergency Stop");
					level = irSensor.getDistance();
					gear.stop();
				}
			}
			
			@Override
			public void far(SensorPort port, int level) {
			}
		}, RoboConfigs.EMERGENCY_DISTANCE);

		gear = new MyGear();
		robot.addPart(gear);
		robot.addPart(cls);
		robot.addPart(leftLightSensor);
		robot.addPart(rightLightSensor);
		robot.addPart(irSensor);
	}

	public static IRobot getInstance() {
		if (robot == null) {
			robot = new Ev31Robot();
		}
		return robot;
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
		return irSensor;
	}

	@Override
	public IGear getGear() {
		return gear;
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
		return leftLightSensor;
	}

	@Override
	public IMyLightSensor getRightLightSensor() {
		return rightLightSensor;
	}

	@Override
	public ColorSensor getColorSensor() {
		return cls;
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
	
	@Override
	public String toString() {
		return "EV31";
	}
}
