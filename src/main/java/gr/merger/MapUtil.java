package gr.merger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

public class MapUtil {

	/**
	 *
	 * @param map1
	 * @param map2
	 * @param combiner
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> Map<K, V> merge(
			Map<K, V> map1,
			Map<K, V> map2,
			BinaryOperator<V> combiner) {
		// TODO Подумать какую мапу клонировать
		Map<K, V> map3 = new HashMap<>(map1);
		map2.forEach((k, v) -> map3.merge(k, v, combiner));
		return map3;
	}
}
