import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * GOALS
 *
 * + align code by any '=,:,+=,-=,*=,/=,%=,^=,|=,&=,<<=,>>=,>>>='
 * + ignore lines with statements for,while,do,if,else,switch
 * + ignore lines with keywords assert,abstract,class,goto,implements,interface,enum,package,private,public,protected,void,Void,return,super,synchronized,throw
 *
 *
 * STEPS
 * + align 2 lines
 * + enforce 1 space after delimeter
 * + add recognition for multiple operators
 * + ignore keywords
 * + no delimeters found
 *
 * ON HOLD
 * - detemine how to handle whitespace at beginning of the line
 *
 * */

public class AlignerTests {

	private final Aligner aligner = new Aligner(AlignerOptions.Default());
	private final String expected =	"var index    = 0;\n" +
									"j            = 1;";

	private void testAligner(String input, String expected) {
		String result = aligner.align(input);
		assertEquals(expected, result);
	}

	@Test
	public void canAlignTwoLinesBasedOnEquals() {
		String input 		=	"var index = 0;\n" +
								"j = 1;";
		testAligner(input, expected);
	}

	@Test
	public void canAlignTwoWhiteSpacedLines() {
		String input 		=	"var index          = 0;\n" +
								"j      = 1;";

		testAligner(input, expected);
	}

	@Test
	public void enforces1SpaceAfterDelimeter() {
		String input 		=	"var index=0;\n" +
								"j=1;";
		testAligner(input, expected);
	}

	@Test
	public void alignsMultipleOperators() {
		String inputTemplate		=	"var index{{OPERATOR}}0;\n" +
										"j {{OPERATOR}}1;";
        String expectedTemplate		=	"var index    {{OPERATOR}} 0;\n" +
										"j            {{OPERATOR}} 1;";

		for (String delimeter : aligner.options.delimeters) {
            String input        = inputTemplate.replace("{{OPERATOR}}", delimeter);
            String expected     = expectedTemplate.replace("{{OPERATOR}}", delimeter);
			testAligner(input, expected);
		}
	}

	@Test
	public void ignoresLinesThatStartWithKeywords() {
		String inputTemplate        = "{{KEYWORD}} (i=0;i<1;++i) { ";
		for (String keyword : aligner.options.keywordsToIgnore) {
			String input            = inputTemplate.replace("{{KEYWORD}}", keyword);
			testAligner(input, input);
		}
	}

	@Test
	public void ignoresLinesStartingWithSpacesThenKeywords() {
		String inputTemplate        = "    {{KEYWORD}} (i=0;i<1;++i) { ";
		for (String keyword : aligner.options.keywordsToIgnore) {
			String input            = inputTemplate.replace("{{KEYWORD}}", keyword);
			testAligner(input, input);
		}
	}

	@Test
	public void ignoresLinesStartingWithTabsThenKeywords() {
				String inputTemplate        = "\t\t\t{{KEYWORD}} (i=0;i<1;++i) { ";
		for (String keyword : aligner.options.keywordsToIgnore) {
			String input            = inputTemplate.replace("{{KEYWORD}}", keyword);
			testAligner(input, input);
		}
	}

	@Test
	public void ignoresLinesThatDontHaveDelimeters() {
		String input = "(0;i<1;++i){";
		testAligner(input, input);
	}
}