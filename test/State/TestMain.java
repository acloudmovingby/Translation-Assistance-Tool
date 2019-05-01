/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Chris
 */
public class TestMain {

    public static void main(String[] args) {
        /*System.out.println(Math.log10(-1));
        System.out.println((int) Math.log10(0) + 1);*/
 /* IntStream.range(0, 10)
                .map(i -> i*2)
                .forEach(System.out::println); */

 /*
        Stream.of("this", "is", "a", "stream")
                .map(s -> {
                    return s.length();
                })
                .forEach(System.out::println); */
        int[] arr = new int[]{
            0, 2, 3, 6, 7
        };

        System.out.println(Arrays.stream(arr)
                .filter(i -> i % 2 == 0)
                .map(i -> i * i)
                .sum());

        System.out.println("\nNext");
        List<Integer> m = new ArrayList();
        List<String> sq = new ArrayList();

        Integer a = 1;
        Integer b = 2;
        Integer c = a + b;
        m.add(a);
        m.add(b);
        m.add(c);

        sq.add("a");
        sq.add("b");
        sq.add("c");

        for (String s : sq) {
            s = "z";
        }

        System.out.println(sq);

    }

}
