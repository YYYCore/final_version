package urban_robot_controller.logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class LogReader.
 */
public class LogReader {

	/** The command list. */
	private ArrayList<String> commandList = new ArrayList<String>();

	/** The config list. */
	private ArrayList<String> configList = new ArrayList<String>();

	/** The filepath. */
	private File filepath;

	/** The config. */
	private String [] config;

	/** The str line. */
	private String strLine;

	/** The end file. */
	private boolean endFile = false;

	/** The time. */
	String time;

	/**
	 * Start.
	 *
	 * @throws FileNotFoundException the file not found exception
	 */
	public void start() throws FileNotFoundException {

		FileInputStream fstream = new FileInputStream(filepath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		try {
			while ((strLine = br.readLine()) != null) {

				if(strLine.contains("[INFO]")) {
					config = strLine.split("-");
					configList.add(config[1]);
					continue;

				} else if(strLine.contains("[CONFIG]")) {
					config = strLine.split("-");
					configList.add(config[1]);

					if(config[1].contains("Time")){
						configList.remove(configList.size() - 1);
						config = config[1].replaceAll("\\s", "").split(":");
						GetDate date = new GetDate();
						String time = date.getDate(Long.parseLong(config[1]));
						time = endFile ? "End time: " + time : "Start time: " + time;
						configList.add(time);
					}

					if(config[1].contains("####################")){					
						endFile = true;
					}
					continue;
				}

				commandList.add(strLine);
			}


			br.close();
		} catch (IOException e) {
			System.out.println(e.getCause());
		}
	}

	/**
	 * Gets the commands.
	 *
	 * @return the commands
	 */
	public ArrayList<String> getCommands(){
		return commandList;
	}

	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	public ArrayList<String> getConfig(){
		return configList;
	}

	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file) {
		this.filepath = file;
	}

}