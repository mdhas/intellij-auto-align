package aligner;

import aligner.utils.StringUtils;

import java.util.*;

public class Aligner {
  public final AlignerOptions options;

  public Aligner(AlignerOptions options) {
    this.options = options;
  }

  public String align(String string) {
    Lines lines = new Lines(string, options);
    lines.process();
    lines.align();
    return lines.getResult();
  }

  // INTERNAL CLASSES
  private static class Lines {
    final String rawLinesString;
    final AlignerOptions options;

    // processed info
    List<Line> lines;
    String result;
    int delimetersCount;
    int furthestDelimeterIndex;


    Lines(String rawLines, AlignerOptions options) {
      this.rawLinesString            = rawLines;
      this.options                   = options;
      this.delimetersCount           = 0;
      this.furthestDelimeterIndex    = -1;
      this.lines                     = new ArrayList<>();
    }

    void process() {
      List<String> rawLines         = Arrays.asList(rawLinesString.split(System.lineSeparator()));

      for (String rawLine : rawLines) {
        Line line = new Line(rawLine, options);
        line.process();

        if (line.hasDelimeter()) {
          delimetersCount++;
        }

        furthestDelimeterIndex = Math.max(line.indexOfDelimeter(), furthestDelimeterIndex);
        lines.add(line);
      }
    }

    void align() {
      // dont bother aligning lines where only one line has a delimeter
      if (!hasMoreThanOneDelimeter()) {
        result = rawLinesString;
        return;
      }

      StringBuilder resultBuilder = new StringBuilder();
      int size = lines.size();

      for (int i=0; i < size; ++i) {
        Line line = lines.get(i);
        line.align(furthestDelimeterIndex);
        resultBuilder.append(line.getResult());
        resultBuilder.append((i != (size-1)) ? System.lineSeparator() : "");
      }

      result = resultBuilder.toString();
    }

    String getResult() {
      return result;
    }

    boolean hasMoreThanOneDelimeter() {
      return delimetersCount > 1;
    }
  }

  private static class Line {
    final String rawLine;
    final AlignerOptions options;

    // processed items
    LineParts lineParts;
    List<Quotation> quotations;
    List<Delimeter> delimeters;
    String result;

    Line(String string, AlignerOptions options) {
      rawLine = string;
      this.options = options;
    }

    boolean hasDelimeter() {
      return indexOfDelimeter() >= 0;
    }

    int indexOfDelimeter() {
      if (lineParts == null) {
        return -1;
      }

      return lineParts.indexOfDelimeter();
    }

    void process() {
      if (!options.shouldProccessLine(rawLine)) {
        return;
      }

      processQuotations();
      processDelimeters();
      processLineParts();
    }

    void processQuotations() {
      quotations = Quotation.findAll(rawLine);
    }

    void processDelimeters() {
      delimeters = Delimeter.findAll(rawLine, options.delimeters());
    }

    void processLineParts() {
      lineParts = LineParts.process(rawLine, delimeters, quotations);
    }

    void align(int furthestIndex) {
      if (!hasDelimeter()) {
        result = rawLine;
        return;
      }

      String delimeter           = lineParts.delimeter();
      String leftOfDeilmeter     = lineParts.leftSide();
      String rightOfDelimeter    = lineParts.rightSide();


      int targetIndex            = furthestIndex + options.padding();
      int padding                = targetIndex - lineParts.indexOfDelimeter();

      // recompose string
      result = new StringBuilder()
          .append(leftOfDeilmeter)
          .append(StringUtils.repeat(' ', padding))
          .append(delimeter)
          .append(" ")
          .append(rightOfDelimeter)
          .toString();
    }

    String getResult() {
      return result;
    }
  }

  private static class LineParts {
    final String delimeter;
    final String left;
    final String right;

    LineParts(String delimeter, String left, String right) {
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

    String delimeter() {
      return delimeter;
    }

    static String processLeft(String rawLine, String delimeter, int indexOfDelimeter) {
      return StringUtils.removeWhiteSpaceFromEnd(rawLine.substring(0, indexOfDelimeter));
    }

    static String processRight(String rawLine, String delimeter, int indexOfDelimeter) {
      return rawLine.substring(indexOfDelimeter + (delimeter.length())).trim();
    }

    static LineParts process(String rawLine, List<Delimeter> delimeters, List<Quotation> quotations) {
      if (delimeters == null) {
        return null;
      }

      for (Delimeter delimeter : delimeters) {
        if (delimeter.isBetweenAny(quotations)) {
          continue;
        }

        String leftOfDelimeter = LineParts.processLeft(rawLine, delimeter.value, delimeter.index);
        String rightOfDelimeter = LineParts.processRight(rawLine, delimeter.value, delimeter.index);

        return new LineParts(delimeter.value, leftOfDelimeter, rightOfDelimeter);
      }

      return null;
    }
  }

  private static class Quotation {
    final char value;
    final int start;
    final int end;

    Quotation(char value, int start, int end) {
      this.value = value;
      this.start = start;
      this.end = end;
    }

    boolean hasClosingQuote() {
      return end != -1;
    }

    int nextStartIndex() {
      return end + 1;
    }

    static List<Quotation> findAll(String rawLine) {
      List<Quotation> results    = null;
      Quotation quotation        = findNext(rawLine, null);

      while (quotation != null) {
        if (results == null) {
          results = new ArrayList<>();
        }

        results.add(quotation);

        if (!quotation.hasClosingQuote() || quotation.nextStartIndex() >= rawLine.length()-1) {
          quotation = null;
        }

        else {
          quotation = findNext(rawLine.substring(quotation.nextStartIndex()), quotation);
        }
      }

      return results;
    }

    static Quotation findNext(String rawLine, Quotation previousQuotation) {
      Quotation openQuote        = findQuote(rawLine, null);

      if (openQuote == null) {
        return null;
      }

      Quotation closingQuote     = findQuote(rawLine.substring(openQuote.start + 1), openQuote.value);

      if (closingQuote == null) {
        return openQuote;
      }

      int startIndex    = openQuote.start;
      int endIndex      = (openQuote.start + closingQuote.start + 1);
      if (previousQuotation != null) {
        startIndex      += previousQuotation.nextStartIndex();
        endIndex        += previousQuotation.nextStartIndex();
      }

      return new Quotation(openQuote.value, startIndex, endIndex);
    }

    static Quotation findQuote(String string, Character characterToFind) {
      int size = string.length();

      for (int i=0; i < size; ++i) {
        char character = string.charAt(i);

        if (characterToFind != null) {
          if (characterToFind == character) {
            return new Quotation(character, i, -1);
          }
        }

        else if (character == '\'' || character == '\"') {
          return new Quotation(character, i, -1);
        }
      }

      return null;
    }
  }

  private static class Delimeter {
    final String value;
    final int index;

    Delimeter(String value, int index) {
      this.value = value;
      this.index = index;
    }

    int nextStartIndex() {
      return index + value.length() + 1;
    }

    boolean isBetween(Quotation quotation) {
      if (index > quotation.start && index < quotation.end) {
        return true;
      }

      if (index > quotation.start && !quotation.hasClosingQuote()) {
        return true;
      }

      return false;
    }

    boolean isBetweenAny(List<Quotation> quotations) {
      if (quotations == null) {
        return false;
      }

      for (Quotation quotation : quotations) {
        if (isBetween(quotation)) {
          return true;
        }
      }

      return false;
    }

    static List<Delimeter> findAll(String rawLine, String[] delimeters) {
      List<Delimeter> results = null;

      for (String delimeter : delimeters) {
        // there may be multiple locations of this delimeter in the rawLine, so find them all
        List<Delimeter> delimetersFound = findAll(rawLine, delimeter);

        if (delimetersFound != null) {
          if (results == null) {
            results = new ArrayList<>();
          }

          results.addAll(delimetersFound);
        }
      }

      if (results != null) {
        results.sort((d1, d2) -> (d1.index - d2.index));
      }

      return results;
    }

    static List<Delimeter> findAll(String rawLine, String delimeterString) {
      List<Delimeter> results = null;


      Delimeter delimeter = findNext(rawLine, delimeterString, null);

      while (delimeter != null) {
        if (results == null) {
          results = new ArrayList<>();
        }

        results.add(delimeter);

        int nextIndex = delimeter.nextStartIndex();

        if (nextIndex >= rawLine.length()-1) {
          delimeter = null;
        }

        else {
          delimeter = findNext(rawLine.substring(nextIndex), delimeterString,  delimeter);
        }
      }

      return results;
    }

    static Delimeter findNext(String string, String delimeterString, Delimeter prevDelimeter) {
      int indexOfDelimeter = string.indexOf(delimeterString);

      if (indexOfDelimeter == -1) {
        return null;
      }

      if (isDoubleColon(string, delimeterString, indexOfDelimeter)) {
        return null;
      }

      if (prevDelimeter != null) {
        indexOfDelimeter += prevDelimeter.nextStartIndex();
      }

      return new Delimeter(delimeterString, indexOfDelimeter);
    }

    static boolean isDoubleColon(String string, String delimeter, int indexOfDelimeter) {
      if (!":".equals(delimeter)) {
        return false;
      }

      if (indexOfDelimeter >= string.length()) {
        return false;
      }

      return string.charAt(indexOfDelimeter+1) == ':';
    }
  }
}

