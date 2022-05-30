package com.gignomai.javachangestutorial.java11;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class StringMethodsTest {

    public static final String EXAMPLE_MULTILINE_STRING = "First line \n \n second line \n third line.";

    @Test
    void shouldSplitStringIntoAList() {
        final List<String> lines = EXAMPLE_MULTILINE_STRING.lines()
                .collect(Collectors.toList());
        assertThat(lines).containsExactly("First line ", " ", " second line ", " third line.");
    }

    @Test
    void shouldFilterBlankLines() {
        final List<String> lines = EXAMPLE_MULTILINE_STRING.lines()
                .filter(line -> !line.isBlank())
                .collect(Collectors.toList());
        assertThat(lines).containsExactly("First line ", " second line ", " third line.");
    }

    @Test
    void shouldStripLines() {
        final List<String> lines = EXAMPLE_MULTILINE_STRING.lines()
                .map(String::strip)
                .collect(Collectors.toList());
        assertThat(lines).containsExactly("First line", "", "second line", "third line.");
    }

    @Test
    void shouldTrimStartBlanks() {
        final List<String> lines = EXAMPLE_MULTILINE_STRING.lines()
                .map(String::stripLeading)
                .collect(Collectors.toList());
        assertThat(lines).containsExactly("First line ", "", "second line ", "third line.");
    }

    @Test
    void shouldTrimEndBlanks() {
        final List<String> lines = EXAMPLE_MULTILINE_STRING.lines()
                .map(String::stripTrailing)
                .collect(Collectors.toList());
        assertThat(lines).containsExactly("First line", "", " second line", " third line.");
    }

    @Test
    void shouldRepeat() {
        final String origin = "yeah!";
        final String result = origin.repeat(3);

        assertThat(result).isEqualTo("yeah!yeah!yeah!");
    }
}
