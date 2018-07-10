package gr.merger;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapUtilTest {

	@Test
	public void merge() {
		Map<String, Integer> map1 = new HashMap<>();
		Map<String, Integer> map2 = new HashMap<>();
		map1.put("key1", 20);
		map1.put("key2", 30);
		map2.put("key3", 40);
		map2.put("key1", 50);

		Map<String, Integer> res = MapUtil.merge(map1, map2, (a, b) -> a + b);
		Assert.assertEquals(70, (int) res.get("key1"));
	}
}