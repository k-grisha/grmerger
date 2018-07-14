
package gr.merger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.IntStream;


public class MapTestOriginal {

    Map<String, Integer> mapFirst;
    Map<String, Integer> mapSecond;

    List<Map<String, Integer>> mapSet;


    @Before
    public void init() {
        mapFirst =  new HashMap();
        mapSecond =  new HashMap();
        mapSet = new ArrayList<>();

        IntStream.range(1, 100).forEach(idx -> mapFirst.put("key" + idx, idx * 10));
        IntStream.range(1, 100).forEach(idx -> mapFirst.put("key" + idx, idx * 10));

        IntStream.range(0, 100).forEach(idx ->
        {
            final Map<String, Integer> map = new HashMap<>();
            IntStream.range(100, 200).forEach(k -> map.put("key" + (idx + k), idx + k));
            mapSet.add(map);
        });
    }

    @Test
    public void mergeTest() {
        mapFirst.put("key1", null);
        mapSecond.put("key10", null);

        Map<String, Integer> result = merge(mapFirst, mapSecond, MapTestOriginal::integerRemapping);

        Assert.assertNull(result.get("key1"));
        Assert.assertEquals(Integer.valueOf(100), result.get("key10"));
    }

    @Test
    public void mergeStreamOfMapsTest() {
        Map<String, Integer> result = mapSet.parallelStream()
                .collect(toMap(MapTestOriginal::integerRemapping));

        result.forEach((key, value) -> System.out.println(String.format("%s:%d", key, value)));

        System.out.println(result.size());
    }


    static Integer integerRemapping(Integer right, Integer left) {
        return right + left;
    }


    public static <K, V> Collector<Map<K, V>, Map<K, V>, Map<K, V>> toMap(BiFunction<V, V, V> remappingFunction) {

        final Map<K, V> accumulationMap = new ConcurrentHashMap<>();

        return Collector.of(() -> accumulationMap
                , (map, element) -> map.putAll( merge(map, element, remappingFunction))
                , ((right, left) -> accumulationMap)
                , Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED);
    }

    static public <K, V> Map<K, V> merge(Map<K, V> left, Map<K, V> right
            , BiFunction<? super V, ? super V, ? extends V> remappingFunction) {

        Objects.requireNonNull(left, "left cannot be null.");
        Objects.requireNonNull(right, "right cannot be null.");

        right.entrySet().stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .forEach(entry -> left.merge(entry.getKey(), entry.getValue(), remappingFunction));

        System.out.println(Thread.currentThread().getName());

        return left;
    }

}
