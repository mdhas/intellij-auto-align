package aligner;

import aligner.predicates.ContainsPredicate;
import aligner.predicates.Predicates;
import aligner.predicates.StartsWithPredicate;
import aligner.utils.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class AlignerOptions {

	public final String[] delimeters;
	public final Predicate<String>[] predicatesList;// returns true to process line, false to ignore line
	public final int numSpacesForPadding;

	public AlignerOptions(String[] delimeters, Predicate<String>[] predicatesList, int numSpacesForPadding) {
		this.delimeters = delimeters;
		this.predicatesList = predicatesList;
		this.numSpacesForPadding = numSpacesForPadding;
	}

	public static AlignerOptions Default() {
		String[] delimeters = new String[]{
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
			"=",
			":"
		};
		List<Predicate<String>> predicatesList =  Arrays.asList(
			// KEYWORDS
			Predicates.not(
				Predicates.any(
					new StartsWithPredicate("for "),
					new StartsWithPredicate("for("),
					new StartsWithPredicate("while "),
					new StartsWithPredicate("while("),
					new StartsWithPredicate("while ("),
					new StartsWithPredicate("do "),
					new StartsWithPredicate("if "),
					new StartsWithPredicate("if("),
					new StartsWithPredicate("else "),
					new StartsWithPredicate("else("),
					new StartsWithPredicate("switch "),
					new StartsWithPredicate("switch("),
					new StartsWithPredicate("assert "),
					new StartsWithPredicate("assert("),
					new StartsWithPredicate("abstract "),
					new StartsWithPredicate("class "),
					new StartsWithPredicate("goto "),
					new StartsWithPredicate("implements "),
					new StartsWithPredicate("interface "),
					new StartsWithPredicate("enum "),
					new StartsWithPredicate("package "),
					new StartsWithPredicate("void "),
					new StartsWithPredicate("Void "),
					new StartsWithPredicate("return "),
					new StartsWithPredicate("super "),
					new StartsWithPredicate("synchronized "),
					new StartsWithPredicate("throw "),
					new StartsWithPredicate("<"),
					new StartsWithPredicate("}"),
					new StartsWithPredicate("{"),
					new StartsWithPredicate("\""),
					new ContainsPredicate("=>")
				)
			),

			// CSS STATES
			Predicates.not(
				Predicates.any(
					new ContainsPredicate(":hover"),
					new ContainsPredicate(":active"),
					new ContainsPredicate(":focus"),
					new ContainsPredicate(":visited")
				)
			),


			// COMMENTS
			Predicates.not(
				Predicates.any(
					new StartsWithPredicate("//"),
					new StartsWithPredicate("/*"),
					new StartsWithPredicate("*")
				)
			)
		);

		return new AlignerOptions(delimeters, CollectionUtils.toArray(predicatesList), 4);
	}
}
