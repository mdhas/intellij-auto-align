package aligner;

import aligner.utils.Pair;
import aligner.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Aligner {
  public final AlignerOptions options;

  public Aligner(AlignerOptions options) {
    this.options = options;
  }

  public String align(String string) {
    List<String> rawLines         = Arrays.asList(string.split(System.lineSeparator()));
    List<Line> lines              = new ArrayList<>();

    int furthestDelimeterIndex    = -1;
    int delimetersCount           = 0;

    // process the rawLine
    for (String rawLine : rawLines) {
      Line line = new Line(rawLine);
      line.process(options);

      if (line.hasDelimeter()) {
        delimetersCount++;
      }

      furthestDelimeterIndex = Math.max(line.indexOfDelimeter(), furthestDelimeterIndex);
      lines.add(line);
    }

    // none or 1 delimeters found, no need to process the rest
    if (delimetersCount <= 1) {
      return string;
    }


    Line lastLine = lines.get(lines.size()-1);
    StringBuilder result = new StringBuilder();
    for (Line line : lines) {
      result.append(line.align(options, furthestDelimeterIndex));

      if (line != lastLine) {
        result.append(System.lineSeparator());
      }
    }

    return result.toString();
  }

  private static class Line {
    private String rawLine;
    private LineParts lineParts;

    Line(String string) {
      rawLine = string;
    }

    public boolean hasDelimeter() {
      return indexOfDelimeter() >= 0;
    }

    public int indexOfDelimeter() {
      if (lineParts == null) {
        return -1;
      }

      return lineParts.indexOfDelimeter();
    }

    public void process(AlignerOptions options) {
      if (!options.shouldProccessLine(rawLine)) {
        return;
      }

      // find the delimeter and the index of it
      Pair<Integer, String> delimeterVals = StringUtils.findFirst(rawLine, options.delimeters());

      if (delimeterVals == null) {
        return;
      }

      int indexOfDelimeter       = delimeterVals.getKey();
      String delimeter           = delimeterVals.getValue();
      String leftOfDelimeter     = LineParts.processLeft(rawLine, delimeter, indexOfDelimeter);
      String rightOfDelimeter    = LineParts.processRight(rawLine, delimeter, indexOfDelimeter);

      // save the values
      lineParts = new LineParts(delimeter, leftOfDelimeter, rightOfDelimeter);
    }

    public String align(AlignerOptions options, int furthestIndex) {
      if (!hasDelimeter()) {
        return rawLine;
      }

      String delimeter           = lineParts.delimeter;
      String leftOfDeilmeter     = lineParts.leftSide();
      String rightOfDelimeter    = lineParts.rightSide();


      int targetIndex            = furthestIndex + options.padding();
      int padding                = targetIndex - lineParts.indexOfDelimeter();


      // recompose string
      return new StringBuilder()
        .append(leftOfDeilmeter)
        .append(StringUtils.repeat(' ', padding))
        .append(delimeter)
        .append(" ")
        .append(rightOfDelimeter)
        .toString();
    }
  }

  private static class LineParts {
    private final String delimeter;
    private final String left;
    private final String right;

    private LineParts(String delimeter, String left, String right) {
      this.delimeter = delimeter;
      this.left = left;
      this.right = right;
    }

    // the delimeter should be placed right after the left side since its split like : left + delimeter + right
    int indexOfDelimeter() {
      return leftSide().length();
    }

    String leftSide() {
      return left;
    }
    String rightSide() {
      return right;
    }

    private static String processLeft(String rawLine, String delimeter, int indexOfDelimeter) {
      return StringUtils.removeWhiteSpaceFromEnd(rawLine.substring(0, indexOfDelimeter));
    }

    private static String processRight(String rawLine, String delimeter, int indexOfDelimeter) {
      return rawLine.substring(indexOfDelimeter+(delimeter.length())).trim();
    }
  }
}

