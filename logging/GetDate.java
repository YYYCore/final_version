package urban_robot_controller.logging;

import java.util.Calendar;

// TODO: Auto-generated Javadoc
/**
 * The Class GetDate.
 * @author Mat
 */
public class GetDate {

	/**
	 * Gets the date.
	 *
	 * @param timestamp the timestamp
	 * @return the date
	 */
	public String getDate(long timestamp) { 
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);

		int mYear = calendar.get(Calendar.YEAR);
		int mMonth = calendar.get(Calendar.MONTH);
		int mDay = calendar.get(Calendar.DAY_OF_MONTH);
		String mTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
		
		return mDay + "." + mMonth + "." + mYear + " - " +mTime;
	}
}
