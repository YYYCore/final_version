package urban_robot_controller.robot_utility;

import java.util.LinkedHashMap;
import java.util.Map;

import urban_robot_controller.actions.routeRecorder.RunLabyrinth;
import urban_robot_controller.procedures.Barcodescanner;
import urban_robot_controller.procedures.LineFollower;
import urban_robot_controller.procedures.PassCrossRoad;
import urban_robot_controller.procedures.crossroad.CrossRoad;
import urban_robot_controller.procedures.object_recognition.local_sign_detection.TrafficSignDetectionLauncher;
import urban_robot_controller.procedures.object_recognition.remote_sign_detection.TrafficSignServer;
import urban_robot_controller.ui.CrossroadUi;
import urban_robot_controller.ui.RobotUi;

@SuppressWarnings("deprecation")
public class RobotData {
private static final String TRAFFIC_SIGN_SERVER = "traffic-sign-server";
//	private static final Logger LOG = Logger.getLogger(RobotData.class.getName());
//	private static final String PARALLEL_METHOD = "parallel_run";
//	private static final String DIRECTION_DECISSION_METHOD = "direction_run";
	private static final String PASS_INTERSECTION = "PassIntersectionThread";
	private static final String E6E7_LABYRINTH_RUNNER = "labyrinthThread";
	private static final String LINE_FOLLOWER = "lineFollowerThread";
	private static final String BARCODE_SCANNER = "barcodescannerThread";
	private static final String TRAFFIC_SIGN_DETECTION = "trafficSignDetectionThread";
	private static Map<String, Thread> threads = new LinkedHashMap<String, Thread>();
	private static Map<IRobot, RobotUi> uiMap = new LinkedHashMap<IRobot, RobotUi>();;
	private static CrossroadUi crossRoadUi;
	
	private RobotData() {
	}
	
	public static void startUiForRobot(IRobot robot) {
		RobotUi robotUi = new RobotUi(robot.toString(), true);
		uiMap.put(robot, robotUi);
		new Thread(robotUi).start();
	}
	
	public static void startCrossRoadUi() {
		crossRoadUi = new CrossroadUi();
		new Thread(crossRoadUi).start();
	}
	
	public static CrossroadUi getCrossroadUi() {
		return crossRoadUi;
	}
	
	public static RobotUi getRobotUi(IRobot robot) {
		RobotUi ui = uiMap.get(robot);
		if(ui != null) {
			return ui;
		}
		return new RobotUi("",false);
	}
	
	public static boolean isLineFollowerRunning(IRobot robot) {
		Thread t = threads.get(LINE_FOLLOWER + robot);
		if(t == null) {
			System.out.println("no linefollower");
			return false;
		} 
		return true;
	}
	
	public static void startLineFollower(IRobot robot) {
		stopLineFollower(robot);
		Thread lineFollowerThread = new Thread(new LineFollower(robot));
		lineFollowerThread.setPriority(10);
		threads.put(LINE_FOLLOWER+robot, lineFollowerThread);
		lineFollowerThread.start();
	}
	
	public static void pauseLineFollower(IRobot robot) {
		Thread thread = threads.get(LINE_FOLLOWER+robot);
		if(thread != null) {
			thread.suspend();
		}
	}
	
	public static void resumLineFollower(IRobot robot) {
		Thread thread = threads.get(LINE_FOLLOWER+robot);
		if(thread != null) {
			thread.resume();
		}
	}
	
	public static void stopLineFollower(IRobot robot) {
		Thread oldThread = threads.get(LINE_FOLLOWER+robot);
		if(oldThread != null && oldThread.isAlive()) {
			oldThread.stop();
			threads.remove(LINE_FOLLOWER + robot);
		}
	}
	
	public static void startLabyrinth() {
		stopLabyrinth();
		Thread labyrinthThread = new Thread(new RunLabyrinth());
		threads.put(E6E7_LABYRINTH_RUNNER, labyrinthThread);
		labyrinthThread.start();
	}

	public static void stopLabyrinth() {
		Thread oldThread = threads.get(E6E7_LABYRINTH_RUNNER);
		if(oldThread !=null && oldThread.isAlive()) {
			oldThread.stop();
		}
	}
	
	public static void startBarcodescanner(IRobot robot) {
		stopBarcodescanner(robot);
		Thread barcodescannerThread = new Thread(new Barcodescanner(robot));
		threads.put(BARCODE_SCANNER + robot,barcodescannerThread);
		barcodescannerThread.start();
	}

	public static void stopBarcodescanner(IRobot robot) {
		Thread oldThread = threads.get(BARCODE_SCANNER + robot);
		if(oldThread != null && oldThread.isAlive()) {
			oldThread.stop();
		}
	}
	
	public static void startTrafficSignDetection(IRobot robot, String ip) {
		stopTrafficSignDetection(robot);
		Thread signDetectionThread = new Thread(new TrafficSignDetectionLauncher(robot, ip));
		threads.put(TRAFFIC_SIGN_DETECTION + robot, signDetectionThread);
		signDetectionThread.start();
	}

	public static void stopTrafficSignDetection(IRobot robot) {
		Thread oldThread = threads.get(TRAFFIC_SIGN_DETECTION);
		if(oldThread != null && oldThread.isAlive()) {
			oldThread.stop();
		}
	}
	
	public static void StartTrafficSignDetectionServer(int port) {
		stopTrafficSignDetectionServer();
		Thread trafficSignServer = new Thread(new TrafficSignServer(port));
		threads.put(TRAFFIC_SIGN_SERVER, trafficSignServer);
		trafficSignServer.start();
	}
	
	private static void stopTrafficSignDetectionServer() {
		Thread oldThread = threads.get(TRAFFIC_SIGN_SERVER);
		if(oldThread != null && oldThread.isAlive()) {
			oldThread.stop();
		}
	}

	public static void startPassCrossRoad(IRobot robot, CrossRoad crossRoad) {
		stopPassCrossRoad(robot);
		Thread PassCrossRoadThread = new Thread(new PassCrossRoad(robot, crossRoad));
		threads.put(PASS_INTERSECTION + robot, PassCrossRoadThread);
		PassCrossRoadThread.start();
	}
	
	public static void stopPassCrossRoad(IRobot robot) {
		Thread oldThread = threads.get(PASS_INTERSECTION + robot);
		if(oldThread != null && oldThread.isAlive()) {
			oldThread.stop();
		}
	}
	
}
