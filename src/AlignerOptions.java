public class AlignerOptions {

	public final String[] delimeters;
	public final String[] keywordsToIgnore;
	public final int numSpacesForPadding;

	public AlignerOptions(String[] delimeters, String[] keywordsToIgnore, int numSpacesForPadding) {
		this.delimeters = delimeters;
		this.keywordsToIgnore = keywordsToIgnore;
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
		String[] keywordsToIgnore = new String[] {
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
			"private",
			"public",
			"protected",
			"void",
			"Void",
			"return",
			"super",
			"synchronized",
			"throw",
		};

		return new AlignerOptions(delimeters, keywordsToIgnore, 4);
	}
}
