package gr.merger;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class MapUtilTest {

	@Test
	public void mergeIntegerTest() {
		Map<String, Integer> map1 = new HashMap<>();
		Map<String, Integer> map2 = new HashMap<>();
		map1.put("key1", 20);
		map1.put("key2", 30);
		map2.put("key3", 40);
		map2.put("key1", 50);

		Map<String, Integer> summarized = MapUtil.merge(map1, map2, Integer::sum);
		Assert.assertEquals(70, (int) summarized.get("key1"));

		Map<String, Integer> multiplied = MapUtil.merge(map1, map2, (a, b) -> a * b);
		Assert.assertEquals(1000, (int) multiplied.get("key1"));
	}

	@Test
	public void mergeStringTest() {
		Map<String, String> map1 = new HashMap<>();
		Map<String, String> map2 = new HashMap<>();
		map1.put("key1", "A1");
		map1.put("key2", "A2");
		map1.put("key3", "A3");
		map2.put("key3", "B3");
		map2.put("key1", "B1");

		Map<String, String> concatenated = MapUtil.merge(map1, map2, String::concat);
		Assert.assertEquals("A1B1", concatenated.get("key1"));
	}

	@Test
	public void listOfMapTest() {
		int mapSize = 505;
		int listSize = 999;
		List<Map<String, Integer>> maps = getFilledMap(listSize, mapSize);

		// Approach 1
		Map<String, Integer> result1 = MapUtil.immutableMergeMaps(maps, Integer::sum);
		// Approach 2
		Map<String, Integer> result2 = MapUtil.mutableMergeMaps(maps, Integer::sum);

		IntStream.range(1, mapSize + 1).forEach(i -> {
			Assert.assertEquals(i * listSize, (int) result1.get("k" + i));
			Assert.assertEquals(i * listSize, (int) result2.get("k" + i));
		});
	}


	@Test
	public void approachComparisonTest() {
		int mapSize = 500;
		int listSize = 500;
		int repeats = 1000;

		List<Map<String, Integer>> maps = getFilledMap(listSize, mapSize);

		Map<String, Integer> result1 = null;
		Map<String, Integer> result2 = null;
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < repeats; i++) {
			result1 = MapUtil.immutableMergeMaps(maps, Integer::sum);
		}
		long t2 = System.currentTimeMillis();
		for (int i = 0; i < repeats; i++) {
			result2 = MapUtil.mutableMergeMaps(maps, Integer::sum);
		}
		long t3 = System.currentTimeMillis();

		System.out.println(String.format("First approach take %s ms\nSecond approach take %s ms ", (t2 - t1), (t3 - t2)));
		Assert.assertEquals(result1.size(), result2.size());
	}

	private List<Map<String, Integer>> getFilledMap(int listSize, int mapSize) {
		List<Map<String, Integer>> maps = new ArrayList<>(listSize);
		IntStream.range(1, listSize + 1).forEach(i -> {
			Map<String, Integer> map = new HashMap<>(mapSize);
			IntStream.range(1, mapSize + 1).forEach(z -> map.put("k" + z, z));
			maps.add(map);
		});
		return maps;
	}

}