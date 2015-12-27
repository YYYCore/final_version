package urban_robot_controller.procedures.crossroad;

public enum CrossRoad {
	A, B;
	
	public boolean isA() {
		return this.equals(A);
	}
	
	public boolean isB() {
		return this.equals(B);
	}
}
