package urban_robot_controller.actions;

import urban_robot_controller.actions.routeRecorder.Knot;
import urban_robot_controller.actions.routeRecorder.Route;

public class DecisionMaking {

	public DecisionMaking() {
		Knot knot = new Knot();
		Route.getInstance().addKnot(knot);
//		knot.invokeKnot();
	
	}
	
}
