package urban_robot_controller.my_distance_sensor;

import ch.aplu.ev3.GenericGear;

public interface IGear {
	public GenericGear backward();
	
	public GenericGear backward(int duration);
	
	public GenericGear forward();
	
	public GenericGear forward(int duration);
	
	public GenericGear leftArc(double radius);
	
	public GenericGear leftArc(double radius, int duration);
	
	public GenericGear rightArc(double raidus);
	
	public GenericGear rightArc(double radius, int duration);
	
	public GenericGear left();
	
	public GenericGear left(int duration);
	
	public GenericGear right();
	
	public GenericGear right(int duration);

	public GenericGear setSpeed(int speed);
	
	public int getSpeed();
	
	public double getVelocity();
	
	public GenericGear stop();
	
	public int getLeftMotorCount();
	
	public int getRightMotorCount();
}
