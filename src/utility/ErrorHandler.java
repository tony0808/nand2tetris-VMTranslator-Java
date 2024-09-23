package utility;

public class ErrorHandler {
	
	public static void exitErrorWithLineInfo(String errMsg, long errorLine) {
		System.err.println("Error at line " + errorLine + ": " + errMsg + ".");
		System.exit(1);
	}
}
