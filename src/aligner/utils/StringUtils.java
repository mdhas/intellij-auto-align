package aligner.utils;

public class StringUtils {
  private StringUtils() { }

  public static boolean isNoE(String string) {
    return string == null || "".equals(string.trim());
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


  public static Pair<Integer, String> findFirst(String string, String[] stringsList) {
    String foundString = null;
    int foundIndex = -1;
    int minIndex = Integer.MAX_VALUE;

    for (String stringToCheck : stringsList) {
      int indexOfDelimeter = string.indexOf(stringToCheck);
      if (indexOfDelimeter != -1 && indexOfDelimeter < minIndex) {
        minIndex = indexOfDelimeter;
        foundIndex = indexOfDelimeter;
        foundString = stringToCheck;
      }
    }

    if (foundIndex != -1) {
      return new Pair<>(foundIndex, foundString);
    }

    return null;
  }

  public static String repeat(char c, int count) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i=0; i < count; ++i) {
      stringBuilder.append(c);
    }
    return stringBuilder.toString();
  }
}
