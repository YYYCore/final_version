package urban_robot_controller.my_distance_sensor;

import ch.aplu.ev3.DebugConsole;
import ch.aplu.ev3.EV3Properties;
import ch.aplu.ev3.HTEopdShortSensor;
import ch.aplu.ev3.LegoRobot;
import ch.aplu.ev3.SensorPort;
import ch.aplu.ev3.SharedConstants;
import ch.aplu.ev3.Tools;
import ch.aplu.ev3.UltrasonicListener;

public class MyEOPDSensor extends HTEopdShortSensor implements IMyDistanceSensor{
	private UltrasonicListener listener = null;
  private int state = SensorState.NEAR;
  private int triggerLevel;
  private int pollDelay;
  private volatile static boolean inCallback = false;
  private EOPDThread eopdThread;
	
  	public MyEOPDSensor(SensorPort port) {
  		super(port);
		eopdThread = new EOPDThread();
		EV3Properties props = LegoRobot.getProperties();
		pollDelay = props.getIntValue("UltrasonicSensorPollDelay");
	}
  
	@Override
	public int getDistance() {
		int distance = getFineDistance();
		return Math.round(distance / 10);
	}
	
	protected int getRawDistance() {
		return super.getDistance();
	}
	
	public int getFineDistance() {
		int x = super.getDistance();
		double a = (523*Math.pow(x, 3)) / 2120580000;
		double b = (943 * Math.pow(x, 2)) / 1893375;
		double c = (339217 * x) / 1009800;
		double d = 4340464 / 58905;
		 
		int distance = (int) Math.round((a-b+c-d) * 10);
		if(distance > 95)
			return 100;
		if(distance < 15)
			return 1;
		
		return distance;
	}

	public void addUltrasonicListener(UltrasonicListener listener,
			int triggerLevel) {
	    this.listener = listener;
	    this.triggerLevel = triggerLevel;
	    if (!eopdThread.isAlive())
	      startEopdThread();
	}

	private void startEopdThread() {
		eopdThread.start();
	}

	/**
	 * inner interface for EOPDListener 
	 */
	public interface EOPDListener extends UltrasonicListener {
		/**
		 * Called when the distance exceeds the trigger level.
		 * @param port the port where the sensor is plugged in
		 * @param level the current distance level
		 */
		public void far(SensorPort port, int level);

		/**
		 * Called when the distance falls below the trigger level.
		 * @param port the port where the sensor is plugged in
		 * @param level the current distance level
		 */
		public void near(SensorPort port, int level);
	}
	
	/**
	 * inner class for EOPD Thread to observe the distance and trigger Listener
	 */
	private class EOPDThread extends Thread {

	    private volatile boolean isRunning = false;

	    private EOPDThread()
	    {
	      if (LegoRobot.getDebugLevel() >= SharedConstants.DEBUG_LEVEL_LOW)
	        DebugConsole.show("UlTh created");
	    }

	    public void run()
	    {
	      if (LegoRobot.getDebugLevel() >= SharedConstants.DEBUG_LEVEL_LOW)
	        DebugConsole.show("UlTh started");

	      isRunning = true;
	      while (isRunning)
			if (listener != null)
	        {
	          Tools.delay(pollDelay);
	          int level = getLevel();
	          if (state == SensorState.NEAR && level > triggerLevel)
				if (inCallback)
	            {
	              if (LegoRobot.getDebugLevel() >= SharedConstants.DEBUG_LEVEL_LOW)
	                DebugConsole.show("Evt'far'(rej)");
	            }
	            else
	            {
	              inCallback = true;
	              if (LegoRobot.getDebugLevel() >= SharedConstants.DEBUG_LEVEL_LOW)
	                DebugConsole.show("Evt'far'(" + getPortLabel() + ")");
	              listener.far(getPort(), level);
	              state = SensorState.FAR;
	              inCallback = false;
	            }
	          if (state == SensorState.FAR && level <= triggerLevel)
				if (inCallback)
	            {
	              if (LegoRobot.getDebugLevel() >= SharedConstants.DEBUG_LEVEL_LOW)
	                DebugConsole.show("Evt'near'(rej)");
	            }
	            else
	            {
	              inCallback = true;
	              if (LegoRobot.getDebugLevel() >= SharedConstants.DEBUG_LEVEL_LOW)
	                DebugConsole.show("Evt'near'(" + getPortLabel() + ")");
	              listener.near(getPort(), level);
	              state = SensorState.NEAR;
	              inCallback = false;
	            }
	        }
	    }

	    private void stopThread()
	    {
	      isRunning = false;
	      try
	      {
	        join(500);
	      }
	      catch (InterruptedException ex)
	      {
	      }
	      if (LegoRobot.getDebugLevel() >= SharedConstants.DEBUG_LEVEL_LOW)
	        if (isAlive())
	          DebugConsole.show("UlTh stop failed");
	        else
	          DebugConsole.show("UlTh stop ok");
	    }
	  
	}

	private interface SensorState {// Simulate enum for J2ME compatibility
		int NEAR = 0;
		int FAR = 1;
	}

	private int getLevel() {
		if (robot == null || !robot.isConnected())
			return -1;
		return getValue(false);
	}
	
	private int getValue(boolean check) {
		if (check)
			checkConnect();
		int v = 0;
		try {
			v = getDistance();
		} catch (NumberFormatException ex) {
		}
		return v;
	}
	
	private void checkConnect() {
		if (robot == null)
			throw new RuntimeException("UltrasonicSensor (port: " + getPortLabel()
					+ ") is not a part of the EV3Robot.\n"
					+ "Call addPart() to assemble it.");
	}
	
	protected void cleanup() {
		if (LegoRobot.getDebugLevel() >= SharedConstants.DEBUG_LEVEL_MEDIUM)
			DebugConsole
					.show("DEBUG: UltrasonicSensor.cleanup() called (Port: "
							+ getPortLabel() + ")");
		if (eopdThread != null)
			eopdThread.stopThread();
	}
}
