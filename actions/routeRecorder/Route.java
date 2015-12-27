package urban_robot_controller.actions.routeRecorder;

import java.util.ArrayList;
import java.util.List;

import urban_robot_controller.actions.routeRecorder.Knot.Decision;
import urban_robot_controller.actions.routeRecorder.Knot.KnotDecision;
import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.RUT;
import urban_robot_controller.robot_utility.RobotProvider;
import ch.aplu.ev3.SensorPort;
import ch.aplu.ev3.UltrasonicListener;

public class Route {
	private List<Knot> knots;
	private IRobot robot;
	private int i = 0;
	private static Route route;
	
	private Route() {
		knots = new ArrayList<Knot>();
		robot = RobotProvider.getE6E7();
	}
	
	public static Route getInstance() {
		if(route != null) {
			return route;
		} 
		route = new Route();
		return route;
	}

	public void addKnot(Knot knot) {
		long timeAdded = 0;
		if(knots.size() > 0) {
			Knot latestKnot = knots.get(knots.size()-1);
			timeAdded = latestKnot.getTimeStamp();
		}
		long currentTime = System.currentTimeMillis();
		knot.setTimeToPreviousKnot(currentTime - timeAdded);
		getKnots().add(knot);
		
		System.out.println(knot.toString());
		
		knot.invokeKnot();
		knot.setTimeStamp(System.currentTimeMillis());
	}
	
	public void returnToLatestKnot() {
		if(knots.size() > 1) {
			Knot latestKnot = knots.get(knots.size()-1);
			Knot preLatestKnot = knots.get(knots.size()-2);
			
			latestKnot.invokeKnotReverse();
			robot.getGear().backward((int) latestKnot.getTimeToPreviousKnot() - 50);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			preLatestKnot.invokeKnotReverse();
			
			KnotDecision decisionMade = preLatestKnot.getKnotDecision();
			preLatestKnot.getOpportunities().remove(decisionMade);
			preLatestKnot.getOpportunities().put(decisionMade.getMethod(), false);
			preLatestKnot.setKnotDecision();
			
			preLatestKnot.invokeKnot();

			knots.remove(knots.size()-1);
		}
	}

	public List<Knot> getKnots() {
		return knots;
	}
	
	public void rerunRoute() {
		i = 0;
		robot.getFrontDistanceSensor().addUltrasonicListener(new UltrasonicListener() {
			@Override
			public void near(SensorPort port, int level) {
				knots.get(i).invokeKnot();
				i++;
			}
			@Override
			public void far(SensorPort port, int level) {
				
			}
		}, 10);
	}
	
	public void rerunRouteReverse() {
		i = knots.size() - 1;
		robot.getFrontDistanceSensor().addUltrasonicListener(new UltrasonicListener() {
			@Override
			public void near(SensorPort port, int level) {
				knots.get(i);
				i--;
			}
			
			@Override
			public void far(SensorPort port, int level) {
				
			}
		}, 10);
	}
	
	public void createDemoRoute() {
//		knots = new ArrayList<Knot>();
		
		for(int i = 0; i < 4; i++) {
			KnotDecision decision = new KnotDecision(Decision.Right, RUT.angle_to_ms(90));
			Knot knot = new Knot(decision);
			
			knots.add(knot);
		}
	}
	
	@Override
	public String toString() {
		int i = 0;
		String s = "";
		for(Knot knot : knots) {
			String knotInfo = knot.getKnotInfo().toString();
			String opportunities = knot.getOpportunities().toString();
			String decision = knot.getKnotDecision().getMethod().toString();
			s += "_______________ Knoten " + i + " ___________________\n";
			s += knotInfo + "\n";
			s += opportunities + "\n";
			s += "Richtung gefahren " + decision + "\n";
			s += " Zeit zum vorherigen Knoten " + knot.getTimeToPreviousKnot() + " [ms]\n";
			s += "____________________________________________\n\n\n";
			i++;
		}
		return s;
	}
}
