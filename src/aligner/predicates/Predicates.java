package aligner.predicates;


import java.util.function.Predicate;

public class Predicates {

	@SafeVarargs
	public static <T> Predicate<T>  or(Predicate<T>... predicates) {
		return (val) -> {
			for (Predicate<T> predicate : predicates) {
				if (predicate.test(val)) {
					return true;
				}
			}
			return false;
		};
	}

	@SafeVarargs
	public static <T> Predicate<T>  and(Predicate<T>... predicates) {
		return (val) -> {
			for (Predicate<T> predicate : predicates) {
				if (!predicate.test(val)) {
					return false;
				}
			}
			return true;
		};
	}

	public static <T> Predicate<T>  not(Predicate<T> predicate) {
		return ((val) -> !predicate.test(val));
	}

	public static <T> Predicate<T> any(Predicate<T>... predicates) {
		return Predicates.or(predicates);
	}
}
