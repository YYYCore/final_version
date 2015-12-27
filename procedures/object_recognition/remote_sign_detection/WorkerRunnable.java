package urban_robot_controller.procedures.object_recognition.remote_sign_detection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import urban_robot_controller.procedures.object_recognition.TrafficSign;
import urban_robot_controller.robot_utility.E6E7Robot;
import urban_robot_controller.robot_utility.Ev31Robot;

public class WorkerRunnable implements Runnable {

	protected Socket clientSocket = null;
	protected String serverText = null;

	public WorkerRunnable(Socket clientSocket, String serverText) {
		this.clientSocket = clientSocket;
		this.serverText = serverText;
	}

	public void run() {
		try {
			InputStream input = clientSocket.getInputStream();

			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			Message msg = new Message(inFromClient.readLine());
			processMessage(msg);

			OutputStream output = clientSocket.getOutputStream();
			long time = System.currentTimeMillis();
			output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: "
					+ this.serverText + " - " + time + "").getBytes());
			output.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processMessage(Message msg) {
		if(msg.isValid()) {
			Object[] arg = new Object[2];
			arg[0] = msg.getSign();
			arg[1] = msg.getDistance();
			if (msg.isE31()) {
				Ev31Robot.getInstance().update(null, arg);
			} else if (msg.isE6E7()) {
				E6E7Robot.getInstance().update(null, arg);
			}
		}
	}

	private class Message {
		private static final String E6E7 = "e6e7";
		private static final String E31 = "e31";
		private String message;
		private float distance;
		private String sign;

		public Message(String message) {
			this.message = message;
			String[] result = message.split(";");
			sign = result[1];
			distance = Float.parseFloat(result[2]);
		}

		public boolean isE31() {
			return message.contains(E31);
		}

		public boolean isE6E7() {
			return message.contains(E6E7);
		}

		public float getDistance() {
			return distance;
		}

		public String getSign() {
			return sign;
		}
		
		public boolean isValid() {
			boolean signValid = sign.equals(TrafficSign.Stop.name()) || sign.equals(TrafficSign.GiveWay.name()) || sign.equals(TrafficSign.RightOfWay.name());
			boolean distanceValid = distance != 0;
			boolean robotValid = isE31() || isE6E7();
			
			return signValid && distanceValid && robotValid;
		}
		
		@Override
		public String toString() {
			String robotName = "";
			if(isE31()) {
				robotName = "E31";
			} else {
				robotName = "E6E7";
			}
			return robotName + " detected: " + sign + ", in " + distance + " [cm]" ;
		}
	}

}