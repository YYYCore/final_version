package urban_robot_controller.robot_utility;

public enum RoboState {
	Maneuvering, Driving, Initial;


	public boolean isDriving() {
		return this.equals(Driving);
	}
	
	public boolean isManeuvering() {
		return this.equals(Maneuvering);
	}


}
