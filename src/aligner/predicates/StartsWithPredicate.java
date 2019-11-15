package aligner.predicates;

import aligner.utils.StringUtils;

import java.util.function.Predicate;

public class StartsWithPredicate implements Predicate<String>{

    public final String startsWithValue;

    public StartsWithPredicate(String startsWithValue) {
        if (StringUtils.isNoE(startsWithValue)) {
            throw new NullPointerException("a non-empty value is required");
        }
        this.startsWithValue = startsWithValue;
    }


    @Override
    public boolean test(String string) {
        if (StringUtils.isNoE(string)) {
            return false;
        }

        return string.trim().startsWith(startsWithValue);
    }
}
