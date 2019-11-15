package aligner.predicates;

import aligner.utils.StringUtils;

import java.util.function.Predicate;

public class ContainsPredicate implements Predicate<String>{

    public final String value;

    public ContainsPredicate(String value) {
        this.value = value;
    }


    @Override
    public boolean test(String string) {
        if (StringUtils.isNoE(string)) {
            return false;
        }

        return string.contains(value);
    }
}
