package lottery;

public class Log {

	private StringBuilder logger;
	private static Log instance;
	
	private Log() {
		this.logger = new StringBuilder();
	}
	
	public static Log getInstance() {
		if(Log.instance == null) {
			Log.instance = new Log();
		}
		return Log.instance;
	}
	
	public void log(String input) {
		this.logger.append(input + "\n");
	}
	
	public String getLog() {
		if(this.logger.toString().isEmpty())
			return "The Logger is empty!";
		return this.logger.toString();
	}

}
