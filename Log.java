package lottery;

public class Log {

	private StringBuilder logger;
	private static Log instance;
	
	private Log() {
		this.logger = new StringBuilder();
	}
	
	//Retrieve a single instance of the logger per session
	public static Log getInstance() {
		if(Log.instance == null) {
			Log.instance = new Log();
		}
		return Log.instance;
	}
	
	//Log the input
	public void log(String input) {
		this.logger.append(input + "\n");
	}
	
	//Retrieve everything logged in the current session
	public String getLog() {
		if(this.logger.toString().isEmpty())
			return "The Logger is empty!";
		return this.logger.toString();
	}

}
