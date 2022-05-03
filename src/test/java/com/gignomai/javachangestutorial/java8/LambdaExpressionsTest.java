package com.gignomai.javachangestutorial.java8;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class LambdaExpressionsTest {

    @Test
    void shouldApplyLambdaCode() {
        final List<String> names = List.of("Barcelona","Girona","LLeida","Tarragona");
        List<String> result = new ArrayList<>();

        names.forEach(name -> result.add(name.toUpperCase()));

        assertThat(result).containsExactlyInAnyOrder("BARCELONA", "GIRONA", "LLEIDA", "TARRAGONA");
        assertThat(result).doesNotContain("Barcelona","Girona","LLeida","Tarragona");
    }

    @Test
    void shouldUseMethodReferenceAsLambda() {
        final List<String> names = List.of("Barcelona","Girona","LLeida","Tarragona");

        final List<String> result = names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        assertThat(result).containsExactlyInAnyOrder("BARCELONA", "GIRONA", "LLEIDA", "TARRAGONA");
        assertThat(result).doesNotContain("Barcelona","Girona","LLeida","Tarragona");
    }
}
