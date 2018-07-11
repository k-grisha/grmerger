package gr.merger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

public class MapUtil {

	public static <K, V> Map<K, V> mergeMaps(List<Map<K, V>> maps, BinaryOperator<V> combiner) {
		return maps.stream().parallel().reduce((m1, m2) -> merge(m1, m2, combiner)).orElse(Collections.emptyMap());

	}

	/**
	 * @param map1
	 * @param map2
	 * @param combiner
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> Map<K, V> merge(Map<K, V> map1, Map<K, V> map2,
										 BinaryOperator<V> combiner) {
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
		return resultMap;
	}
}
