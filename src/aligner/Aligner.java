package aligner;

import aligner.predicates.Predicates;
import aligner.utils.Pair;
import aligner.utils.StringUtils;

public class Aligner {
	public final AlignerOptions options;

	public Aligner(AlignerOptions options) {
		this.options = options;
	}

	public String align(String string) {
		String[] lineStrings    = string.split(System.lineSeparator());
		Line[] lines            = new Line[lineStrings.length];
		int size                = lineStrings.length;

		int furthestIndex           = -1;

		for (int i = 0; i < size; ++i) {
			Line line = preProcessLine(lineStrings[i]);
			lines[i] = line;

			if (line.indexOfDelimeter > furthestIndex) {
				furthestIndex = line.indexOfDelimeter;
			}
		}

 		int targetIndexForDelimeter     = furthestIndex + options.numSpacesForPadding;

		String output =  autoAlignLines(lines, targetIndexForDelimeter);

		return output;
	}

	private Line preProcessLine(String line) {

		// CHECK FOR IGNORED KEYWORDS
		if (!shouldProcessLine(line)) {
			return Line.empty(line);
		}

		Pair<Integer, String> delimeterVals     = StringUtils.find(line, options.delimeters);

		// DID NOT FIND A DELIMETER
		if (delimeterVals == null) {
			return Line.empty(line);
		}

		String delimeter                        = delimeterVals.getValue();
		int indexOfDelimeter                    = delimeterVals.getKey();

		// split the line by the delimeter
		String leftOfDelimeter                  = line.substring(0, indexOfDelimeter);
		String rightOfDelimeter                 = line.substring(indexOfDelimeter);

		// remove the whitespace from the end of the string
		leftOfDelimeter                         = StringUtils.removeWhiteSpaceFromEnd(leftOfDelimeter);
		rightOfDelimeter                        = rightOfDelimeter.trim();

		// update indexOfDelimeter after we remove the whitespace from the end of the string
		indexOfDelimeter = leftOfDelimeter.length();

		// CHECK IF THERE IS A SPACE AFTER DELIMETER, IF NOT ADD ONE
		if (!Character.isSpaceChar(rightOfDelimeter.charAt(delimeter.length()))) {
			rightOfDelimeter                    = StringUtils.insertChartAt(rightOfDelimeter, ' ', delimeter.length(), 1);
		}

		// collapse the string after removing whitespace ensuring there is a space after the delimeter
		String outString                         = leftOfDelimeter + rightOfDelimeter;

		return new Line(outString, delimeter, indexOfDelimeter);
	}

	private static String autoAlignLines(Line[] lines, int targetIndexForDelimeter) {
		StringBuilder result            = new StringBuilder();
		int size                        = lines.length;

		for (int i = 0; i < size; ++i) {
			Line line = lines[i];

			String alignedLine;

			if (line.indexOfDelimeter != -1) {
				int deltaToShift = targetIndexForDelimeter - line.indexOfDelimeter;
				alignedLine = StringUtils.insertChartAt(line.string, ' ', line.indexOfDelimeter, deltaToShift);
			}

			else {
				alignedLine = line.string;
			}


			result.append(alignedLine);

			if ((i+1) != size) {
				result.append(System.lineSeparator());
			}
		}

		return result.toString();
	}

	private boolean shouldProcessLine(String line) {
		return Predicates.and(options.predicatesList).test(line);
	}

	private static class Line {
		final String string;
		final String delimeter;
		final int indexOfDelimeter;

		private Line(String string, String delimeter, int indexOfDelimeter) {
			this.string = string;
			this.delimeter = delimeter;
			this.indexOfDelimeter = indexOfDelimeter;
		}

		static Line empty(String line) {
			return new Line(line, null, -1);
		}
	}

}

