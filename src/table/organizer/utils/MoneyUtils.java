package table.organizer.utils;

public class MoneyUtils {
	public static String printPrice (int cents) {
		String cent;
		if (cents%100 < 10)
			cent = "0" + cents%100;
		else
			cent = "" + cents%100;
		String price = "$" + cents/100 + "." + cent;
		return price;
	}
}
