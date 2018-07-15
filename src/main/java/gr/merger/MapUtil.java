package gr.merger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

public class MapUtil {

	/**
	 * Map-merging
	 *
	 * @param map1     map1
	 * @param map2     map2
	 * @param combiner merging behavior
	 * @return result map
	 */
	public static <K, V> Map<K, V> merge(Map<K, V> map1, Map<K, V> map2, BinaryOperator<V> combiner) {
		Map<K, V> bigMap;
		Map<K, V> smallMap;
		if (map1.size() > map2.size()) {
			bigMap = map1;
			smallMap = map2;
		} else {
			bigMap = map2;
			smallMap = map1;
		}
		Map<K, V> resultMap = new HashMap<>(bigMap);
		smallMap.forEach((k, v) -> resultMap.merge(k, v, combiner));
//		System.out.println(Thread.currentThread().getName());
		return resultMap;
	}

	/**
	 * Bonus part
	 * merging a list of maps based on a immutable merge-method result from the task
	 *
	 * @param maps     list of maps
	 * @param combiner merging behavior
	 * @return result map
	 */
	public static <K, V> Map<K, V> immutableMergeMaps(List<Map<K, V>> maps, BinaryOperator<V> combiner) {
		return maps.parallelStream().reduce(Collections.emptyMap(), (m1, m2) -> merge(m1, m2, combiner));
	}

	/**
	 * Bonus part
	 * merging a list of maps based on a mutable merge-method result
	 *
	 * @param maps     list of maps
	 * @param combiner merging behavior
	 * @return result map
	 */
	public static <K, V> Map<K, V> mutableMergeMaps(List<Map<K, V>> maps, BinaryOperator<V> combiner) {
		return maps.parallelStream().collect(
				HashMap::new,
				(accumulator, element) -> accumulate(accumulator, element, combiner),
				(m1, m2) -> accumulate(m1, m2, combiner));
	}

	private static <K, V> void accumulate(Map<K, V> accumulator, Map<K, V> map, BinaryOperator<V> combiner) {
		map.forEach((k, v) -> accumulator.merge(k, v, combiner));
	}

}
