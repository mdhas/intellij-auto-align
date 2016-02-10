import aligner.Aligner;
import aligner.AlignerOptions;
import aligner.predicates.StartsWithPredicate;
import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

	private final String[] keywords = new String[] {
		"for",
		"while",
		"do",
		"if",
		"else",
		"switch",
		"assert",
		"abstract",
		"class",
		"goto",
		"implements",
		"interface",
		"enum",
		"package",
		"void",
		"Void",
		"return",
		"super",
		"synchronized",
		"throw",
		"<",
		"\""
	};
	private final String[] cssStates = new String[] {
		":hover",
		":active",
		":focused",
		":visited"
	};

	private void testAligner(String input, String expected) {
		String result = aligner.align(input);
		assertEquals(expected, result);
	}

	private void testStartsWith(String inputTemplate) {
		for (String keyword: keywords) {
			String input = inputTemplate.replace("{{KEYWORD}}", keyword);
			testAligner(input, input);
		}
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
	public void ignoresLinesThatStartWithKeyWords() {
		String inputTemplate;
		String outTemplate;
		inputTemplate = "{{KEYWORD}}(i=0;i<1;++i) {\n" +
						"let joey=0;";
		outTemplate = inputTemplate.replace("{{KEYWORD}}", "for");
		testAligner(outTemplate, outTemplate);

		outTemplate = inputTemplate.replace("{{KEYWORD}}", "for ");
		testAligner(outTemplate, outTemplate);


		outTemplate = inputTemplate.replace("{{KEYWORD}}", "while");
		testAligner(outTemplate, outTemplate);

		outTemplate = inputTemplate.replace("{{KEYWORD}}", "while ");
		testAligner(outTemplate, outTemplate);


		inputTemplate =	"  do {\n" +
						"    i+=1;\n" +
						"  } while((i=j) <= 7);";
		testAligner(inputTemplate, inputTemplate);

		inputTemplate =	"  { while((i=j) <= 7) {\n" +
						"  i++;\n" +
						" }\n" +
						"    i+=1;\n}";
		testAligner(inputTemplate, inputTemplate);

		// FIXME : make this test past even
//		inputTemplate =	"{myfield: 'isCool'},\n"+
//						"{myOtherfield: 'isCool'},\n"+
//						"{myFriendsfield: 'isCool'}\n";
//
//		outTemplate =	"{myfield           : 'isCool'},\n"+
//						"{myOtherfield      : 'isCool'},\n"+
//						"{myFriendsfield    : 'isCool'}\n";
//		testAligner(inputTemplate, outTemplate);
	}

	@Test
	public void ignoresLinesStartingWithSpacesThenWords() {
		String inputTemplate        = "    {{KEYWORD}} (i=0;i<1;++i) { ";
		testStartsWith(inputTemplate);
	}

	@Test
	public void ignoresLinesStartingWithTabsThenWords() {
		String inputTemplate        = "\t\t\t{{KEYWORD}} (i=0;i<1;++i) { ";
		testStartsWith(inputTemplate);
	}

	@Test
	public void ignoresLinesThatDontHaveDelimeters() {
		String input = "(0;i<1;++i){";
		testAligner(input, input);
	}

	@Test
	public void ignoresLinesForCssSelectorStates() {
		String input =	".dev-summary.possible-hero-tile:{{CSS_STATE}} .dev-cover-image {\n" +
						"  transition-delay : 350ms;\n" +
						"  transition: all 1750ms ease-in-out;\n" +
						"  transform: translate(45px, 25px) scale(1.5);\n" +
						"}";
		String expected =	".dev-summary.possible-hero-tile:{{CSS_STATE}} .dev-cover-image {\n" +
							"  transition-delay    : 350ms;\n" +
							"  transition          : all 1750ms ease-in-out;\n" +
							"  transform           : translate(45px, 25px) scale(1.5);\n" +
							"}";

		for (String cssState : cssStates) {
			testAligner(input.replace("{{CSS_STATE}}", cssState), expected.replace("{{CSS_STATE}}", cssState));
		}
	}

	@Test
	public void usesEqualsSignBeforeColonForAlignment() {
		String input =		"let defaultParams = DCHelper.getDefaultParams({SENTV_TYPE : FILTERS.SENTV_TYPE.MOVIES});\n" +
							"let dc = DiscoverListDC.create({\n" +
							"  title      : title,\n" +
							"  url        : DCHelper.getUrlItem(),\n" +
							"  request    : VueEndpoints.Explore.items(defaultParams).updateParams(inParams)\n" +
							"});";
		String expected =	"let defaultParams    = DCHelper.getDefaultParams({SENTV_TYPE : FILTERS.SENTV_TYPE.MOVIES});\n" +
							"let dc               = DiscoverListDC.create({\n" +
							"  title              : title,\n" +
							"  url                : DCHelper.getUrlItem(),\n" +
							"  request            : VueEndpoints.Explore.items(defaultParams).updateParams(inParams)\n" +
							"});";
		testAligner(input, expected);
	}

	@Test
	public void usesFirstIndexOfDelimeterForAutoAlignment() {
		String input =		"{\n" +
							"hello: (something=cool())\n"+
							"arnoldLovesCake: 'oh yeah'\n"+
							"}";
		String expected =	"{\n" +
							"hello              : (something=cool())\n"+
							"arnoldLovesCake    : 'oh yeah'\n"+
							"}";
		testAligner(input, expected);
	}

	@Test
	public void doesNotIgnoreLinesThatStartWithKeywordWithExtraTextAfter() {
		String input =		"  tagName: 'tile-hero',\n" +
							"  classNames: ['hero'],\n" +
							"  program: Ember.computed.alias('attrs.item.value')";
		String expected =	"  tagName       : 'tile-hero',\n" +
							"  classNames    : ['hero'],\n" +
							"  program       : Ember.computed.alias('attrs.item.value')";

		testAligner(input, expected);


		input =		"  tagName: 'tile-hero',\n" +
					"  forNames: ['hero'],\n" +
					"  program: Ember.computed.alias('attrs.item.value')";
		expected =	"  tagName     : 'tile-hero',\n" +
					"  forNames    : ['hero'],\n" +
					"  program     : Ember.computed.alias('attrs.item.value')";
		testAligner(input, expected);


		input =		"  tagName: 'tile-hero',\n" +
					"  whileNames: ['hero'],\n" +
					"  program: Ember.computed.alias('attrs.item.value')";
		expected =	"  tagName       : 'tile-hero',\n" +
					"  whileNames    : ['hero'],\n" +
					"  program       : Ember.computed.alias('attrs.item.value')";
		testAligner(input, expected);
	}

}