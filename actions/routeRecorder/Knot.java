package urban_robot_controller.actions.routeRecorder;

import java.util.Iterator;
import java.util.LinkedHashMap;

import urban_robot_controller.actions.MakeSideShiftBack;
import urban_robot_controller.actions.MakeSideShift.ShiftDirection;
import urban_robot_controller.robot_utility.IRobot;
import urban_robot_controller.robot_utility.RUT;
import urban_robot_controller.robot_utility.RoboState;
import urban_robot_controller.robot_utility.RobotProvider;


public class Knot {
	private static final int ALLOWED_DISTANCE = 10;
	private KnotInformation knotInfo;
	private KnotDecision knotDecision;
	private IRobot robot;
	private KnotOpportunities opportunities;
	private long timeStamp;
	private long timeToPreviousKnot;

	public Knot(KnotDecision decisionToMake) {
		this.robot = RobotProvider.getE6E7();
		this.knotDecision = decisionToMake;
		this.knotInfo = new KnotInformation();
		this.opportunities = new KnotOpportunities();
		robot = RobotProvider.getE6E7();
	}
	
	public Knot() {
		this.robot = RobotProvider.getE6E7();
		this.knotInfo= new KnotInformation();
		this.opportunities = new KnotOpportunities();
		setKnotDecision();
	}
	
	public void setKnotDecision() {
		Iterator<Decision> itr = opportunities.keySet().iterator();
		while(itr.hasNext()) {
			Decision nexOpportunity = itr.next();
			if(opportunities.get(nexOpportunity)) {
				this.knotDecision = new KnotDecision(nexOpportunity, RUT.angle_to_ms(90));
				return;
			}
		}
	}
	
	public KnotDecision getKnotDecision() {
		return knotDecision;
	}
	
	public KnotInformation getKnotInfo() {
		return knotInfo;
	}
	
	public KnotOpportunities getOpportunities() {
		return opportunities;
	}
	
	public void setTimeToPreviousKnot(long timeToPreviousKnot) {
		this.timeToPreviousKnot = timeToPreviousKnot;
	}
	
	public long getTimeToPreviousKnot() {
		return timeToPreviousKnot;
	}
	
	public void invokeKnot() {
		robot.getGear().stop();
		robot.setState(RoboState.Maneuvering);
		switch(knotDecision.getMethod()) {
			case Left: {
				int distance = robot.getRightSensor().getDistance();
				if(distance < 6) {
					new MakeSideShiftBack(ShiftDirection.Left, 3).run();
				}
				robot.getGear().left(knotDecision.getDuration());
				break;
			} 
			case Right: {
				int distance = robot.getLeftSensor().getDistance();
				if(distance < 6) {
					new MakeSideShiftBack(ShiftDirection.Right, 3).run();
				}
				robot.getGear().right(knotDecision.getDuration());
				break;
			}
			case LeftArc: {
				robot.getGear().leftArc(knotDecision.getRadius(), knotDecision.getDuration());
				break;
			}
			case RightArc: {
				robot.getGear().rightArc(knotDecision.getRadius(), knotDecision.getDuration());
				break;
			} 
			case Forward: {
				robot.getGear().forward();
				break;
			} 
			case LatestKnot: {
				Route.getInstance().returnToLatestKnot();
			}
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		timeStamp = System.currentTimeMillis();
		robot.getGear().forward();
		robot.setState(RoboState.Driving);
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public void invokeKnotReverse() {
		robot.getGear().stop();
		switch(knotDecision.getMethod()) {
			case Left: {
				robot.getGear().right(knotDecision.getDuration());
				break;
			} 
			case Right: {
				robot.getGear().left(knotDecision.getDuration());
				break;
			}
			case LeftArc: {
				robot.getGear().rightArc(knotDecision.getRadius(), knotDecision.getDuration());
				break;
			}
			case RightArc: {
				robot.getGear().leftArc(knotDecision.getRadius(), knotDecision.getDuration());
				break;
			} 
			case Forward: {
				robot.getGear().backward();
				break;
			} 
			case LatestKnot: {
				// Nothing to do
			}
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static class KnotDecision {
		private Decision method;
		private int ms;
		private double arc;

		public KnotDecision(Decision method, double arc, int ms) {
			this.method = method;
			this.arc = arc;
			this.ms = ms;
		}
		
		public KnotDecision(Decision method, int ms) {
			this.method = method;
			this.ms = ms;
		}
		
		public Decision getMethod() {
			return method;
		}
		
		public int getDuration() {
			return ms;
		}
		
		public double getRadius() {
			return arc;
		}
	}
	
	public enum Decision {
		Left, Right, LeftArc, RightArc, Forward, LatestKnot;
	}

	public class KnotInformation {
		int distanceFront;
		int distanceLeft;
		int distanceRight;
		
		private KnotInformation() {
			distanceFront = robot.getFrontDistanceSensor().getDistance();
			distanceLeft = robot.getLeftSensor().getDistance();
			distanceRight = robot.getRightSensor().getDistance();
		}
		
		public int getDistanceFront() {
			return distanceFront;
		}
		
		public int getDistanceLeft() {
			return distanceLeft;
		}
		
		public int getDistanceRight() {
			return distanceRight;
		}
		
		@Override
		public String toString() {
			String s = "";
			s += "                    " + distanceFront + "[cm]\n";
			s += distanceLeft + "[cm] <- links     vorne     rechts -> " + distanceRight + "[cm]\n";
			return s;
		}
	}
	
	public class KnotOpportunities extends LinkedHashMap<Decision, Boolean>{
		private static final long serialVersionUID = -7113463771025074531L;

		private KnotOpportunities() {
			
			int right = getKnotInfo().getDistanceRight();
			if(right < ALLOWED_DISTANCE && right > 0) {
				put(Decision.Right, false);
			} else {
				put(Decision.Right, true);
			}
			int left = getKnotInfo().getDistanceLeft();
			if(left < ALLOWED_DISTANCE && left > 0) {
				put(Decision.Left, false);
			} else {
				put(Decision.Left, true);
			}
			int front = getKnotInfo().getDistanceFront();
			if( front < ALLOWED_DISTANCE &&  front > 0) {
				put(Decision.Forward, false);
			} else {
				put(Decision.Forward, true);
			}
			if(!get(Decision.Forward) && !get(Decision.Right) && !get(Decision.Left)) {
				put(Decision.LatestKnot, true);
			}
		}
		
		@Override
		public String toString() {
			String s = "";
			Iterator<Decision> itr = keySet().iterator();
			while(itr.hasNext()) {
				Decision next = itr.next();
				boolean b = get(next);
				s += next.toString() + " " + b + "\n";
			}
			return s;
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		String knotInfo = getKnotInfo().toString();
		String opportunities = getOpportunities().toString();
		String decision = getKnotDecision().getMethod().toString();
		s += "_______________    Knoten    ________________\n";
		s += knotInfo + "\n";
		s += opportunities + "\n";
		s += "Richtung gefahren " + decision + "\n";
		s += "____________________________________________\n\n\n";
		return s;
	}
}