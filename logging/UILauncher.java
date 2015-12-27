package urban_robot_controller.logging;

import java.io.FileNotFoundException;


public class UILauncher implements Runnable{


	public static void main(String[] args) throws FileNotFoundException {

		SelectionMode blub = new SelectionMode();


		while(blub.getMode() == null){
		}

		switch(blub.getMode()){

		case "online":
			new OnlineMode();
			break;

		case "offline":
			try {
				new OfflineMode();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void run() {
		new OnlineMode();		
	}

}
