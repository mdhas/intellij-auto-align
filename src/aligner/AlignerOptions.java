package aligner;

import aligner.predicates.*;

import java.util.function.Predicate;

public class AlignerOptions {

  private final String[] delimeters;
  private final Predicate<String> shouldProcessLine;// returns true to process line, false to ignore line
  private final int numSpacesForPadding;

  public AlignerOptions(String[] delimeters, Predicate<String> shouldProcessLine, int numSpacesForPadding) {
    this.delimeters = delimeters;
    this.shouldProcessLine = shouldProcessLine;
    this.numSpacesForPadding = numSpacesForPadding;
  }

  public boolean shouldProccessLine(String line) {
    return shouldProcessLine.test(line);
  }

  public int padding() {
    return numSpacesForPadding;
  }

  public String[] delimeters() {
    return delimeters;
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
    Predicate<String> shouldProcessLinePredicate =  Predicates.and(
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
          new StartsWithPredicate(":"),
          new ContainsPredicate("=>")
        )
      ),

      // CSS STATES
      Predicates.not(
        Predicates.any(
          new ContainsPredicate(":hover"),
          new ContainsPredicate(":active"),
          new ContainsPredicate(":focus"),
          new ContainsPredicate(":visited"),
          new StartsWithPredicate("&:")
        )
      ),


      // COMMENTS
      Predicates.not(
        Predicates.any(
          new StartsWithPredicate("//"),
          new StartsWithPredicate("/*"),
          new StartsWithPredicate("*")
        )
      ),

      // MISC
      Predicates.not(
        EndsWithPredicate.any(delimeters)
      )
    );

    return new AlignerOptions(delimeters, shouldProcessLinePredicate, 4);
  }
}
