package urban_robot_controller.robot_utility;

import urban_robot_controller.procedures.crossroad.RouteOneObserver;
import urban_robot_controller.procedures.crossroad.RouteTwoObserver;

public enum Parkour {
	RouteOne, RouteTwo;

	private static final String ROUTE_ONE_CLAZZ = "RouteOneObserver";
	private static final String ROUTE_TWO_CLAZZ = "RouteTwoObserver";
	
	public String getClassToLoad() {
		if(isRouteOne()) {
			return RouteOneObserver.class.getName();
		} else if (isRouteTwo()) {
			return RouteTwoObserver.class.getName();
		} else {
			return "";
		}
	}
	
	public boolean isRouteOne() {
		return this.equals(RouteOne);
	}
	
	public boolean isRouteTwo() {
		return this.equals(RouteTwo);
	}
}
