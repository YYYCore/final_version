package urban_robot_controller.procedures;

import urban_robot_controller.my_distance_sensor.IGear;
import urban_robot_controller.my_distance_sensor.IMyLightSensor;
import urban_robot_controller.robot_utility.IRobot;
import ch.aplu.ev3.Gear;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.LightSensor;
import ch.aplu.ev3.SensorPort;

public class LineFollower implements Runnable{
	public static final double VARIANCE = 1.03;
	public static final double CONST_P = 57;				//varP by Trial&Error
	public static final double CONST_I = 5;					//varI ~~ 1.2*Kc*(dT)/OscPeriod
	public static final double CONST_D = 0.0002;			//varD = 0.6*Kc*OscPeriod/8dT	
	public static final double MAX_TURN = 0.06;

	public direction direct = direction.forward;
	
	
	private IMyLightSensor lls;
	private IMyLightSensor rls;
	private IRobot robot;
	private IGear gear;
	private int rlsvalue;
	private int llsvalue;
	private FollowerData data;

	public LineFollower(IRobot robot) {
		data = new FollowerData();
		this.robot = robot;
		
		lls = robot.getLeftLightSensor();
		rls = robot.getRightLightSensor();
		gear = robot.getGear();
	}
	
	public void startLineFollower() {
		gear.setSpeed(20);
		gear.forward();
		
		while (true) {
			llsvalue = getAvgValue(lls);
			rlsvalue = getAvgValue(rls);															//Get Value of Lightsensor
			if (llsvalue > 400 && llsvalue < 1000 && rlsvalue > 400 && rlsvalue < 1000){			//Check if "on line"
				direct = direction.forward;
			}
			if (isDriveRight()) {
				driveRight();
				if (rlsvalue < 150) {
					direct = direction.right;														//Drive right
				}
			} else if (isDriveLeft()) {
				driveLeft();
				if (llsvalue < 150){
					direct = direction.left;														//Drive left
				};
			}
			else {
				reactToOvershoot();
			}	
		}
	}
	
	private void reactToOvershoot() {
		if (isOvershootRight())	{																	//If driven right & lost Line
			while(true){
				gear.rightArc(MAX_TURN);															//Turn MAX Right
				gear.rightArc(0.05);			
				if (getAvgValue(lls) < 800) {
					direct = direction.forward;
					break;
			}
			}				
		}
		else if (isOvershootLeft())	{																//If driven left & lost Line
			while(true) {
				gear.leftArc(MAX_TURN);																//Turn MAX Left
				gear.leftArc(0.05);				
				if (getAvgValue(rls) < 800) 														//On Line?
					direct = direction.forward;														//Drive forward
					break;
				}
			}
		}
		gear.forward();
		data.setIntegral(0);
	}

	private void driveLeft() {
		data = new FollowerData();
		data.setError(rlsvalue - llsvalue);
		data.setDerivative(data.getLastError() - data.getError());		
		data.setIntegral(data.getIntegral() + data.getError());
		gear.leftArc(data.getTurn());
		data.setLastError(data.getError());
	}

	private void driveRight() {
		data = new FollowerData();
		data.setError(llsvalue - rlsvalue);
		data.setDerivative(data.getLastError()- data.getError());	
		data.setIntegral(data.getIntegral() + data.getError());
		gear.rightArc(data.getTurn());		
		data.setLastError(data.getError());
	}

	private boolean isOvershootLeft() {
		return (direct == direction.left);
	}

	private boolean isOvershootRight() {
		return (direct == direction.right);
	}

	private boolean isDriveLeft() {
		return rlsvalue > VARIANCE * llsvalue;
	}
	
	public boolean isDriveRight() {
		return llsvalue > VARIANCE * rlsvalue;
	}

	public static int calibrate(int ls1value, int ls2value){
		 return Math.round((ls1value + ls2value) / 2);
	}
	
	private int getAvgValue(IMyLightSensor ls) {	
		return ls.getValue();
	}
	
	private class FollowerData {
		private double turn = 0;
		
		public double getTurn() {
			turn = (CONST_P * (1/data.getError()))											//Calculate Error -> Turn
			- (CONST_I * (1/data.getIntegral()))											//Correct by last Errors
			+ (CONST_D * (data.getDerivative()));											//Correct by predicted Error
			
			if (turn < MAX_TURN || data.getDerivative()> 15*data.getError()) { 
				turn = MAX_TURN;
			}
			return turn;
		}
		public double getError() {
			if (error == 0) {
				error = 1;
			}
			return error;
		}
		public void setError(double error) {
			if (error == 0) {
				error = 1;
			}
			this.error = error;
		}
		public double getIntegral() {
			return integral;
		}
		public void setIntegral(double integral) {
			this.integral = integral;
		}
		public double getDerivative() {
			return derivative;
		}
		public void setDerivative(double derivative) {
			this.derivative = derivative;
		}
		public double getLastError() {
			return lastError;
		}
		public void setLastError(double lastError) {
			this.lastError = lastError;
		}
		private double error = 0;					
		private double integral = 0;														//Sum of all previous Errors
		private double derivative = 0;														//Next Error 
		private double lastError = 0;														//Last Error
	}
	
	public enum direction {	
		left, forward, right;		
	}

	@Override
	public void run() {
		startLineFollower();
	}
}