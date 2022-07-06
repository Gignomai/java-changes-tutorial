package com.gignomai.javachangestutorial.java8;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.function.BinaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StreamsTest {

    @Test
    void shouldCreateStreamFromCollection() {
        final String test = "Test";

        final Stream<String> stream = Stream.of(test);

        assertThat(stream).isNotNull();
        assertThat(stream).contains(test);
    }

    // Method stream() from Optional is available since Java 9
    @Test
    void shouldCreateStreamFromOptional() {
        final Optional<String> test = Optional.of("Test");

        final Stream<String> stream = test.stream();

        assertThat(stream).isNotNull();
        assertThat(stream).contains("Test");
    }

    // Method results() from Matcher is only available since Java 9
    @Test
    void shouldCreateStreamFromMatcher() {
        final String test = "This is the test string";
        final Pattern pattern = Pattern.compile("test");
        final Matcher matcher = pattern.matcher(test);

        final OptionalInt start = matcher.results()
                .mapToInt(MatchResult::start)
                .findFirst();

        assertThat(start).isNotEmpty();
        assertThat(start.getAsInt()).isEqualTo(12);
    }

    // Method results() from Matcher is only available since Java 9
    @Test
    void shouldCreateStreamFromScanner() {
        final String test = "This is the test string";
        final Scanner scanner = new Scanner(test);

        final OptionalInt start = scanner.findAll("test")
                .mapToInt(MatchResult::start)
                .findFirst();

        assertThat(start).isNotEmpty();
        assertThat(start.getAsInt()).isEqualTo(12);
    }

    @Test
    void shouldCreateCollectionFromStream() {
        final List<String> names = Arrays.asList("Barcelona", "Girona", "LLeida", "Tarragona");

        final List<String> result = names.stream()
                .filter(name -> name.endsWith("ona"))
                .collect(Collectors.toList());

        assertThat(result).isNotNull();
        assertThat(result).contains("Barcelona", "Girona", "Tarragona");
    }

    @Test
    void shouldFilterValuesFromCollection() {
        final List<String> names = Arrays.asList("Barcelona", "Girona", "LLeida", "Tarragona");

        final List<String> result = names.stream()
                .filter(name -> name.startsWith("Bar"))
                .collect(Collectors.toList());

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldMapValuesFromCollection() {
        final List<String> names = Arrays.asList("Barcelona", "Girona", "LLeida", "Tarragona");

        final List<String> result = names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("BARCELONA", "GIRONA", "LLEIDA", "TARRAGONA");
        assertThat(result).doesNotContain("Barcelona", "Girona", "LLeida", "Tarragona");
    }

    @Test
    void shouldFlatMapValuesFromCollection() {
        final List<List<String>> names = Arrays.asList(Arrays.asList("Barcelona", "Girona"), Arrays.asList("LLeida", "Tarragona"));

        final List<String> result = names.stream()
                .flatMap(List::stream)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("BARCELONA", "GIRONA", "LLEIDA", "TARRAGONA");
        assertThat(result).doesNotContain("Barcelona", "Girona", "LLeida", "Tarragona");
    }

    @Test
    void shouldMapToInt() {
        final List<List<String>> names = Arrays.asList(Arrays.asList("Barcelona", "Girona"), Arrays.asList("LLeida", "Tarragona"));

        final int result = names.stream()
                .mapToInt(List::size)
                .sum();

        assertThat(result).isEqualTo(4);
    }

    @Test
    void shouldLogSomethingWithPeek() {
        final List<List<String>> names = Arrays.asList(Arrays.asList("Barcelona", "Girona"), Arrays.asList("LLeida", "Tarragona"));

        final int result = names.stream()
                .peek(list -> System.out.println(list.size()))
                .mapToInt(List::size)
                .sum();

        assertThat(result).isEqualTo(4);

    }

    // This piece of code uses foreach in a not a very functional way because the state modification.
    @Test
    void shouldConsumeValuesFromCollection() {
        final List<String> names = Arrays.asList("Barcelona", "Girona", "LLeida", "Tarragona");

        final List<String> result = new ArrayList<>();

        names.stream()
                .filter(name -> name.endsWith("ona"))
                .forEach(result::add);

        assertThat(result).hasSize(3);
    }

    @Test
    void shouldReturnDistinctValuesFromCollection() {
        final List<String> names = Arrays.asList("Barcelona", "Barcelona", "Girona", "LLeida", "Tarragona");

        final List<String> result = names.stream()
                .distinct()
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("Barcelona", "Girona", "LLeida", "Tarragona");
    }

    @Test
    void shouldCountValuesFromCollection() {
        final List<String> names = Arrays.asList("Barcelona", "Girona", "LLeida", "Tarragona");

        final long count = names.stream()
                .filter(name -> name.endsWith("ona"))
                .count();

        assertThat(count).isEqualTo(3L);
    }

    @Test
    void shouldReduceToMaxValueFromCollection() {
        final List<Integer> numbers = Arrays.asList(1, 6, 3, 2);

        final Optional<Integer> max = numbers.stream()
                .max(Integer::compareTo);

        assertThat(max).isNotEmpty();
        assertThat(max.get()).isEqualTo(6);
    }

    @Test
    void shouldReduceToMinValueFromCollection() {
        final List<Integer> numbers = Arrays.asList(1, 6, 3, 2);

        final Optional<Integer> min = numbers.stream()
                .min(Integer::compareTo);

        assertThat(min).isNotEmpty();
        assertThat(min.get()).isEqualTo(1);
    }

    @Test
    void shouldReduceToMaxStringLengthUsingComparator() {
        final List<String> names = Arrays.asList("Barcelona", "Girona", "LLeida", "Tarragona");

        final Optional<String> max = names.stream()
                .max(Comparator.comparingInt(String::length));

        assertThat(max).isNotEmpty();
        assertThat(max.get()).isEqualTo("Barcelona");
    }

    @Test
    void shouldReduceToMinStringLengthUsingComparator() {
        final List<String> names = Arrays.asList("Barcelona", "Girona", "LLeida", "Tarragona");

        final Optional<String> max = names.stream()
                .min(Comparator.comparingInt(String::length));

        assertThat(max).isNotEmpty();
        assertThat(max.get()).isEqualTo("Girona");
    }

    @Test
    void shouldCustomReduceValuesFromCollection() {
        final List<String> names = Arrays.asList("aaa", "aa", "a");

        final Optional<String> result = names.stream()
                .reduce(getShortestString());

        assertThat(result).contains("a");
    }

    private BinaryOperator<String> getShortestString() {
        return (word1, word2)
                -> word1.length() <= word2.length()
                ? word1 : word2;
    }
}
