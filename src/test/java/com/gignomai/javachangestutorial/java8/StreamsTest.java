package com.gignomai.javachangestutorial.java8;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StreamsTest {

    @Test
    void shouldCreateStreamsFromCollection() {
        final String test = "Test";

        final Stream<String> stream = Stream.of(test);

        assertThat(stream).isNotNull();
        assertThat(stream).contains(test);
    }

    @Test
    void shouldCreateCollectionFromStream() {
        final List<String> names = List.of("Barcelona", "Girona", "LLeida", "Tarragona");

        final List<String> result = names.stream()
                .filter(name -> name.endsWith("ona"))
                .collect(Collectors.toList());

        assertThat(result).isNotNull();
        assertThat(result).contains("Barcelona", "Girona", "Tarragona");
    }

    @Test
    void shouldFilterValuesFromCollection() {
        final List<String> names = List.of("Barcelona", "Girona", "LLeida", "Tarragona");

        final List<String> result = names.stream()
                .filter(name -> name.startsWith("Bar"))
                .collect(Collectors.toList());

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldMapValuesFromCollection() {
        final List<String> names = List.of("Barcelona", "Girona", "LLeida", "Tarragona");

        final List<String> result = names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("BARCELONA", "GIRONA", "LLEIDA", "TARRAGONA");
        assertThat(result).doesNotContain("Barcelona", "Girona", "LLeida", "Tarragona");
    }

    @Test
    void shouldFlatMapValuesFromCollection() {
        final List<List<String>> names = List.of(List.of("Barcelona", "Girona", "LLeida", "Tarragona"));

        final List<String> result = names.stream()
                .flatMap(List::stream)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("BARCELONA", "GIRONA", "LLEIDA", "TARRAGONA");
        assertThat(result).doesNotContain("Barcelona", "Girona", "LLeida", "Tarragona");
    }

    @Test
    void shouldConsumeValuesFromCollection() {
        final List<String> names = List.of("Barcelona", "Girona", "LLeida", "Tarragona");

        final List<String> result = new ArrayList<>();

        names.stream()
                .filter(name -> name.endsWith("ona"))
                .forEach(result::add);

        assertThat(result).hasSize(3);
    }

    @Test
    void shouldReturnDistinctValuesFromCollection() {
        final List<String> names = List.of("Barcelona", "Barcelona", "Girona", "LLeida", "Tarragona");

        final List<String> result = names.stream()
                .distinct()
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("Barcelona", "Girona", "LLeida", "Tarragona");
    }

    @Test
    void shouldCountValuesFromCollection() {
        final List<String> names = List.of("Barcelona", "Girona", "LLeida", "Tarragona");

        final long count = names.stream()
                .filter(name -> name.endsWith("ona"))
                .count();

        assertThat(count).isEqualTo(3L);
    }

    @Test
    void shouldReduceToMaxValueFromCollection() {
        final List<Integer> numbers = List.of(1, 6, 3, 2);

        final Optional<Integer> max = numbers.stream()
                .max(Integer::compareTo);

        assertThat(max).isNotEmpty();
        assertThat(max.get()).isEqualTo(6);
    }

    @Test
    void shouldReduceToMinValueFromCollection() {
        final List<Integer> numbers = List.of(1, 6, 3, 2);

        final Optional<Integer> min = numbers.stream()
                .min(Integer::compareTo);

        assertThat(min).isNotEmpty();
        assertThat(min.get()).isEqualTo(1);
    }

    @Test
    void shouldCustomReduceValuesFromCollection() {
        final List<String> names = List.of("aaa", "aa", "a");

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
