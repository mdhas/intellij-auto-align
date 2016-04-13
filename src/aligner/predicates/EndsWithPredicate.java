package aligner.predicates;

import aligner.utils.StringUtils;

import java.util.function.Predicate;

public class EndsWithPredicate implements Predicate<String>{

	public final String value;

	public EndsWithPredicate(String value) {
		this.value = value;
	}


	@Override
	public boolean test(String string) {
		if (StringUtils.isNoE(string)) {
			return false;
		}

		return string.trim().endsWith(value);
	}

	public static Predicate<String> any(String[] strings) {
		return testString -> {
			for (String string : strings) {
				if (new EndsWithPredicate(string).test(testString)) {
					return true;
				}
			}
			return false;
		};
	}
}
