package aligner.utils;

public class StringUtils {
	private StringUtils() { }

	public static boolean isNoE(String string) {
		return string == null || "".equals(string.trim());
	}
	public static boolean startsWithAny(String string, String[] stringsList) {
		if (string == null) {
			return false;
		}

		if (string.length() == 0) {
			return false;
		}

		for (String stringToCheck : stringsList) {
			if (string.startsWith(stringToCheck)) {
				return true;
			}
		}

		return  false;
	}

	public static String removeWhiteSpaceFromEnd(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}

		int idx = string.length();

		while (idx > 0 && Character.isWhitespace(string.charAt(idx-1))) {
			--idx;
		}

		return string.substring(0, idx);
	}

	public static String insertChartAt(String string, char character, int index, int repeatNum) {
		StringBuilder sb = new StringBuilder();
		sb.append(string.substring(0,index));
		for (int i=0; i < repeatNum; ++i) {
			sb.append(character);
		}
		sb.append(string.substring(index));
		return sb.toString();
	}


	public static Pair<Integer, String> find(String string, String[] stringsList) {
		for (String stringToCheck : stringsList) {
			int indexOfDelimeter = string.indexOf(stringToCheck);
			if (indexOfDelimeter != -1) {
				return new Pair<>(indexOfDelimeter, stringToCheck);
			}
		}

		return null;
	}

}
