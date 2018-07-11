package gr.merger;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
		map2.put("key3", "B3");
		map2.put("key1", "B1");

		Map<String, String> concatenated = MapUtil.merge(map1, map2, String::concat);
		Assert.assertEquals("A1B1", concatenated.get("key1"));
	}

	@Test
	public void bigMapTest() {
		Map<String, Integer> map1 = getRandom(3000000);
		Map<String, Integer> map2 = getRandom(4);
		long tStart = System.currentTimeMillis();
		Map<String, Integer> summarized = null;
		for (int i = 0; i < 100; i++) {
			summarized = MapUtil.merge(map1, map2, Integer::sum);
		}
		long tEnd = System.currentTimeMillis();
		System.out.println(tEnd - tStart + " ms " + summarized.size());
	}

	private Map<String, Integer> getRandom(int size) {
		HashMap<String, Integer> map = new HashMap<>(size);
		for (int i = 0; i < size; i++) {
			map.put("k" + i, i);
		}
		return map;
	}
}