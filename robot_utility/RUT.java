package urban_robot_controller.robot_utility;
//Robot Unit Transformation
public class RUT {

	public static int cm_to_ms(int cmToDrive) {
		double velocity = RobotProvider.getE6E7().getGear().getVelocity();
		double secondsToDrive = cmToDrive/velocity;
		double milliSecondsToDrive = secondsToDrive * 10;
		return (int) Math.round(milliSecondsToDrive);
	}
	
	public static int angle_to_ms(int between1and360) {
		//TODO dosnt work corectyl
		if(between1and360 < 1 || between1and360 > 360) {
			throw new RuntimeException("angle " + between1and360 + " out of scale");
		}
		int angle = between1and360;
		double speed = RobotProvider.getE6E7().getGear().getSpeed();
		double timeForFullCircle = (2500 / 30) * speed;
		double millisecondsForAngle = timeForFullCircle / 360 * angle;
		return (int) Math.round(millisecondsForAngle - 180);
	}

	public static float cameraDistance_mm(int cmDistance) {
		return cmDistance;
	}
	
	
}
