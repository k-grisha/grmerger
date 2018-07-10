package gr.merger;

import java.util.Map;
import java.util.function.BiFunction;

public class MapUtil {
	public static <K, V> Map<K, V> merge(Map<K, V> map1, Map<K, V> map2, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		map1.forEach((k, v) -> map2.merge(k, v, remappingFunction));
		return map2;
	}
}
