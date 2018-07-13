package gr.merger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;

public class MapUtil {

	public static <K, V> Map<K, V> mergeMaps(List<Map<K, V>> maps, BinaryOperator<V> combiner) {
//		return maps.stream().parallel().reduce( (m1, m2) -> merge(m1, m2, combiner)).orElse(Collections.emptyMap());

		final Map<K, V> accumulationMap = new ConcurrentHashMap<>();
		Collector<Map<K, V>, Map<K, V>, Map<K, V>> collector = Collector.of(
				() -> accumulationMap
				, (Map<K, V> map, Map<K, V> element) -> map.putAll(merge(map, element, combiner))    //accumulator
				, ((right, left) -> accumulationMap)
				, Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED);
		Map<K, V> asd = maps.parallelStream()
				.collect(collector);

//		final Map<K, V> accumulationMap = new ConcurrentHashMap<>();
//		Map<K, V> asd = maps.parallelStream().collect(
//				() -> accumulationMap,
//				(Map<K, V> accumulator, Map<K, V> element) -> accumulator.putAll(merge(accumulator, element, combiner)),
//				(m1, m2) -> merge(m1, m2, combiner)
//		);

		return asd;
		//.orElse(Collections.emptyMap());

	}

	private static <K, V> void accumulate(Map<K, V> acuumulator, Map<K, V> map,
										  BinaryOperator<V> combiner) {

		acuumulator.putAll(merge(acuumulator, map, combiner));

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
			bigMap = new ConcurrentHashMap<>(map1) ;
			smallMap = new ConcurrentHashMap<>(map2);
		} else {
			bigMap = new ConcurrentHashMap<>(map2) ;
			smallMap = new ConcurrentHashMap<>(map1) ;
		}

		Map<K, V> resultMap = new ConcurrentHashMap<>(bigMap);
		smallMap.forEach((k, v) -> resultMap.merge(k, v, combiner));
		System.out.println(Thread.currentThread().getName());
		return resultMap;
	}
}
