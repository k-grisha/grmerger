package gr.merger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.IntStream;

import static gr.merger.MapUtilTest.getFilledMap;


public class MapTest {

	Map<String, Integer> mapFirst;
	Map<String, Integer> mapSecond;

	List<Map<String, Integer>> mapList;


	@Before
	public void init() {
		mapFirst = new HashMap<>();
		mapSecond = new HashMap<>();
		mapList = new ArrayList<>();

		IntStream.range(1, 100).forEach(idx -> mapFirst.put("key" + idx, idx * 10));
		IntStream.range(1, 100).forEach(idx -> mapFirst.put("key" + idx, idx * 10));

		IntStream.range(0, 100).forEach(idx ->
		{
			final Map<String, Integer> map = new HashMap<>();
			IntStream.range(100, 200).forEach(k -> map.put("key" + (idx + k), idx + k));
			mapList.add(map);
		});
	}

	@Test
	public void mergeTest() {
		mapFirst.put("key1", null);
		mapSecond.put("key10", null);

		Map<String, Integer> result = merge(mapFirst, mapSecond, MapTest::integerRemapping);

		Assert.assertNull(result.get("key1"));
		Assert.assertEquals(Integer.valueOf(100), result.get("key10"));
	}

	@Test
	public void mergeStreamOfMapsTest() {
//		List<Map<String, Integer>> maps = new ArrayList<>();
//		IntStream.range(0, 5).forEach(idx -> {
//			final Map<String, Integer> map = new HashMap<>();
//			IntStream.range(1, 5).forEach(k -> map.put("key" + k, k));
//			maps.add(map);
//		});

		List<Map<String, Integer>> maps2 = Arrays.asList(
				getFilledMap(2),
				getFilledMap(3),
				getFilledMap(4),
				getFilledMap(5),
				getFilledMap(6));


		Map<String, Integer> result = maps2.parallelStream()
				.collect(toMap(MapTest::integerRemapping));

		System.out.println(result);
		Assert.assertEquals(0, (int) result.get("k0"));
		Assert.assertEquals(5, (int) result.get("k1"));
		Assert.assertEquals(8, (int) result.get("k2"));
		Assert.assertEquals(9, (int) result.get("k3"));
		Assert.assertEquals(8, (int) result.get("k4"));
		Assert.assertEquals(5, (int) result.get("k5"));

//		Assert.assertEquals(5, (int) result.get("key1"));
//		Assert.assertEquals(10, (int) result.get("key2"));
//		Assert.assertEquals(20, (int) result.get("key4"));
//		result.forEach((key, value) -> System.out.println(String.format("%s:%d", key, value)));
//		System.out.println(result.size());
	}


	static Integer integerRemapping(Integer right, Integer left) {
		return right + left;
	}


	public static <K, V> Collector<Map<K, V>, Map<K, V>, Map<K, V>> toMap(BiFunction<V, V, V> remappingFunction) {

		final Map<K, V> accumulationMap = new ConcurrentHashMap<>();

//		return Collector.of(() -> accumulationMap
//				, (Map<K, V> map, Map<K, V> element) -> map.putAll(mergeMy(map, element, remappingFunction))
////				, (map, element) -> merge(map, element, remappingFunction)
//				, (right, left) -> mergeMy(right, left, remappingFunction)
//		);

		return Collector.of(HashMap::new
				, (Map<K, V> map, Map<K, V> element) -> map.putAll(mergeMy(map, element, remappingFunction))
//				, (map, element) -> merge(map, element, remappingFunction)
				, (right, left) -> mergeMy(right, left, remappingFunction)
		);
	}

	static public <K, V> Map<K, V> merge(Map<K, V> left, Map<K, V> right
			, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {

		right.forEach((key, value) -> left.merge(key, value, remappingFunction));

		System.out.println(Thread.currentThread().getName());

		return left;
	}


	public static <K, V> Map<K, V> mergeMy(Map<K, V> map1, Map<K, V> map2,
										   BiFunction<V, V, V> combiner) {

		Map<K, V> resultMap = new HashMap<>(map1);
		map2.forEach((k, v) -> resultMap.merge(k, v, combiner));
		System.out.println(Thread.currentThread().getName());
		return resultMap;
	}

}