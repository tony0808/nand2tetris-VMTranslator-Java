package utility;

public class Utility {
	
	public static boolean isNumeric(String str) {
		try {
			Integer.valueOf(str);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
}