public class AlignerOptions {

	public final String[] delimeters;
	public final String[] wordsToIgnore;
	public final int numSpacesForPadding;

	public AlignerOptions(String[] delimeters, String[] wordsToIgnore, int numSpacesForPadding) {
		this.delimeters = delimeters;
		this.wordsToIgnore = wordsToIgnore;
		this.numSpacesForPadding = numSpacesForPadding;
	}

	public static AlignerOptions Default() {
		String[] delimeters = new String[]{
			":",
			"+=",
			"-=",
			"*=",
			"/=",
			"%=",
			"^=",
			"|=",
			"&=",
			"<<=",
			">>>=",
			">>=",
			"?=",
			"="
		};
		String[] wordsToIgnore = new String[] {
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

		return new AlignerOptions(delimeters, wordsToIgnore, 4);
	}
}
