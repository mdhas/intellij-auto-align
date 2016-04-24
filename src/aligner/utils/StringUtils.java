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

  public static String repeat(char c, int count) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i=0; i < count; ++i) {
      stringBuilder.append(c);
    }
    return stringBuilder.toString();
  }
}
