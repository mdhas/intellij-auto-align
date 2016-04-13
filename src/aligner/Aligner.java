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

      if (line.indexOfDelimeter() >= 0) {
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
    private DelimeterData delimeterData;

    Line(String string) {
      rawLine = string;
    }

    public int indexOfDelimeter() {
      if (delimeterData == null) {
        return -1;
      }

      // we use the left
      return delimeterData.leftOfDelimeter().length();
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

      String delimeter = delimeterVals.getValue();
      int indexOfDelimeter = delimeterVals.getKey();

      // save the values
      delimeterData = new DelimeterData(rawLine, delimeter, indexOfDelimeter);
    }

    public String align(AlignerOptions options, int furthestIndex) {
      int indexOfDelimeter = indexOfDelimeter();

      if (indexOfDelimeter == -1) {
        return rawLine;
      }

      String delimeter           = delimeterData.delimeter;
      String leftOfDeilmeter     = delimeterData.leftOfDelimeter();
      String rightOfDelimeter    = delimeterData.rightOfDelimeter();

      // recompose string
      String result              = leftOfDeilmeter + delimeter + " " + rightOfDelimeter;

      // add padding
      int targetIndex            = furthestIndex + options.getNumSpacesForPadding();
      int deltaToShift           = targetIndex - leftOfDeilmeter.length();
      result                     = StringUtils.insertChartAt(result, ' ', leftOfDeilmeter.length(), deltaToShift);

      return result;
    }
  }

  private static class DelimeterData {
    private final String rawLine;
    private final String delimeter;
    private final int indexOfDelimeter;

    private DelimeterData(String rawLine, String delimeter, int indexOfDelimeter) {
      this.rawLine = rawLine;
      this.delimeter = delimeter;
      this.indexOfDelimeter = indexOfDelimeter;
    }

    String leftOfDelimeter() {
      String leftOfDelemeter = rawLine.substring(0, indexOfDelimeter);
      return StringUtils.removeWhiteSpaceFromEnd(leftOfDelemeter);
    }
    String rightOfDelimeter() {
      return rawLine.substring(indexOfDelimeter+(delimeter.length())).trim();
    }
  }
}

