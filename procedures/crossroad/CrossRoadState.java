package urban_robot_controller.procedures.crossroad;


public enum CrossRoadState {
	Free, Blocked;

	public boolean isFree() {
		return this.equals(CrossRoadState.Free);
	}

	public boolean isBlocked() {
		return this.equals(CrossRoadState.Blocked);
	}
}
