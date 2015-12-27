package urban_robot_controller.actions;

import java.util.ArrayList;
import java.util.logging.Logger;

import urban_robot_controller.my_distance_sensor.IGear;
import urban_robot_controller.my_distance_sensor.MyEOPDSensor;
import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.RobotProvider;
import ch.aplu.ev3.Gear;

public class RunParallelToStructure implements Runnable{
	private static final Logger LOG = Logger.getLogger(RunParallelToStructure.class.getName());
	private static final int TO_FAR_AWAY = 80;
	private static final int Y_GROWTH_TRIGGER = 5;
	private static final double SCALE_CONSTANT = 0.4;
	private IRobot robot;
	private MyEOPDSensor sensorToObserve; 
	private static OrientationSide orientationSide;
	
	public RunParallelToStructure(OrientationSide sideToObserve) {
		this.robot = RobotProvider.getE6E7();
		setSideToObserve(sideToObserve);
	}
	
	public double startMeassure() throws InterruptedException {
		if(sensorToObserve == null) {
			LOG.severe("no sensor to observe, attached");
			return 0;
		}
		
		ArrayList<Integer> meassureProtocol = new ArrayList<Integer>();
		IGear gear = robot.getGear();
		gear.forward();
		int startDistance = sensorToObserve.getFineDistance();
		long meassureStart = System.currentTimeMillis();
		
		int yGrowth = 0;
		while(Math.abs(yGrowth) < Y_GROWTH_TRIGGER ) {
			for(int i = 0; i < 10 ; i++) {
				meassureProtocol.add(sensorToObserve.getFineDistance());
				Thread.sleep(1);
			}
			int sum = 0;
			for(Integer point : meassureProtocol) {
				sum = sum + point;
			}
			int avg = Math.round(sum / meassureProtocol.size());
			if(avg > TO_FAR_AWAY) {
				return 1;
			}
			yGrowth = startDistance - avg;
		}
		long meassureStop = System.currentTimeMillis();
		long timeSpend = meassureStop - meassureStart;
		double cmTravelled = gear.getVelocity() * (timeSpend / 10);
		double pitch = yGrowth / cmTravelled;
		return pitch;
	}

	@Override
	public void run() {
		try {
			runParallel();
		} catch (InterruptedException e) {
			LOG.info("Parallel Meassuring stoped");
		}
	}

	private void runParallel() throws InterruptedException {
		while(true) {
			if(robot.getState().isDriving()) {
				double pitch = startMeassure();
				System.out.println("pitch " + pitch);
				if(Math.abs(pitch) > 0.5 ){
					continue;
				} else {
					correctePath(pitch);
				}
			}
		}
	}
	
	private void correctePath(double pitch) {
		if(orientationSide == OrientationSide.Right) {
			orientToRightSide(pitch);
		} else {
			orientToLeftSide(pitch);
		}
	}
	
	private void orientToLeftSide(double pitch) {
		IGear gear = robot.getGear();
		long duration = Math.abs(Math.round(pitch * SCALE_CONSTANT * 1000));
		if(pitch >= 0) {
			gear.rightArc(0.3,(int) duration );
			gear.forward();
		} else {
			gear.leftArc(0.3, (int) duration);
			gear.forward();
		} 
	}

	private void orientToRightSide(double pitch) {
		IGear gear = robot.getGear();
		long duration = Math.abs(Math.round(pitch * SCALE_CONSTANT * 1000));
		if(pitch >= 0) {
			gear.leftArc(0.1,(int) duration );
			gear.forward();
		} else {
			gear.rightArc(0.1, (int) duration);
			gear.forward();
		} 
	}
	
	private void setSideToObserve(OrientationSide sideToObserve) {
		RunParallelToStructure.orientationSide = sideToObserve;
		if(sideToObserve == OrientationSide.Left) {
			this.sensorToObserve = (MyEOPDSensor) RobotProvider.getE6E7().getLeftSensor();
		} 
		if(sideToObserve == OrientationSide.Right) {
			this.sensorToObserve = (MyEOPDSensor) RobotProvider.getE6E7().getRightSensor();
		}
	}
	
	public enum OrientationSide {
		Left, Right
	}

	public static OrientationSide getOrientationSide() {
		return orientationSide;
	}
	
}



// Measure results needed to long and were less reliant

//public void startMeassure() throws InterruptedException {
//	Gear gear = robot.getGear();
//	gear.forward();
//	long meassureStart = System.currentTimeMillis();
//	long timeMeassured = 0;
//	while (timeMeassured < MEASSURE_DURATION) {
//		timeMeassured = System.currentTimeMillis() - meassureStart;
//		meassureProtocol.add(robot.getRightSensor().getFineDistance());
//		Thread.sleep(1);
//	}
//	double velocity = gear.getVelocity();
//	double cmTravelled = velocity * MEASSURE_DURATION / 10;
//	
//	Integer firstPoint = meassureProtocol.get(0);
//	int sum = 0;
//	for(Integer meassurePoint : meassureProtocol) {
//		sum = sum + meassurePoint;
//	}
//	gear.stop();
//	int avg = Math.round(sum/meassureProtocol.size());
//	int y = firstPoint - avg;
//	double pitch = y / cmTravelled;
//	System.out.println("start point" + firstPoint);
//	System.out.println("avg " + avg);
//	System.out.println("travelled " + cmTravelled);
//	System.out.println("computed pitch " + pitch);
//}