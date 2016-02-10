package aligner.utils;

import java.util.Collection;

public class CollectionUtils {

	public static <T> T[] toArray(Collection<T> collection) {
		if (collection == null) {
			return null;
		}

		return (T[]) collection.toArray();
	}
}
