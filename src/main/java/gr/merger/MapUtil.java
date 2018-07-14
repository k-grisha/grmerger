package gr.merger;

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
		System.out.println(Thread.currentThread().getName());
		return resultMap;
	}


	public static <K, V> Map<K, V> mergeMaps(List<Map<K, V>> maps, BinaryOperator<V> combiner) {
//		Map<K, V> asd = maps.stream().parallel().reduce((m1, m2) -> merge(m1, m2, combiner)).orElse(Collections.emptyMap());

//		Collector<Map<K, V>, Map<K, V>, Map<K, V>> collector = Collector.of(HashMap::new
//				, (Map<K, V> map, Map<K, V> element) -> map.putAll(merge(map, element, combiner))
//				, (right, left) -> merge(right, left, combiner));

//		Map<K, V> accumulationMap = new ConcurrentHashMap<>();
//		Collector<Map<K, V>, Map<K, V>, Map<K, V>> collector = Collector.of(
//				ConcurrentHashMap::new
//				, (map, element) -> map.putAll(merge(map, element, combiner))
//				, (right, left) -> merge(right, left, combiner)
//				, Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED);
//		Map<K, V> asd = maps.parallelStream().collect(collector);


//		Map<K, V> asd = maps.parallelStream().collect(
//				HashMap::new
//				, (Map<K, V> map, Map<K, V> element) -> map.putAll(merge(map, element, combiner))
//				, (right, left) -> merge(right, left, combiner));


		Map<K, V> asd = maps.parallelStream().collect(
				HashMap::new
				, (Map<K, V> map, Map<K, V> element) -> map.putAll(merge(map, element, combiner))
				, (right, left) -> merge(right, left, combiner));

		return asd;
	}

	private static <K, V> void accumulate(Map<K, V> acumulator, Map<K, V> map,
										  BinaryOperator<V> combiner) {
		map.forEach((k, v) -> acumulator.merge(k, v, combiner));
//		System.out.println(Thread.currentThread().getName());
	}

}
