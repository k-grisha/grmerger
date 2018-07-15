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
		Map<String, Integer> result = MapUtil.mutableMergeMaps(maps, Integer::sum);
		// Approach 2
		Map<String, Integer> result2 = MapUtil.immutableMergeMaps(maps, Integer::sum);

		IntStream.range(1, mapSize + 1).forEach(i -> {
			Assert.assertEquals(i * listSize, (int) result.get("k" + i));
			Assert.assertEquals(i * listSize, (int) result2.get("k" + i));
		});
	}

	private static List<Map<String, Integer>> getFilledMap(int listSize, int mapSize) {
		List<Map<String, Integer>> maps = new ArrayList<>(listSize);
		IntStream.range(1, listSize + 1).forEach(i -> {
			Map<String, Integer> map = new HashMap<>(mapSize);
			IntStream.range(1, mapSize + 1).forEach(z -> map.put("k" + z, z));
			maps.add(map);
		});
		return maps;
	}

	@Test
	public void approachComparationTest() {
		int mapSize = 505;
		int listSize = 999;

		List<Map<String, Integer>> maps1 = getFilledMap(listSize, mapSize);


	}


	@Test
	public void listOfMapTestBig() {
		int mapSize = 505;
		int listSize = 999;
		List<Map<String, Integer>> maps = new ArrayList<>(listSize);
		IntStream.range(1, listSize + 1).forEach(i -> {
			Map<String, Integer> map = new HashMap<>(mapSize);
			IntStream.range(1, mapSize + 1).forEach(z -> map.put("k" + z, z));
			maps.add(map);
		});

		Map<String, Integer> result = null;
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			result = MapUtil.mutableMergeMaps(maps, Integer::sum);
		}
		long t2 = System.currentTimeMillis();

		Map<String, Integer> result2 = null;
		for (int i = 0; i < 1000; i++) {
			result2 = MapUtil.immutableMergeMaps(maps, Integer::sum);
		}
		long t3 = System.currentTimeMillis();

		System.out.println((t2 - t1) + "   " + (t3 - t2));

		System.out.println(result.size() + "  " + result2.size());

//		IntStream.range(1, mapSize + 1).forEach(i -> {
//			Assert.assertEquals(i * listSize, (int) result.get("k" + i));
//		});
	}


//	@Test
//	public void listOfBigMapTest() {
//		List<Map<String, Integer>> maps = new ArrayList<>();
//
//		IntStream.range(0, 4000).forEach(i -> maps.add(getFilledMap(i)));
//
//		long tStart = System.currentTimeMillis();
//		Map<String, Integer> result = MapUtil.mergeMaps(maps, Integer::sum);
//		long tEnd = System.currentTimeMillis();
//		System.out.println(tEnd - tStart + " ms " + result.size());
//
//	}


	@Test
	public void bigMapTest() {
		Map<String, Integer> map1 = getFilledMap(3000000);
		Map<String, Integer> map2 = getFilledMap(4);
		long tStart = System.currentTimeMillis();
		Map<String, Integer> summarized = null;
		for (int i = 0; i < 100; i++) {
			summarized = MapUtil.merge(map1, map2, Integer::sum);
		}
		long tEnd = System.currentTimeMillis();
		System.out.println(tEnd - tStart + " ms " + summarized.size());
	}

	public static Map<String, Integer> getFilledMap(int size) {
		HashMap<String, Integer> map = new HashMap<>(size);
		for (int i = 0; i < size; i++) {
			map.put("k" + i, i);
		}
		return map;
	}

	@Test
	public void asdf() {
//		List<String> lst = Arrays.asList("a","a","a","a","a","a","a");
//		long sdf = lst.stream().reduce()
//		System.out.println(sdf);

//		int[] asd = IntStream.range(0, 100).parallel().map(x -> x * 2).toArray();
//		for (int i : asd) {
//			System.out.println(i);
//		}


//		List<String> l = new ArrayList(Arrays.asList("one", "two"));
//		Stream<String> sl = l.stream();
//		l.add("three");
//		String s = sl.collect(joining(" "));
//		System.out.println(s);

//		List<String> lst = Arrays.asList("a","a","a","a","a","a","a");
//		Set<String> seen = new HashSet<>();
//		List<String> res = lst.stream().parallel().map(e -> {
//			if (seen.add(e))
//				return e+"-1";
//			else
//				return e+"-0";
//		}).collect(Collectors.toList());
//		System.out.println(res);


	}
}