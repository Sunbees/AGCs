package com.sun;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class Demo01ApplicationTests {

    @Test
    void contextLoads() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        Integer sum = numbers.stream().reduce(0, (subTotal, element) -> (subTotal + element));
        System.out.println(sum);
        sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum);

        List<String> letters = Arrays.asList("apple", "banana", "cat", "dog", "elephant");
        //String s = letters.stream().reduce("", (subtotal, element) -> subtotal.concat(element));
        Integer len = letters.parallelStream().reduce(0, (subTotal, element) -> (subTotal + element.length()), Integer::sum);
        System.out.println(len);
    }

    List<String> res;
    Map<String, PriorityQueue<String>> map;

    public List<String> findItinerary(List<List<String>> tickets) {
        res = new ArrayList<>();
        map = new HashMap();
        for (List<String> ticket : tickets) {
            String src = ticket.get(0);
            String dest = ticket.get(1);
            if (!map.containsKey(src)) {
                map.put(src, new PriorityQueue<>());
            }
            map.get(src).offer(dest);
        }
        dfs("JFK");
        Collections.reverse(res);
        return res;
    }

    private void dfs(String airport) {
        while (map.containsKey(airport) && map.get(airport).size() > 0) {
            String dest = map.get(airport).poll();
            dfs(dest);
        }
        res.add(airport);
    }

    //[["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
    @Test
    void findItiner() {
        List<List<String>> tickets = new ArrayList<>();
        List<String> list1 = Arrays.asList("JFK", "SFO");
        List<String> list2 = Arrays.asList("JFK", "ATL");
        List<String> list3 = Arrays.asList("SFO", "ATL");
        List<String> list4 = Arrays.asList("ATL", "JFK");
        List<String> list5 = Arrays.asList("ATL", "SFO");
        tickets.add(list1);
        tickets.add(list2);
        tickets.add(list3);
        tickets.add(list4);
        tickets.add(list5);
        findItinerary(tickets);

    }

}
