package urban_robot_controller.my_distance_sensor;

import ch.aplu.ev3.SensorPort;

public class MyEOPDSensorRight extends MyEOPDSensor{

	public MyEOPDSensorRight(SensorPort port) {
		super(port);
	}
	
	@Override
	public int getDistance() {
		return getFineDistance() / 10;
	}
	
	@Override
	public int getFineDistance() {
		int x = super.getRawDistance();
		double a = (4001*Math.pow(x, 4)) / (35814240*Math.pow(10, 6));
		double b = (39619*Math.pow(x, 3)) / (255816 *Math.pow(10, 6));
		double c = (16907573*Math.pow(x, 2)) / (3581424 * Math.pow(10, 5));
		double d = (1793369 * x) / (149226*Math.pow(10, 3));
		double e = 16693 / 7480;
		
		int distance = (int) Math.round((a-b+c+d-e) * 10);
		if(distance > 95)
			return 100;
		if(distance < 15)
			return 1;
		
		return distance;
	}

}
